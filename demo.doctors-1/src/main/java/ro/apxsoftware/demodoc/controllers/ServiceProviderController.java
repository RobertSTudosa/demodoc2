package ro.apxsoftware.demodoc.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.apxsoftware.demodoc.entities.Appointment;
import ro.apxsoftware.demodoc.entities.CompanyService;
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.entities.ProfileImg;
import ro.apxsoftware.demodoc.entities.User;
import ro.apxsoftware.demodoc.service.AppointmentService;
import ro.apxsoftware.demodoc.service.CompanyServiceService;
import ro.apxsoftware.demodoc.service.DatesAndTimeService;
import ro.apxsoftware.demodoc.service.EmailService;
import ro.apxsoftware.demodoc.service.ImageResize;
import ro.apxsoftware.demodoc.service.PersonService;
import ro.apxsoftware.demodoc.service.ProfileImgService;
import ro.apxsoftware.demodoc.service.UserService;

@Controller
@SessionAttributes({"emailAddress","userAccount","newPassword","token","userNotifs", "person"})
@RequestMapping("/doctor")
public class ServiceProviderController {
	@Autowired
	UserService userServ;
	
	@Autowired
	PersonService persServ;
	
	@Autowired
	AppointmentService appServ;
	
	@Autowired
	CompanyServiceService coservServ;
	
	@Autowired
	EmailService emailServ;
	
	@Autowired
	BCryptPasswordEncoder bCryptEncoder;
	
	@Autowired
	ImageResize imgResizerServ;
	
	@Autowired
	ProfileImgService profileImgServ;
	
	 
		@Autowired
		DatesAndTimeService dtServ;
	
	
	@GetMapping("/profile")
	public String getDoctorProfile(Model model, Authentication auth, RedirectAttributes redirAttr) {
		
		if(auth == null) {
			return "redirect:/";
		}
		
		if(auth != null) {
			
			model.addAttribute("appointment", new Appointment());
			
			User user = (User) userServ.loadUserByUsername(auth.getName());
			model.addAttribute("userAccount", user);
			
			Person person = persServ.findPersonByUserId(user.getUserId());
			model.addAttribute("person", person);
			long personId = person.getPersonId();
			System.out.println("person id is --->" + personId );
			
			
			
			//get the pics from the profileImg repo
			List<ProfileImg> personPics = profileImgServ.getPicsByPersonId(personId);
			
			//get the last pic of the person's profile from profileImg repo
			ProfileImg theImg = profileImgServ.getLastProfilePic(personId);
			if(theImg != null) {
				System.out.println("in the if !null of the image");
				model.addAttribute("img", theImg);	
				
				List<ProfileImg> lastPicList = new ArrayList<>();
				lastPicList.add(profileImgServ.getLastProfilePic(person.getPersonId()));
				model.addAttribute("lastPicList", lastPicList);
				
			} else {
				personPics.clear();
				System.out.println("ALL CLEAR");
				model.addAttribute("img", new ProfileImg());
				model.addAttribute("lastPicList", new ArrayList<>());
			}
		}
		
		return "doctor/profile";
	}
	
	@GetMapping("/updateProfile")
	public String editDoctorProfile(@Valid @ModelAttribute(value="userAccount") User patchUser, Model model, User user, 
				Person person, HttpServletRequest request, RedirectAttributes redirAtrr, Authentication auth ) {
		
		System.out.println("patch User is ---> " + patchUser.getUsername());
		user = userServ.findUserByPersonId(patchUser.getUserId());
		user.setEmail(patchUser.getEmail());
		user.setUserName(patchUser.getEmail());
		String newPassword = request.getParameter("newPassword");
		user.setPassword(bCryptEncoder.encode(newPassword));
		
		
		person = persServ.findPersonById(patchUser.getUserId());
		person.setEmail(patchUser.getEmail());
		person.setFirstName(patchUser.getPerson().getFirstName());
		person.setPhone(patchUser.getPerson().getPhone());
		person.setJobWishDesc(patchUser.getPerson().getJobWishDesc());
		
		userServ.save(user);
		persServ.save(person);

		
		
		System.out.println("new password is ---> " + newPassword);
//		redirAtrr.addAttribute("userAccount", user);
		model.addAttribute("userAccount", user);
		
		List<GrantedAuthority> listAuth = (List<GrantedAuthority>) auth.getAuthorities();
		for(GrantedAuthority userauth : listAuth) {
			if(userauth.getAuthority().equals("PACIENT")) {
				return "redirect:/client/profile";
			}
				
			}
		
		return "doctor/profile";
	}
	
	@PostMapping(value = "/addProfileImg", consumes = { "multipart/form-data" })
	public String addProfileImg(Model model, Person person, @RequestParam("img") MultipartFile img, Authentication auth,
			RedirectAttributes redirAttr) {
		
		if(img == null) {
			return "redirect:/doctor/profile";
		}
		
		
		System.out.println("original name of the image" + img.getName());
		
		User user = (User) userServ.loadUserByUsername(auth.getName());

		person = persServ.findPersonByUserId(user.getUserId());
		
		String imgName = StringUtils.cleanPath(img.getOriginalFilename());
	
		//init a new byte array	
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
	    //the bytes you want for writing your class image 
	    byte[] newBytes = null;
		    
	    	baos = imgResizerServ.resizeImage(img);
		
			newBytes = baos.toByteArray();
		    
		    try {
				baos.close();
			} catch (IOException e) {
				System.out.println("ByteArrayOutputSteam is not closed " );
				e.printStackTrace();
			}
		    
		    System.out.println("newbytes ====== new money " + newBytes.toString());   
		    
		    
			ProfileImg smallImg = new ProfileImg(imgName, img.getContentType(), newBytes);
			
			

			List<ProfileImg> picList = profileImgServ.getPicsByPersonId(person.getPersonId());

			smallImg.setPerson(person);

			profileImgServ.savePic(smallImg);
			
			picList.add(smallImg);

			List<ProfileImg> lastPicList = new ArrayList<>();

			lastPicList.add(profileImgServ.getLastProfilePic(person.getPersonId()));
			
			person.setLastImgId(smallImg.getPicId());
			
			persServ.save(person);

			redirAttr.addFlashAttribute("lastPicList", lastPicList);
			redirAttr.addFlashAttribute("picList", picList);
			redirAttr.addFlashAttribute("img", smallImg);
			
			List<GrantedAuthority> listAuth = (List<GrantedAuthority>) auth.getAuthorities();
			for(GrantedAuthority userauth : listAuth) {
				
				if(userauth.getAuthority().equals("PACIENT")) {
					return "redirect:/client/profile";
				}
					
			}
			
			

		return "redirect:/doctor/profile";
	}
	
	@GetMapping(value = "/img")
	public ResponseEntity<?> showProfileImg(@RequestParam("imgId") long id, Person person) {

		ProfileImg profilePic = profileImgServ.findProfilePicById(id);
		if(profilePic == null) {
			return null;
		}
		
		System.out.println("made it so far so --- > image id is --> " + profilePic.getPicId());
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(profilePic.getPicType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + profilePic.getPicName() + "\"")
				.body(new ByteArrayResource(profilePic.getData()));
		

	}
	
	@GetMapping(value= "/lastImg")
	public ResponseEntity<?> showLastProfileImg(@RequestParam("personId") long personId) {
		
		ProfileImg lastProfilePic = profileImgServ.getLastProfilePic(personId);
		
		if(lastProfilePic == null) {
			return null;
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(lastProfilePic.getPicType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + lastProfilePic.getPicName() + "\"")
				.body(new ByteArrayResource(lastProfilePic.getData()));
	}
	
	@GetMapping(value="/clients")
	public String showClients(Model model, Authentication auth, RedirectAttributes redirAttr ) {
		
		if(auth != null ) {
			
			model.addAttribute("appointment", new Appointment());
			
			//get the logged in user
			User user = (User) userServ.loadUserByUsername(auth.getName());
			model.addAttribute("userAccount", user);
			Person person = persServ.findPersonByUserId(user.getUserId());
			model.addAttribute("person", person);
			long personId = person.getPersonId();
			System.out.println("person id is --->" + personId );
			model.addAttribute("userId", user.getUserId());
			
			//get 6 pacients in a list to populate a table 
			List<Person> serviceProvidersClients = persServ.getClientsForProviderByProviderId(person.getPersonId());
			model.addAttribute("clientsByProvider", serviceProvidersClients);
			
			
			//get all the FUTURE appointments sorted ascending in query
			List<Appointment> doctorAppointments = appServ.getAllAppointmentsByDoctorIdByCurrentMonthNotCanceled(user.getUserId());
			
			model.addAttribute("doctorAppointments", doctorAppointments);
			
			List<Appointment> pastDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonth(user.getUserId());					
			model.addAttribute("pastDoctorAppointments", pastDoctorAppointments);
			
			
			System.out.println("no of future appointments of the doctor are " + doctorAppointments.size());
			
			//get the pics from the profileImg repo
			List<ProfileImg> personPics = profileImgServ.getPicsByPersonId(personId);
			
			//get the last pic of the person's profile from profileImg repo
			ProfileImg theImg = profileImgServ.getLastProfilePic(personId);
			if(theImg != null) {
				System.out.println("in the if !null of the image");
				model.addAttribute("img", theImg);	
				
				List<ProfileImg> lastPicList = new ArrayList<>();
				lastPicList.add(profileImgServ.getLastProfilePic(person.getPersonId()));
				model.addAttribute("lastPicList", lastPicList);
				
			} else {
				personPics.clear();
				System.out.println("ALL CLEAR");
				model.addAttribute("img", new ProfileImg());
				model.addAttribute("lastPicList", new ArrayList<>());
			}
			
			List<LocalTime> fixedTimes = new ArrayList<LocalTime>() {{
				 add(LocalTime.of(9,0,0,0));
				 add(LocalTime.of(10,0,0,0));
				 add(LocalTime.of(11,0,0,0));
				 add(LocalTime.of(12,0,0,0));
				 add(LocalTime.of(14,0,0,0));
				 add(LocalTime.of(15,0,0,0));
				 add(LocalTime.of(16,0,0,0));
				 add(LocalTime.of(17,0,0,0));
				 add(LocalTime.of(18,0,0,0));
				 
				 
			 }};
			 
			
			 
			 model.addAttribute("fixedTimes", fixedTimes);
			 
			 //start checking for doctor 1;
			List<String> stringBusyDates = dtServ.getAllBusyDates(model, redirAttr);
	 
				 model.addAttribute("busyDates", stringBusyDates);
			

			//get all the doctors in the house
			//and send them to the view for the future only the names and ids
			
			Set<Person> doctors = persServ.getDoctors();
			model.addAttribute("doctors", doctors);
			

		
		
		} else {
			return "redirect:/";
		}
		
		
		return "doctor/clients";
	}
	
	
	@GetMapping(value="/getClientsByMonth", produces= {" application/json" })
	public String getClientsByMonthByProvider (Model model, @RequestParam("month") String month, Authentication auth) {
				
		List<Person> allClientsByProvider = new ArrayList<>();
		
		if(auth != null) {
			User user = (User) userServ.loadUserByUsername(auth.getName());
			Person person = (Person) persServ.findPersonByUserId(user.getUserId());
			System.out.println("Loaded user is " + user.getUserName());
			System.out.println("month is month --> " + month);
			
			
			//get the current month and if it is smaller then run and return clientPastAppointments with pastDoctorAppointments
			LocalDate ld = LocalDate.now();
			System.out.println("Current month is " + ld.getMonthValue());
			int intMonth = Integer.parseInt(month);					
			if(ld.getMonthValue() > intMonth) {
				//increase the integer month because from view it comes from a zero based array 
				//as a minus one value;
				System.out.println("intMonth before plus " + intMonth);
				intMonth += 1; 
				String stringMonth = String.valueOf(intMonth);
				System.out.println("intMonth passed is " + intMonth);
				
				allClientsByProvider = persServ.getAllClientsByServiceIdByMonth(person.getPersonId(), stringMonth);
				System.out.println("PAST clientsByProviderByMonth " + allClientsByProvider.toString());
				for(Person client : allClientsByProvider) {
					System.out.println("client available --> " + client.getEmail());
				}
				model.addAttribute("clientsByProvider", allClientsByProvider);
				return "doctor/clients :: #clientsByProvider";
		
			}

		}
		
		
		return "doctor/clients :: #clientsByProvider";
	}
	
	
	@GetMapping(value="/getMoreClientsByMonth", produces= {" application/json" })
	public String getMoreClientsByMonthByProvider (Model model, @RequestParam("month") String month, @RequestParam("lastPersonId") long lastPersonId, Authentication auth) {
	
		List<Person> allClientsByProvider = new ArrayList<>();
		List<Person> sixMoreClients = new ArrayList<>();
		
		if(auth != null) {
			User user = (User) userServ.loadUserByUsername(auth.getName());
			Person person = (Person) persServ.findPersonByUserId(user.getUserId());
			System.out.println("Loaded user is " + user.getUserName());
			System.out.println("month is month --> " + month);
			
			
			//get the current month and if it is smaller then run and return clientPastAppointments with pastDoctorAppointments
			LocalDate ld = LocalDate.now();
			System.out.println("Current month is " + ld.getMonthValue());
			int intMonth = Integer.parseInt(month);					
			if(ld.getMonthValue() > intMonth) {
				//increase the integer month because from view it comes from a zero based array 
				//as a minus one value;
				System.out.println("intMonth before plus " + intMonth);
				intMonth += 1; 
				String stringMonth = String.valueOf(intMonth);
				System.out.println("intMonth passed is " + intMonth);
				
				allClientsByProvider = persServ.getAllClientsByServiceIdByMonthNoLimit(person.getPersonId(), stringMonth);
				System.out.println("PAST clientsByProviderByMonth " + allClientsByProvider.toString());
				for(Person client : allClientsByProvider) {
					System.out.println("client available --> " + client.getEmail());
				}
								
				int count = 0;
				for(int i = 0; i < allClientsByProvider.size(); i++) {
					Person XTRperson = allClientsByProvider.get(i);
					if(count < 3) {
						sixMoreClients.add(XTRperson);
						if(person.getPersonId() > lastPersonId) {
							count++;
						}

					}
				}
								
				model.addAttribute("clientsByProvider", sixMoreClients);
				return "doctor/clients :: #clientsByProvider";
		
			}

		}

		return "doctor/clients :: #clientsByProvider";
	}
	
	
	@GetMapping(value="/searchClients", produces = {" application/json" })
	public <T> String simpleSearchClients (Model model, @RequestParam("keyword") String keyword,@RequestParam("search") String search,
			RedirectAttributes redirAttr, 
				Authentication auth) {
		
		if(auth == null) {
			return "redirect:/";
		}
		
		model.addAttribute("appointment", new Appointment());
		System.out.println("search : " + search);

		
		List<T> searchResults = new ArrayList<>();
		

		
		System.out.println("keyword : " + keyword);
		

		
		User user = (User) userServ.loadUserByUsername(auth.getName());
		model.addAttribute("userAccount", user);
		
		Person person = persServ.findPersonByUserId(user.getUserId());
		System.out.println("person id is --->" + person.getPersonId());
		long personId = person.getPersonId();
		model.addAttribute("person", person);
		long userId = user.getUserId();		
		searchResults = (List<T>) persServ.listAll(userId, keyword);
		
		System.out.println(searchResults.toString());
		
		model.addAttribute("keywords", searchResults);
		model.addAttribute("selectClients", searchResults);
		model.addAttribute("searchMessage",  keyword);
		
//		model.addAttribute("doctorAppointments", searchResults);
//		model.addAttribute("pastDoctorAppointments", searchResults);
//		model.addAttribute("cancelDoctorAppointments", searchResults);
		
		//get 6 pacients in a list to populate a table 
		List<Person> serviceProvidersClients = persServ.getClientsForProviderByProviderId(person.getPersonId());
		model.addAttribute("clientsByProvider", serviceProvidersClients);
		
		
		//get the pics from the profileImg repo
		List<ProfileImg> personPics = profileImgServ.getPicsByPersonId(personId);
		
		//get the last pic of the person's profile from profileImg repo
		ProfileImg theImg = profileImgServ.getLastProfilePic(personId);
		if(theImg != null) {
			System.out.println("in the if !null of the image");
			model.addAttribute("img", theImg);	
			
			List<ProfileImg> lastPicList = new ArrayList<>();
			lastPicList.add(profileImgServ.getLastProfilePic(person.getPersonId()));
			model.addAttribute("lastPicList", lastPicList);
			
		} else {
			personPics.clear();
			System.out.println("ALL CLEAR");
			model.addAttribute("img", new ProfileImg());
			model.addAttribute("lastPicList", new ArrayList<>());
		}
		
		
		
		
		
		return "doctor/clients";
	}
	
	
	
	@GetMapping(value="/loadMoreSearchClients", produces = {" application/json" })
	public <T> String loadMoreSimpleSearchClients (Model model, @RequestParam("keyword") String keyword, @RequestParam("lastPersonId") long lastPersonId,
			RedirectAttributes redirAttr, 
				Authentication auth) {
		if(auth == null) {
			return "redirect:/";
		}
		
		model.addAttribute("appointment", new Appointment());

		List<T> searchResults = new ArrayList<>();

		System.out.println("keyword : " + keyword);
		
		User user = (User) userServ.loadUserByUsername(auth.getName());
		model.addAttribute("userAccount", user);
		
		Person person = persServ.findPersonByUserId(user.getUserId());
		System.out.println("person id is --->" + person.getPersonId());
		long personId = person.getPersonId();
		model.addAttribute("person", person);
		long userId = user.getUserId();		
		searchResults = (List<T>) persServ.listAll(userId, keyword);
		List<Person> sixMoreSearchResults = new ArrayList<>();
		
		int count = 0;
		for(int i = 0; i < searchResults.size(); i++) {
			T checkPerson = searchResults.get(i);
			if(count < 3) {
				sixMoreSearchResults.add((Person) checkPerson);
				if(((Person) checkPerson).getPersonId() > lastPersonId) {
					count++;
				}

			}
		}
		
		System.out.println(searchResults.toString());
		
		model.addAttribute("keywords", searchResults);
		model.addAttribute("searchMessage",  keyword);
		
//		model.addAttribute("doctorAppointments", searchResults);
//		model.addAttribute("pastDoctorAppointments", searchResults);
//		model.addAttribute("cancelDoctorAppointments", searchResults);
		
		//get 6 pacients in a list to populate a table 
		List<Person> serviceProvidersClients = persServ.getClientsForProviderByProviderId(person.getPersonId());
		model.addAttribute("clientsByProvider", serviceProvidersClients);
		
		
		//get the pics from the profileImg repo
		List<ProfileImg> personPics = profileImgServ.getPicsByPersonId(personId);
		
		//get the last pic of the person's profile from profileImg repo
		ProfileImg theImg = profileImgServ.getLastProfilePic(personId);
		if(theImg != null) {
			System.out.println("in the if !null of the image");
			model.addAttribute("img", theImg);	
			
			List<ProfileImg> lastPicList = new ArrayList<>();
			lastPicList.add(profileImgServ.getLastProfilePic(person.getPersonId()));
			model.addAttribute("lastPicList", lastPicList);
			
		} else {
			personPics.clear();
			System.out.println("ALL CLEAR");
			model.addAttribute("img", new ProfileImg());
			model.addAttribute("lastPicList", new ArrayList<>());
		}
		
		return "doctor/clients :: #searchResults";
	}
	
	
	@GetMapping(value="/searchClientsModal", produces = {" application/json" })
	public <T> String simpleSearchClientsModal (Model model, @RequestParam("keyword") String keyword,@RequestParam("search") String search,
			RedirectAttributes redirAttr, 
				Authentication auth) {
		
		model.addAttribute("appointment", new Appointment());
		
		if(auth == null) {
			return "redirect:/";
		}
				
		System.out.println("search : " + search);
		
		List<T> searchResults = new ArrayList<>();		
		
		System.out.println("keyword : " + keyword);
		
		User user = (User) userServ.loadUserByUsername(auth.getName());
		model.addAttribute("userAccount", user);
		
		Person person = persServ.findPersonByUserId(user.getUserId());
		System.out.println("person id is --->" + person.getPersonId());
		long personId = person.getPersonId();
		model.addAttribute("person", person);
		long userId = user.getUserId();		
		searchResults = (List<T>) persServ.listAll(userId, keyword);
		
		System.out.println(searchResults.toString());
		
		model.addAttribute("keywords", searchResults);
		model.addAttribute("selectClients", searchResults);
		model.addAttribute("searchMessage",  keyword);
		
		model.addAttribute("doctorAppointments", searchResults);
		model.addAttribute("pastDoctorAppointments", searchResults);
		model.addAttribute("cancelDoctorAppointments", searchResults);
		
		//get 6 pacients in a list to populate a table 
		List<Person> serviceProvidersClients = persServ.getClientsForProviderByProviderId(person.getPersonId());
		model.addAttribute("clientsByProvider", serviceProvidersClients);
		
		
		//get the pics from the profileImg repo
		List<ProfileImg> personPics = profileImgServ.getPicsByPersonId(personId);
		
		//get the last pic of the person's profile from profileImg repo
		ProfileImg theImg = profileImgServ.getLastProfilePic(personId);
		if(theImg != null) {
			System.out.println("in the if !null of the image");
			model.addAttribute("img", theImg);	
			
			List<ProfileImg> lastPicList = new ArrayList<>();
			lastPicList.add(profileImgServ.getLastProfilePic(person.getPersonId()));
			model.addAttribute("lastPicList", lastPicList);
			
		} else {
			personPics.clear();
			System.out.println("ALL CLEAR");
			model.addAttribute("img", new ProfileImg());
			model.addAttribute("lastPicList", new ArrayList<>());
		}
		
		
		List<LocalTime> fixedTimes = new ArrayList<LocalTime>() {{
			 add(LocalTime.of(9,0,0,0));
			 add(LocalTime.of(10,0,0,0));
			 add(LocalTime.of(11,0,0,0));
			 add(LocalTime.of(12,0,0,0));
			 add(LocalTime.of(14,0,0,0));
			 add(LocalTime.of(15,0,0,0));
			 add(LocalTime.of(16,0,0,0));
			 add(LocalTime.of(17,0,0,0));
			 add(LocalTime.of(18,0,0,0));
			 
			 
		 }};
		 
		
		 
		 model.addAttribute("fixedTimes", fixedTimes);
		 
		 //start checking for doctor 1;
		List<String> stringBusyDates = dtServ.getAllBusyDates(model, redirAttr);

			 model.addAttribute("busyDates", stringBusyDates);
			 
				Set<Person> doctors = persServ.getDoctors();
				model.addAttribute("doctors", doctors);	 
			 
				//get all the FUTURE appointments sorted ascending in query
				List<Appointment> doctorAppointments = appServ.getAllAppointmentsByDoctorIdByCurrentMonthNotCanceled(user.getUserId());
				
				model.addAttribute("doctorAppointments", doctorAppointments);
				
				List<Appointment> pastDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonth(user.getUserId());					
				model.addAttribute("pastDoctorAppointments", pastDoctorAppointments);
				
				
				List<Appointment> canceledDoctorAppointments = new ArrayList<Appointment>();
				canceledDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonthAndCanceled(user.getUserId());
				model.addAttribute("cancelDoctorAppointments", canceledDoctorAppointments);	 
		
		return "usermodals :: #modalSearchClientsWrapper";
	}
	
	@Transactional
	@GetMapping(value="/addTempAppointment", produces = {" application/json" })
	public String addTempAppointment(Model model, @RequestParam("personId") long personId,   @RequestParam(value="doctor", required=false) Long doctorId, 
			@RequestParam(value="service", required=false) String service,
			@RequestParam(value="time", required=true) CharSequence time, @RequestParam(value="date", required = true) CharSequence date,
			Appointment appointment, Authentication auth, RedirectAttributes redirAttr) {
		
		
		List<Appointment> tempAppointments = appServ.getTemporaryAppointmentsByPacientId(personId);
		System.out.println("tempAppointments before adding " + Arrays.asList(tempAppointments));
		Person client = persServ.findPersonById(personId);
		
		LocalTime theTime = LocalTime.parse(time);
		LocalDate theDate = LocalDate.parse(date);
		
		if(doctorId != null) {
			Person doctor = persServ.findPersonByUserId(doctorId);
			
			appointment.setDoctor(doctor);
			
		}
		
		appointment.setPacient(client);
		appointment.setPacientName(client.getFirstName());
		appointment.setAppointmentToken(UUID.randomUUID().toString());
		appointment.setAppointmentTime(theTime);
		appointment.setPacientEmail(client.getEmail());
		appointment.setTemporary(true);
		appointment.setDate(theDate);


		
//		appServ.saveApp(appointment);
		
		Set<CompanyService> existingCoServ = appointment.getCompanyServices();
		CompanyService coServ = new CompanyService();
		coServ.setName(service+ "");
		coServ.setAppointment(appointment);
		existingCoServ.add(coServ);
		appointment.setCompanyServices(existingCoServ);
		appServ.saveAndFlush(appointment);
//		coservServ.saveCompanyService(coServ);
		coservServ.saveAndFlush(coServ);
		
		appServ.dbflush();
		coservServ.dbflush();
		
		System.out.println("company services just saved " + coServ.getName());
		
		//create query for the temp appoint of the client
		
		
		tempAppointments.add(appointment);
		model.addAttribute("tempAppointments", tempAppointments);
		
		System.out.println(" ====== services for this appointment are ===> " + appointment.getCompanyServices().toString());
		System.out.println(" ====== String services for this appointment are ===> " + appointment.getStringCompanyServicesNames());
		
		return "usermodals :: #tempAppointList" ;
		
	}
	
	@GetMapping(value="/approveAllTempApp", produces = {" application/json"})
	public String approveAllTemporaryAppointments(Model model, @RequestParam("personId") long personId) {
		List<Appointment> tempAppointments = appServ.getTemporaryAppointmentsByPacientId(personId);
		for(Appointment temp : tempAppointments) 
			{
				temp.setTemporary(false);
				appServ.saveAndFlush(temp);
			}
		model.addAttribute("appointsMade", tempAppointments);
		
		return "doctor/dashboard :: #appointsMadeList";
		
	}
	
	@GetMapping(value="/deleteOneTempAppointment", produces = {" application/json" })
	@ResponseStatus(HttpStatus.OK)
	public void deleteOneTempAppointment(Model model, @RequestParam("appId") long appId) {
		
		System.out.println("appId passed to method is ---> " + appId);
		
		Appointment toDeleteApp = appServ.findAppointmentByAppointmentId(appId);
		Set<CompanyService> coServForDeletedApp = toDeleteApp.getCompanyServices();
		
		appServ.deleteAppointment(toDeleteApp);
		appServ.dbflush();
		
	}
	
	
	@GetMapping(value="/deleteTempAppointments", produces = {" application/json" })
	@ResponseStatus(HttpStatus.OK)
	public void deleteTempAppointments(Model model, @RequestParam("personId") long personId) {
		
		System.out.println("personId passed to delete temp apps method is ---> " + personId);
		List<Appointment> tempAppointments = appServ.getTemporaryAppointmentsByPacientId(personId);
		for(Appointment temp : tempAppointments) {
			appServ.deleteAppointment(temp);
			appServ.dbflush();
		}
	

	}
	
	
	@GetMapping(value="/adminProfile")
	public String showAdminPage(Model model, Authentication auth, RedirectAttributes redirAttr ) {
		
		model.addAttribute("appointment", new Appointment());
		
		
		User user = (User) userServ.loadUserByUsername(auth.getName());
		model.addAttribute("userAccount", user);
		Person person = persServ.findPersonByUserId(user.getUserId());
		model.addAttribute("person", person);
		long personId = person.getPersonId();
		System.out.println("person id is --->" + personId );
		model.addAttribute("userId", user.getUserId());
		
		Appointment nextAppointment = new Appointment();
		
//		SEND AN ARRAY OF LOCALTIME FROM 09 TO 18 TO THE VIEW AS TIMES 
		List<LocalTime> fixedTimes = new ArrayList<LocalTime>() {{
			 add(LocalTime.of(9,0,0,0));
			 add(LocalTime.of(10,0,0,0));
			 add(LocalTime.of(11,0,0,0));
			 add(LocalTime.of(12,0,0,0));
			 add(LocalTime.of(14,0,0,0));
			 add(LocalTime.of(15,0,0,0));
			 add(LocalTime.of(16,0,0,0));
			 add(LocalTime.of(17,0,0,0));
			 add(LocalTime.of(18,0,0,0));
			 
			 
		 }};
		 
		
		 
		 model.addAttribute("fixedTimes", fixedTimes);
		 
		 //start checking for doctor 1;
		List<String> stringBusyDates = dtServ.getAllBusyDates(model, redirAttr);
 
			 model.addAttribute("busyDates", stringBusyDates);
		

		//get all the doctors in the house
		//and send them to the view for the future only the names and ids
		
		Set<Person> doctors = persServ.getDoctors();
		model.addAttribute("doctors", doctors);
		
		
		nextAppointment = appServ.findNextAppointByDoctorId(personId);
		
		//get all the FUTURE appointments sorted ascending in query
		List<Appointment> doctorAppointments = appServ.getAllAppointmentsByDoctorIdByCurrentMonthNotCanceled(user.getUserId());
		
		model.addAttribute("doctorAppointments", doctorAppointments);
		
		List<Appointment> pastDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonth(user.getUserId());					
		model.addAttribute("pastDoctorAppointments", pastDoctorAppointments);
		
		
		List<Appointment> canceledDoctorAppointments = new ArrayList<Appointment>();
		canceledDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonthAndCanceled(user.getUserId());
		model.addAttribute("cancelDoctorAppointments", canceledDoctorAppointments);
		
		
		System.out.println("no of future appointments of the doctor are " + doctorAppointments.size());
		
		//get the pics from the profileImg repo
		List<ProfileImg> personPics = profileImgServ.getPicsByPersonId(personId);
		
		//get the last pic of the person's profile from profileImg repo
		ProfileImg theImg = profileImgServ.getLastProfilePic(personId);
		if(theImg != null) {
			System.out.println("in the if !null of the image");
			model.addAttribute("img", theImg);	
			
			List<ProfileImg> lastPicList = new ArrayList<>();
			lastPicList.add(profileImgServ.getLastProfilePic(person.getPersonId()));
			model.addAttribute("lastPicList", lastPicList);
			
		} else {
			personPics.clear();
			System.out.println("ALL CLEAR");
			model.addAttribute("img", new ProfileImg());
			model.addAttribute("lastPicList", new ArrayList<>());
		}
		
		model.addAttribute("lastAppointment", nextAppointment);
		
		
		
		
		return "doctor/admin";
	}
	
	

}
