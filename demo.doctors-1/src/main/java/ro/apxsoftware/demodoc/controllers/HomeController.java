package ro.apxsoftware.demodoc.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.apxsoftware.demodoc.entities.Appointment;
import ro.apxsoftware.demodoc.entities.CompanyService;
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.entities.ProfileImg;
import ro.apxsoftware.demodoc.entities.User;
import ro.apxsoftware.demodoc.service.AppointmentService;
import ro.apxsoftware.demodoc.service.CompanyServiceService;
import ro.apxsoftware.demodoc.service.DatesAndTimeService;
import ro.apxsoftware.demodoc.service.EmailService;
import ro.apxsoftware.demodoc.service.PersonService;
import ro.apxsoftware.demodoc.service.ProfileImgService;
import ro.apxsoftware.demodoc.service.UserService;
import ro.apxsoftware.demodoc.utils.AppDateFormater;

@Controller
@SessionAttributes({"emailAddress","userAccount","newPassword","token","userNotifs", "person"})
@RequestMapping("/")
public class HomeController {
	
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
	ProfileImgService profileImgServ;
	
 
	@Autowired
	DatesAndTimeService dtServ;
	
	@Autowired
	AppDateFormater adf;
	
	@GetMapping("/")
	public String displayIndex(Model model, HttpSession session, Authentication auth, RedirectAttributes redirAttr) throws JsonProcessingException {

		model.addAttribute("appointment", new Appointment());
		
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
		 
		
		 
		 model.addAttribute("fixedTimes", null);
		 
		 //start checking for doctor 1;
		List<String> stringBusyDates = getAllBusyDates(model, redirAttr);
 
			 model.addAttribute("busyDates", stringBusyDates);
		

		//get all the doctors in the house
		//and send them to the view for the future only the names and ids
		
		Set<Person> doctors = persServ.getDoctors();
		model.addAttribute("doctors", doctors);
		
		
		
		
		
		
		if(auth != null ) {
			//get the logged in user
			User user = (User) userServ.loadUserByUsername(auth.getName());
			model.addAttribute("userAccount", user);
			Person person = persServ.findPersonByUserId(user.getUserId());
			model.addAttribute("person", person);
			long personId = person.getPersonId();
			System.out.println("person id is --->" + personId );
			model.addAttribute("userId", user.getUserId());

			//getting the list of authorities for the logged in user to 
			//present the right view 
			List<GrantedAuthority> listAuth = (List<GrantedAuthority>) auth.getAuthorities();
			for(GrantedAuthority userauth : listAuth) {
				if(userauth.getAuthority().equals("DOCTOR")) {
					
					
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
					
					return "doctor/dashboard";
				}
				
				if(userauth.getAuthority().equals("PACIENT")) {
					//THIS IS WHERE YOU PREPARE FOR CLIENT INTERACTION
					
					nextAppointment = appServ.findNextAppointByPacientId(personId);
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
					
					return "index";
					
				}
				
				
				System.out.println(userauth.getAuthority());
			}
			
			
		}
			
			

		return "index";
		
		
	}
	
	
	
	@PostMapping("/makeAppointment")
	@Transactional
	public String makeAnAppointment(Model model, @Valid  Appointment appointment, @RequestParam(value="doctor", required=false) Long doctorId, @RequestParam(value="service", required=false) String service,
			@RequestParam(value="time", required=true) CharSequence time, @RequestParam(value="date", required = true) CharSequence date,
				  BindingResult result, Errors errors, User userAccount, Person person, Authentication auth, HttpSession session, RedirectAttributes redirAttr) {

	
		if(errors.hasErrors()) {
			return "redirect:/#appointment";
		}
		
		if(result.hasErrors()) {
			return "redirect:/#appointment";
		}
		
		System.out.println(appointment.getAppointmentTime());
		
		Appointment newApp = new Appointment();
		
		LocalTime theTime = LocalTime.parse(time);
		LocalDate theDate = dtServ.formatToLocalDate(date);
		//form a query to get next doctor available

		
		//user logged in
		if(auth != null) {
			
			if(doctorId == null) {
//				appointment.setDoctor(null);
				appointment.setDoctor(persServ.findNextDoctorAvailable(theDate, theTime));
				appointment.setPacient(person);
				appointment.setAppointmentToken(UUID.randomUUID().toString());
				appointment.setAppointmentTime(theTime);
				appointment.setPacientEmail(person.getEmail());

				
				appServ.saveApp(appointment);
				CompanyService coServ = new CompanyService();
				coServ.setName(service + "");
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);
				
			} else {
				Person doctor = persServ.findPersonByUserId(doctorId);
				
				appointment.setDoctor(doctor);
				appointment.setPacient(person);
				appointment.setAppointmentToken(UUID.randomUUID().toString());
				appointment.setAppointmentTime(theTime);
				appointment.setPacientEmail(person.getEmail());


				
				appServ.saveApp(appointment);
				CompanyService coServ = new CompanyService();
				coServ.setName(service+ "");
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);
			}
			

			
		} else {
			//no user logged in
			//check for a doctor if selected
			if(doctorId == null) {
				System.out.println("is not a doctor in the house?");
				
				//get the next available doctor and assign this to him 
				//appointment.setDoctor(null);
				appointment.setDoctor(persServ.findNextDoctorAvailable(theDate, theTime));
				
				
				appointment.setPacient(null);
				appointment.setAppointmentToken(UUID.randomUUID().toString());
				appointment.setAppointmentTime(theTime);
//				appointment.setDate(theDate);
//				appointment.removeAppointmentFixedTime(time);
				
				appServ.saveApp(appointment);
				CompanyService coServ = new CompanyService();
				coServ.setName(service + "");
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);
				
			//no user logged in 
			//doctor is selected	
			} else {
				Person doctor = persServ.findPersonByUserId(doctorId);
				
				appointment.setDoctor(doctor);
				appointment.setPacient(null);
				appointment.setAppointmentToken(UUID.randomUUID().toString());
				appointment.setAppointmentTime(theTime);
//				appointment.setDate(theDate);
//				appointment.removeAppointmentFixedTime(appointment.getAppointmentTime());
				
//				appServ.saveApp(appointment);
				newApp = appServ.saveAppAndReturn(appointment);
				CompanyService coServ = new CompanyService();
				coServ.setName(service+ "");
				coServ.setAppointment(newApp);
				coservServ.saveAndFlush(coServ);
//				coservServ.saveCompanyService(coServ);
//				coservServ.dbflush();
				
					for(CompanyService coServone : newApp.getCompanyServices()) {
						if(coServone.equals(coServ)) {
							System.out.println("company service in the set ===> " + coServone.getName());
						}
					}
				

			}
		
		}

		
//		SimpleMailMessage appointMail = emailServ.simpleAppointmentConfirmation(appointment.getPacientEmail(), 
//				"Your Appointment with Medicio", appointment.getAppointmentToken());
		
		MimeMessage mimeAppointMail = emailServ.confirmAppointmentMimeEmail(appointment.getPacientEmail(), 
				"Your Appointment with Medicio", appointment.getAppointmentToken(),service);
		
		emailServ.sendMimeEmail(mimeAppointMail);
		
//		emailServ.sendEmail(appointMail);
		redirAttr.addFlashAttribute("appointmentMade", new String("Programarea dvs a fost efectuata. Am trimis detaliile pe adresa dvs: " + appointment.getPacientEmail() + "."));
		
		return "redirect:/";
	}
	
	@GetMapping("/pacientAppointment")
	public String showPacientAppointment(@RequestParam("at") String appointmentToken, Model model,  HttpSession session, HttpServletRequest request, Authentication auth, 
			 RedirectAttributes redirAttr) {
		Appointment theAppoint = appServ.getAppointmentByToken(appointmentToken);
		model.addAttribute("appointment", theAppoint);
		
		Set<CompanyService> appointServices = theAppoint.getCompanyServices();
		model.addAttribute("appointmentServices", appointServices);
		
		
		List<String> stringBusyDates = dtServ.getAllBusyDates(model, redirAttr);			 
		model.addAttribute("busyDates", stringBusyDates);
		
		Set<Person> doctors = persServ.getDoctors();
		model.addAttribute("doctors", doctors);
		
		//SEND AN ARRAY OF LOCALTIME FROM 09 TO 18 TO THE VIEW AS TIMES 
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
		 
		 
		
		return "pacientAppointment";
	}
	

	
	
	@GetMapping(value="/getDatesByDoctorId", produces = { "application/json" })
	@ResponseBody
	public List<String> getBusyDatesByDoctorId (Model model, @RequestParam(value="doctor", required=false) Long doctorId) {
		
				//get all doctors appointments after current date
				List<Appointment> appointmentsAfterCurrentDate = appServ.getAppointmentsByDoctorIdandCurrentTimeNotCanceled(doctorId); 
				
				

				System.out.println("app after current date === size " + appointmentsAfterCurrentDate.size());
				
				//set an empty map to be populated 
				Map<LocalDate, ArrayList<LocalTime>> theList = new HashMap<>();
				
				//loop through all future appointments 
				for(int i = 0; i < appointmentsAfterCurrentDate.size(); i++) {
					
					//initialize a set of the times 
					ArrayList<LocalTime> theTimes = new ArrayList<>();
					
					//if the list does not contain the key then check if it contains the time also
					LocalDate iDate = appointmentsAfterCurrentDate.get(i).getDate();
					
					
					if(!theList.containsKey(appointmentsAfterCurrentDate.get(i).getDate())) {
						System.out.println("===========================");
						System.out.println("checking the date ---> " + appointmentsAfterCurrentDate.get(i).getDate());
						System.out.println("arraylist of times is ---> " + theTimes);
						
						//get the time to check if already in the set or not
						LocalTime iTime = appointmentsAfterCurrentDate.get(i).getAppointmentTime();
						System.out.println("what is the iTime? ===> " + iTime);
						
						
						if(!theTimes.contains(appointmentsAfterCurrentDate.get(i).getAppointmentTime())) {
							
							System.out.println("adding the i time in the date " + appointmentsAfterCurrentDate.get(i).getAppointmentTime());
							//we add the time to the empty set theTimes
							theTimes.add(iTime);
							
							//if we add the time of i we now compare with the whole List
							//we prepare a loop starting with the next after the initial one
							for(int j = 1; j < appointmentsAfterCurrentDate.size(); j++) {
								//we get the j appointment date
								LocalDate jDate = appointmentsAfterCurrentDate.get(j).getDate();
								//if the dates are the same we aff the time of the jDate to theTimes
								if(iDate.equals(jDate)) {
									
									LocalTime jTime = appointmentsAfterCurrentDate.get(j).getAppointmentTime();
									
									System.out.println("checking if date [i] == date [j] " + "\n"
											+ "date [i] ---> " + appointmentsAfterCurrentDate.get(i).getDate() + "\n"
											+ "date [j] ---> " + appointmentsAfterCurrentDate.get(j).getDate());	
									
									
									
									//we check if the jTime is not already in theTimes 
									//because no double of the hours can be there
									
									System.out.println("this are theTimes " + theTimes);
									
									if(!theTimes.contains(jTime)){
										theTimes.add(jTime);
										
										System.out.println("adding the j time==> " + jTime);
										
										
									}
									
								} else {
									System.out.println("date [i] != date [j] where j is " + j);
								}
							}
							
							//after we finished adding all the times on the same date 
							//we complete the map 
							theList.put(appointmentsAfterCurrentDate.get(i).getDate(), theTimes);
							
							//we print it out to the console
							System.out.println("printing out theTimes after putting it in the map " + theTimes);
							for(LocalDate keyDate : theList.keySet()) {
								System.out.println(keyDate + " : " + theList.get(keyDate));
							}
							System.out.println("=========================================");
						}
					} else {
						System.out.println("this is already in the list");
					}
					
					
				}
				
				List<LocalTime> fixedTimesArray = new ArrayList<LocalTime>() {{
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
				 
				 fixedTimesArray.forEach((t) -> System.out.println("fixes times array entry : " + t) );
				 

				 List<LocalDate> busyDates =  new ArrayList<>();
				 
				 for(LocalDate keyDate : theList.keySet()) {
					 
					 List<LocalTime> toCheckTimes = new ArrayList<>();
					 
					 if(theList.get(keyDate) != null) {
						 toCheckTimes.addAll(theList.get(keyDate));
						 
					 }
					 
					 Collections.sort(toCheckTimes);
					 System.out.println("print out the sorted times for the keySet");
					 toCheckTimes.forEach((t) -> System.out.println("toCheckTimes entry : " + t));
					 System.out.println("toCheckTimes.size is : " + toCheckTimes.size());
					 
					 int count = 0;
					 for(int i = 0; i < toCheckTimes.size(); i++) {
						 if(toCheckTimes.get(i).equals(fixedTimesArray.get(i))) {
							 count++;
							 System.out.println("count is ---> " + count);
						 }
						 
						 System.out.println("fixedTimes Array is " + fixedTimesArray.toString());
					 }
					 

					 
					 if(count > 8) {
						 System.out.println("adding the toString() date : " + keyDate.toString() + " to the busyDates");
						 busyDates.add(keyDate);
						 System.out.println("added the date : " + keyDate + " to the busyDates");

					 } else {
						 
						 System.out.println("there still time in the day");
						
					 }
					 
				 }
				 
				 System.out.println("these are the busy dates : " + busyDates);
				 

				 //build a list of strings for javascript 	
				 List<String> stringBusyDates = new ArrayList<>();
				 for(int i = 0; i < busyDates.size(); i++) {
					 stringBusyDates.add(busyDates.get(i).toString());
				 }
				 
			model.addAttribute("busyDates", stringBusyDates);	 
				
				
			return stringBusyDates;
	}
	
	
	
	@GetMapping(value="/selectTimeByDateByDoctor", produces = { "application/json" })
	@ResponseBody
	public List<LocalTime> getFixedTimesByDateByDoctor (@RequestParam("date") String date, @RequestParam("doctor") long doctorId, Model model, RedirectAttributes redirAttr) {
		
		int doctorsCount = userServ.getHowManyUserOfType("DOCTOR"); // get the number of doctors in the app
		
		List<LocalTime> theFixedTimes = new ArrayList<>(); // this will be the resulted array
		
		//prepare a list of LocalTime as standard
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

		System.out.println("calling the time by date=================");
		

		List<Appointment> appointByDate = appServ.getAppointmentsByDoctorIdandDateNotCanceled(doctorId, date);
		
		//collect all LocalTimes from the result and subtract from the prepared LocalTime list
		for(int i = 0; i < appointByDate.size(); i++) {
			System.out.println("appointment time to remove " + appointByDate.get(i).getAppointmentTime());
			fixedTimes.remove(appointByDate.get(i).getAppointmentTime());
		}

		model.addAttribute("fixedTimes", fixedTimes);
		
		System.out.println(appointByDate.toString());
		System.out.println("these are the fixedTimes printed ---> " + fixedTimes.toString());
		
		
		//run getFixedTimesByDate then cross with doctor's fixed times. 
		//allFixedTimes will always be an array equal to 9 or less(from start hour to end)
		List<LocalTime> allFixedTimes = this.getFixedTimesByDate(date, model, redirAttr);
		List<LocalTime> addAllTimes = new ArrayList<>();
		System.out.println("allFixedTimes ===>" + allFixedTimes.toString());
		addAllTimes.addAll(allFixedTimes);
		System.out.println("after adding allFixedTimes ran from getTimesByDate addAllTimes is " + addAllTimes.toString());
		addAllTimes.addAll(fixedTimes);
		System.out.println("after adding local fixedTimes addAllTimes is " + addAllTimes.toString());
		
		
		//check with 2 because of the allFixedTimes array
		for(int i = 0; i < addAllTimes.size(); i++) {
			if(Collections.frequency(addAllTimes, addAllTimes.get(i)) == 2) {
				
				if(!theFixedTimes.contains(addAllTimes.get(i))) {
					theFixedTimes.add(addAllTimes.get(i));					
				}

			}
		}
		
		System.out.println("theFixedTimes is the returning array ==> " + theFixedTimes.toString());
		
		
		return theFixedTimes;
	}
	
	
	
	@GetMapping(value="/selectTimeByDate", produces = { "application/json" })
	@ResponseBody
	public List<LocalTime> getFixedTimesByDate (@RequestParam("date") String date, Model model, RedirectAttributes redirAttr) {
		
		
		int doctorsCount = userServ.getHowManyUserOfType("DOCTOR"); // get the number of doctors in the app

		List<Long> doctorsIds = userServ.getListOfUsersIdsOfType("DOCTOR"); // get a list with docs ids
		
		List<LocalTime> allTimes = new ArrayList<>(); // prepare an arraylist for all times;
		
		List<LocalTime> noTimes = new ArrayList<>(); // prepare an arraylist to be sent to model
		
		// prepare a list of LocalTime as standard
		List<LocalTime> fixedTimes = new ArrayList<LocalTime>() {
						{
							add(LocalTime.of(9, 0, 0, 0));
							add(LocalTime.of(10, 0, 0, 0));
							add(LocalTime.of(11, 0, 0, 0));
							add(LocalTime.of(12, 0, 0, 0));
							add(LocalTime.of(14, 0, 0, 0));
							add(LocalTime.of(15, 0, 0, 0));
							add(LocalTime.of(16, 0, 0, 0));
							add(LocalTime.of(17, 0, 0, 0));
							add(LocalTime.of(18, 0, 0, 0));

						}
					};

		

			

			System.out.println("calling the time by date=================");

			
			List<Appointment> appointBydDate = appServ.getAllAppointmentsByDateNotCanceled(date);
			System.out.println("appointments retrieved --->" + appointBydDate.toString());
			// collect all LocalTimes from the result and subtract from the prepared
			// LocalTime list
			for (int i = 0; i < appointBydDate.size(); i++) {
				System.out.println("appointment time to add to allTimes " + appointBydDate.get(i).getAppointmentTime());
				
				allTimes.add(appointBydDate.get(i).getAppointmentTime());
			}


			
			

	
		
		System.out.println("print all times ----> " + allTimes.toString());
		
		for(int i = 0; i < allTimes.size(); i ++) {
			
			if(Collections.frequency(allTimes, allTimes.get(i)) == doctorsCount) {
								
				if(!noTimes.contains(allTimes.get(i))) {
					noTimes.add(allTimes.get(i));
					fixedTimes.remove(allTimes.get(i));

				}
			}
			
		}
		
		
		List<LocalTime> newFixedTimes = new ArrayList<> ();
		
		for(int i = 0; i < fixedTimes.size(); i++ ) {
			newFixedTimes.add(fixedTimes.get(i));
		}
		
		model.addAttribute("fixedTimes", fixedTimes);		
		
		System.out.println("no Times ===> " + noTimes.toString());
		System.out.println("newFixedTimes ===> " + newFixedTimes.toString());
		System.out.println(" date checked was ===> " + date);
		System.out.println("fixed Times ===> " + fixedTimes.toString());
		
		return newFixedTimes;
	}
	
	
	@GetMapping("/allBusyDates")
	@ResponseBody
	public List<String> getAllBusyDates (Model model, RedirectAttributes redirAttr) {
		

				int doctorsCount = userServ.getHowManyUserOfType("DOCTOR"); //get the number of doctors in the app 
				
				List<Long> doctorsIds = userServ.getListOfUsersIdsOfType("DOCTOR"); //get a list with docs ids 
				
				List<String> allBusyDates = new ArrayList<>();
					 
					
					List<Appointment> appointments = appServ.getAppointmentsAfterCurrentDateNotCanceled();
					System.out.println("appointments size = " + appointments.size());
				
					List<LocalDate> allDates = new ArrayList<>();
					
					List<LocalDate> datesToCheck = new ArrayList<>();
					
					List<LocalDate> busyDates = new ArrayList<>();
					
					//add all dates to an array
					for (int x = 0; x < appointments.size(); x++) {

						allDates.add(appointments.get(x).getDate());

					}
					
					//if one date is repeating more than one in the allDates then we add it
					//if it is NOT contained already
					for (int i = 0; i < allDates.size(); i++) {
						if(Collections.frequency(allDates, allDates.get(i)) >= 1) {
							if(!datesToCheck.contains(allDates.get(i))) {
								datesToCheck.add(allDates.get(i));
	
							}
						}
					}
					
					System.out.println("dates to check " + datesToCheck.toString());
					
					
					
					for(int i = 0; i < datesToCheck.size(); i++) {
						List<LocalTime> fixedTimeOfTheDay = getFixedTimesByDate(datesToCheck.get(i).toString(), model, redirAttr);
						System.out.println("fixedTimes for date : " + datesToCheck.get(i) + " are : " + fixedTimeOfTheDay.toString());
						if(fixedTimeOfTheDay.isEmpty()) {
							busyDates.add(datesToCheck.get(i));
						}
					}
					

					 //build a list of strings for javascript 	
					 List<String> stringBusyDates = new ArrayList<>();
					 for(int i = 0; i < busyDates.size(); i++) {
						 stringBusyDates.add(busyDates.get(i).toString());
					 }
					 
				
					 allBusyDates.addAll(stringBusyDates);
					 
				
					 System.out.println("all busy dates are ===> " + allBusyDates);
			
				return allBusyDates;
	}
	
	
	@GetMapping(value="/getMonthAppointments", produces = {" application/json" })
//	@ResponseStatus(HttpStatus.OK)
	public String currentMonthAppointmentsByDoctorId(Model model, @RequestParam("month") String month, 
			@RequestParam(value="canceled", required=false ) String isCanceled, RedirectAttributes redirAttr, 
			Authentication auth)  {
		
		List<Appointment> userAppointmentsByMonth = new ArrayList<>();
		int canceled = 0;
		if(isCanceled != null) {
			 canceled = Integer.parseInt(isCanceled);
		}
		
		if(auth != null) {
			User user = (User) userServ.loadUserByUsername(auth.getName());
			System.out.println("month is month --> " + month);
			
			List<GrantedAuthority> listAuth = (List<GrantedAuthority>) auth.getAuthorities();
			for(GrantedAuthority userauth : listAuth) {
				if(userauth.getAuthority().equals("PACIENT")) {
					
					//get the current month and if it is smaller then run and return clientPastAppointments with pastDoctorAppointments
					LocalDate ld = LocalDate.now();
					System.out.println("Current month is " + ld.getMonthValue());
					int intMonth = Integer.parseInt(month);					
					if(ld.getMonthValue() > intMonth) {
						//increase the integer month because from view it comes from a zero based array 
						//as a minus one value;
						intMonth += 1; 
						String stringMonth = String.valueOf(intMonth);
						System.out.println("intMonth pasted is " + ld.getMonthValue());
						
						//if it has cancel than call the cancel query
						if(canceled == 1) {
							userAppointmentsByMonth = appServ.getAllCanceledAppointmentsByPersonIdByMonth(user.getUserId(), stringMonth);
							System.out.println("CANCELED pacientAppointmentsByMonth " + userAppointmentsByMonth.size());
							model.addAttribute("canceledDoctorAppointments", userAppointmentsByMonth);
							return "pacient/historyAppointments :: #canceledClientAppointments";
						}
						userAppointmentsByMonth = appServ.getAllPastAppointmentsByPersonIdByMonth(user.getUserId(), stringMonth);
						System.out.println("PAST pacientAppointmentsByMonth " + userAppointmentsByMonth.size());
						model.addAttribute("pastDoctorAppointments", userAppointmentsByMonth);
						
						return "pacient/historyAppointments :: #clientPastAppointments";
					}
					
					
					
					//get future appointments by person id and month
					//if it has cancel than call the cancel query
					if(canceled == 1) {
						userAppointmentsByMonth = appServ.getAllCanceledAppointmentsByPersonIdByMonth(user.getUserId(), month);
						System.out.println("CANCELED pacientAppointmentsByMonth " + userAppointmentsByMonth.size());
						model.addAttribute("canceledDoctorAppointments", userAppointmentsByMonth);
						return "pacient/historyAppointments :: #canceledClientAppointments";
					}
					userAppointmentsByMonth = appServ.getAllFutureAppointmentsByPersonIdByMonth(user.getUserId(), month);
					System.out.println("FUTURE pacientAppointmentsByMonth " + userAppointmentsByMonth.size());
					model.addAttribute("doctorAppointments", userAppointmentsByMonth);
					
					return "pacient/historyAppointments :: #clientfutureAppointments";
					
					
					}//end of pacient check
				
				
				}
			
			
			//get the current month and if it is smaller then run and return clientPastAppointments with pastDoctorAppointments
			LocalDate ld = LocalDate.now();
			System.out.println("Current month is " + ld.getMonthValue());
			int intMonth = Integer.parseInt(month);					
			if(ld.getMonthValue() > intMonth) {
				//increase the integer month because from view it comes from a zero based array 
				//as a minus one value;
				intMonth += 1; 
				String stringMonth = String.valueOf(intMonth);
				System.out.println("intMonth pasted is " + ld.getMonthValue());
				if(canceled == 1) {
					userAppointmentsByMonth = appServ.getAllPastAppointmentsByDoctorIdByMonthAndCanceled(user.getUserId(), stringMonth);
					model.addAttribute("cancelDoctorAppointments", userAppointmentsByMonth);
					return "doctor/dashboard :: #doctorCancelAppointments";
				}
				userAppointmentsByMonth = appServ.getAllPastAppointmentsByDoctorIdByMonth(user.getUserId(), stringMonth);
				System.out.println("PAST doctorAppointmentsByMonth " + userAppointmentsByMonth.size());
				model.addAttribute("pastDoctorAppointments", userAppointmentsByMonth);
				
				return "doctor/dashboard :: #doctorPastAppointments";
			}
			
			
			
			//get future appointments by person id and month
			if(canceled == 1) {
				userAppointmentsByMonth = appServ.getAllPastAppointmentsByDoctorIdByMonthAndCanceled(user.getUserId(),month);
				model.addAttribute("cancelDoctorAppointments", userAppointmentsByMonth);
				return "doctor/dashboard :: #doctorCancelAppointments";
			}
			
			userAppointmentsByMonth = appServ.getAllFutureAppointmentsByDoctorIdByMonth(user.getUserId(), month);
			System.out.println("FUTURE doctorAppointmentsByMonth " + userAppointmentsByMonth.size());
			model.addAttribute("doctorAppointments", userAppointmentsByMonth);
			
			return "doctor/dashboard :: #futureAppointments";
			
		
		}
				
		model.addAttribute("doctorAppointments", userAppointmentsByMonth);
		
		 
		
		return "doctor/dashboard :: #futureAppointments";
	}
	
	
	@GetMapping(value="/getMoreMonthAppointments", produces = {" application/json" })
	public String getMoreMonthAppointments(Model model, @RequestParam("month") String month, 
				@RequestParam("lastAppId") long lastAppId, 
				@RequestParam(value="canceled", required=false ) String isCanceled,
				RedirectAttributes redirAttr, 
			Authentication auth)  {
		
		System.out.println("method param month ---------> " + month);
		
		System.out.println("primul println");
		List<Appointment> sixMoreAppointments = new ArrayList<>();
		List<Appointment> allPersonAppointmentsByMonth = new ArrayList<>();
		


		
		
		
		int canceled = 0;
		
		if(isCanceled != null) {
			 canceled = Integer.parseInt(isCanceled);
		}
		
		
		if(auth != null) {
			
			
			//SEND AN ARRAY OF LOCALTIME FROM 09 TO 18 TO THE VIEW AS TIMES 
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
			
			List<String> stringBusyDates = dtServ.getAllBusyDates(model, redirAttr);			 
			model.addAttribute("busyDates", stringBusyDates);
			
			Set<Person> doctors = persServ.getDoctors();
			model.addAttribute("doctors", doctors);
			
			User user = (User) userServ.loadUserByUsername(auth.getName());
			model.addAttribute("userAccount", user);
			
			Person person = persServ.findPersonByUserId(user.getUserId());
			System.out.println("person id is --->" + person.getPersonId());
			
			model.addAttribute("person", person);
			
			
			
			System.out.println("month is month --> " + month);
			System.out.println("lastAppId is ---> " + lastAppId);
			
			
			List<GrantedAuthority> listAuth = (List<GrantedAuthority>) auth.getAuthorities();
			for(GrantedAuthority userauth : listAuth) {
				if(userauth.getAuthority().equals("PACIENT")) {
					LocalDate ld = LocalDate.now();
					System.out.println("Current month is " + ld.getMonthValue());
					int intMonth = Integer.parseInt(month);					
					//if is in the past
					if(ld.getMonthValue() > intMonth) {
						//increase the integer month because from view it comes from a zero based array 
						//as a minus one value;
						intMonth += 1; 
						String stringMonth = String.valueOf(intMonth);
						System.out.println("intMonth passed is " + intMonth);
						System.out.println("stringMonth is " + stringMonth);
						if(canceled == 1) {
							
							System.out.println("string month passed when canceled " + stringMonth);
							allPersonAppointmentsByMonth = appServ.getAllPastAppointmentsByPersonIdByMonthNoLimitAndCanceled(user.getUserId(), stringMonth);
						} else {
							allPersonAppointmentsByMonth = appServ.getAllPastAppointmentsByPersonIdByMonthNoLimit(user.getUserId(), stringMonth);
							System.out.println("calling past appointments from else");
						}
						
						System.out.println("PacientAppointmentsByMonthNoLimit " + allPersonAppointmentsByMonth.size());
						
						int count = 0;
						for(int i = 0; i < allPersonAppointmentsByMonth.size(); i++) {
							Appointment appoint = allPersonAppointmentsByMonth.get(i);
							if(count < 3) {
								sixMoreAppointments.add(appoint);
								if(appoint.getAppointmentId() > lastAppId) {
									count++;
								}

							}
						}
						
						
						model.addAttribute("pastDoctorAppointments", sixMoreAppointments);
						System.out.println("pastDoctorAppointments to model " + sixMoreAppointments.size());
						System.out.println("doctorAppointments to model " + sixMoreAppointments.toString());
						
						return "pacient/historyAppointments :: #clientPastAppointments";
					}//end if Current Month is bigger than selected month
					
					if(canceled == 1 ) {
						allPersonAppointmentsByMonth = appServ.getAppointmentByPersonIdAndMonthNotLimitCanceled(user.getUserId(), month);
					} else {
						allPersonAppointmentsByMonth = appServ.getAppointmentByPersonIdAndMonthNotLimit(user.getUserId(), month);
					}
					
					System.out.println("pacientAppointmentsByMonthNoLimit " + allPersonAppointmentsByMonth.size());
					
					int count = 0;
					for(int i = 0; i < allPersonAppointmentsByMonth.size(); i++) {
						Appointment appoint = allPersonAppointmentsByMonth.get(i);
						if(count < 3) {
							sixMoreAppointments.add(appoint);
							if(appoint.getAppointmentId() > lastAppId) {
								count++;
							}

						}
					}
					
					if(canceled == 1) {
						System.out.println("in the canceledDoctorAppointments");
						model.addAttribute("canceledDoctorAppointments", sixMoreAppointments);
						return "pacient/historyAppointments :: #canceledClientAppointments";
					}
					
					model.addAttribute("doctorAppointments", sixMoreAppointments);
					System.out.println("futuredoctorAppointments to model " + sixMoreAppointments.size());
					System.out.println("futuredoctorAppointments to model " + sixMoreAppointments.toString());
					
					return "pacient/historyAppointments :: #clientfutureAppointments";
					
					
				}//end of If PACIENT
			}//end of FOR LOOP --> for authorities
			System.out.println("================outside pacient check============");
			LocalDate ld = LocalDate.now();
			System.out.println("Current month is " + ld.getMonthValue());
			int intMonth = Integer.parseInt(month);					
			//if is in the past
			if(ld.getMonthValue() > intMonth) {
				//increase the integer month because from view it comes from a zero based array 
				//as a minus one value;
				intMonth += 1; 
				
				String stringMonth = String.valueOf(intMonth);
				System.out.println("intMonth passed is " + ld.getMonthValue());
				
				if(canceled == 1) {
					allPersonAppointmentsByMonth = appServ.getAllPastAppointmentsByDoctorIdByMonthNoLimitAndCanceled(user.getUserId(), stringMonth);

				} else {
					allPersonAppointmentsByMonth = appServ.getAllPastAppointmentsByDoctorIdByMonthNoLimit(user.getUserId(), stringMonth);

				}
				
				
				System.out.println("past doctorAppointmentsByMonthNoLimit " + allPersonAppointmentsByMonth.size());
				
				int count = 0;
				for(int i = 0; i < allPersonAppointmentsByMonth.size(); i++) {
					Appointment appoint = allPersonAppointmentsByMonth.get(i);
					if(count < 3) {
						sixMoreAppointments.add(appoint);
						if(appoint.getAppointmentId() > lastAppId) {
							count++;
						}

					}
				}
				
				
				model.addAttribute("pastDoctorAppointments", sixMoreAppointments);
				System.out.println("pastDoctorAppointments to model " + sixMoreAppointments.size());
				System.out.println("doctorAppointments to model " + sixMoreAppointments.toString());
				
				return "doctor/dashboard :: #doctorPastAppointments";
			}//end if Current Month is bigger than selected month
			
			if(canceled == 1) {
				allPersonAppointmentsByMonth = appServ.getAllFutureAppointmentsByDoctorIdByMonthNoLimitAndCanceled(user.getUserId(), month);
			} else {
				//allPersonAppointmentsByMonth = appServ.getAllFutureAppointmentsByDoctorIdByMonthNoLimit(user.getUserId(), month);
				sixMoreAppointments = appServ.getSixMoreFutureAppointmentsByDoctorIdByMonthByLastAppId(user.getUserId(), month, lastAppId);
				model.addAttribute("doctorAppointments", sixMoreAppointments);
				return "doctor/dashboard :: #futureAppointments";

			}
			
			System.out.println("doctorAppointmentsByMonthNoLimit " + allPersonAppointmentsByMonth.size());
			
			int count = 0;
			for(int i = 0; i < allPersonAppointmentsByMonth.size(); i++) {
				Appointment appoint = allPersonAppointmentsByMonth.get(i);
				if(count < 3) {
					sixMoreAppointments.add(appoint);
					if(appoint.getAppointmentId() > lastAppId) {
						count++;
					}

				}
			}
			
			model.addAttribute("doctorAppointments", sixMoreAppointments);
			System.out.println("futuredoctorAppointments to model " + sixMoreAppointments.size());
			System.out.println("futuredoctorAppointments to model " + sixMoreAppointments.toString());
			
		}//end if auth!=null
				
		model.addAttribute("doctorAppointments", sixMoreAppointments);
		
		
		 
		
		return "doctor/dashboard :: #futureAppointments";
	}
	
	
	@GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
	
	
	@GetMapping(value="/searchAppointments", produces = {" application/json" })
	public <T> String simpleSearchAppointments (Model model, @RequestParam("keyword") String keyword,@RequestParam("search") String search,
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
		if(search.equals("appointSearch")) {
			
			searchResults = (List<T>) appServ.listAll(userId, keyword);
		}
		
		if(search.equals("clientSearch")) {
			
		}//end if search from client


		
		System.out.println(searchResults.toString());
		
		model.addAttribute("keywords", searchResults);
		model.addAttribute("searchMessage",  keyword);
		
	
		
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
		
				
		//get all the FUTURE appointments sorted ascending in query
		List<Appointment> doctorAppointments = appServ.getAllAppointmentsByDoctorIdByCurrentMonthNotCanceled(user.getUserId());
		
		model.addAttribute("doctorAppointments", doctorAppointments);
		
		List<Appointment> pastDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonth(user.getUserId());					
		model.addAttribute("pastDoctorAppointments", pastDoctorAppointments);
		
		List<Appointment> canceledDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonthAndCanceled(user.getUserId());
		model.addAttribute("cancelDoctorAppointments", canceledDoctorAppointments);
		
		
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
			 
		
		return "doctor/dashboard";
		
		

	}
	
	
	@GetMapping(value="/loadMoreSearchAppointments", produces = {" application/json" })
	public String loadMoreSimpleSearchAppointments (Model model, @RequestParam("keyword") String keyword, @RequestParam("lastAppId") long lastAppId, RedirectAttributes redirAttr, 
				Authentication auth ) {
		
		if(auth == null) {
			return "redirect:/";
		}
		
		model.addAttribute("appointment", new Appointment());
		User user = (User) userServ.loadUserByUsername(auth.getName());
		model.addAttribute("userAccount", user);
		
		Person person = persServ.findPersonByUserId(user.getUserId());
		System.out.println("person id is --->" + person.getPersonId());
		long personId = person.getPersonId();
		model.addAttribute("person", person);
		
		
		System.out.println("keyword : " + keyword);
		long userId = user.getUserId();
		List<Appointment> searchResults = appServ.listAllNoLimit(userId,keyword);
		List<Appointment> sixMoreSearchResults = new ArrayList<>();
		
		int count = 0;
		for(int i = 0; i < searchResults.size(); i++) {
			Appointment appoint = searchResults.get(i);
			if(count < 3) {
				sixMoreSearchResults.add(appoint);
				if(appoint.getAppointmentId() > lastAppId) {
					count++;
				}

			}
		}
		
		System.out.println(searchResults.toString());
		
		model.addAttribute("keywords", sixMoreSearchResults);	
		
		model.addAttribute("searchMessage",  keyword);
		
		
		
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
		
		
		//get all the FUTURE appointments sorted ascending in query
		List<Appointment> doctorAppointments = appServ.getAllAppointmentsByDoctorIdByCurrentMonthNotCanceled(user.getUserId());
		
		model.addAttribute("doctorAppointments", doctorAppointments);
		
		List<Appointment> pastDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonth(user.getUserId());					
		model.addAttribute("pastDoctorAppointments", pastDoctorAppointments);
		
		List<Appointment> canceledDoctorAppointments = appServ.getAllAppointmentsByDoctorIdAndUpToCurrentMonthAndCanceled(user.getUserId());
		model.addAttribute("cancelDoctorAppointments", canceledDoctorAppointments);
		
		
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
			 
		
		//return "doctor/dashboard :: #searchResults";
				return null;
	}
	
	@GetMapping(value="/cancelAppointmentFM")
	public String cancelAppointment(@RequestParam("appToken") String token, Model model, Authentication auth, RedirectAttributes redirAttr) {
		Person person = new Person ();
		
		if(auth != null) {
			User user = (User) userServ.loadUserByUsername(auth.getName());
			model.addAttribute("userAccount", user);
			
			person = persServ.findPersonByUserId(user.getUserId());
			
			model.addAttribute("person", person);
			
		}
		
		Appointment appToCancel = appServ.getAppointmentByToken(token);
		appToCancel.setCanceled(true);
		
		if(auth != null) {
			appToCancel.setCanceledId(person.getPersonId());			
		}

		appServ.saveApp(appToCancel);
		
		
		List<Appointment> clientAppointments = appServ.getAllAppointmentsByPersonIdByCurrentMonthNotCanceled(person.getPersonId());
		model.addAttribute("doctorAppointments", clientAppointments);
		
		List<Appointment> canceledClientAppointments = appServ.getAllAppointmentsByPersonIdAndUpToCurrentMonthCanceled(person.getPersonId());
		model.addAttribute("canceledDoctorAppointments", clientAppointments);
		
		MimeMessage mimeAppointMail = emailServ.canceledAppointmentMimeEmail(appToCancel.getPacientEmail(), 
				"Your Appointment with Medicio", appToCancel.getAppointmentToken(), appToCancel.getStringCompanyServicesNames());
		
		emailServ.sendMimeEmail(mimeAppointMail);

		
		redirAttr.addFlashAttribute("appointmentMade", new String("Programarea dvs a fost anulata."));
		
		return "redirect:/";
	}
	
	
	
}
