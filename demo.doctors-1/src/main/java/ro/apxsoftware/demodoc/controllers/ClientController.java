package ro.apxsoftware.demodoc.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
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
@SessionAttributes({"emailAddress","userAccount","newPassword","token","userNotifs", "person","lastPicList"})
@RequestMapping(path="/client")
public class ClientController {
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
	
	@GetMapping(path="/profile")
	public String getDoctorProfile(Model model, Authentication auth, RedirectAttributes redirAttr) {
		
		if(auth == null) {
			return "redirect:/";
		}
		
		
		
		
		if(auth != null) {
			User user = (User) userServ.loadUserByUsername(auth.getName());
			model.addAttribute("userAccount", user);
			
			Person person = persServ.findPersonByUserId(user.getUserId());
			long personId = person.getPersonId();
			System.out.println("person id is --->" + personId );
			
			model.addAttribute("person", person);
			
			
			
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
		
		return "pacient/profile";
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
	
	@GetMapping(value="/historyAppointments")
	public String getClientHistoryAppointments(@RequestParam("personId") long personId, Model model, Authentication auth, RedirectAttributes redirAttr  ) {
		
		System.out.println("person Id is " + personId);
		
		if(auth == null) {
			return "redirect:/";
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
			
			Person person = persServ.findPersonById(personId);
			System.out.println("person id is --->" + personId );
			
			model.addAttribute("person", person);
			
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
			
		List<Appointment> clientAppointments = appServ.getAllAppointmentsByPersonIdByCurrentMonthNotCanceled(personId);
	
		model.addAttribute("doctorAppointments", clientAppointments);
		System.out.println("adding the future pacient appointments");
		
		List<Appointment> pastClientAppointments = appServ.getAllAppointmentsByPersonIdAndUpToCurrentMonth(personId);
		model.addAttribute("pastDoctorAppointments", pastClientAppointments);
		System.out.println("adding the pacient appointments");
		
		List<Appointment> canceledClientAppointments = appServ.getAllAppointmentsByPersonIdAndUpToCurrentMonthCanceled(personId);
		model.addAttribute("canceledDoctorAppointments", canceledClientAppointments);
		System.out.println("adding the canceled pacient appointments");
		
			
		}
		
		return "pacient/historyAppointments";
	}
	
	
	@GetMapping(value="/cancelAppointment")
	public String cancelAppointment(@RequestParam("appToken") String token, Model model, Authentication auth) {
		if(auth == null) {
			return "redirect: /";
		}
	
		User user = (User) userServ.loadUserByUsername(auth.getName());
		model.addAttribute("userAccount", user);
		
		Person person = persServ.findPersonByUserId(user.getUserId());
		
		model.addAttribute("person", person);
		
		Appointment appToCancel = appServ.getAppointmentByToken(token);
		appToCancel.setCanceled(true);
		appToCancel.setCanceledId(person.getPersonId());
		appServ.saveApp(appToCancel);
		
		
		List<Appointment> clientAppointments = appServ.getAllAppointmentsByPersonIdByCurrentMonthNotCanceled(person.getPersonId());
		model.addAttribute("doctorAppointments", clientAppointments);
		
		List<Appointment> canceledClientAppointments = appServ.getAllAppointmentsByPersonIdAndUpToCurrentMonthCanceled(person.getPersonId());
		model.addAttribute("canceledDoctorAppointments", clientAppointments);
		
		
		System.out.println("adding the pacient appointments");
		
		return "pacient/historyAppointments :: #canceledClientAppointments";
	}
	
	@Transactional
	@PostMapping("/remakeAppointment")
	public String remakeAnAppointment(@ModelAttribute Appointment patchApp,  @RequestParam(value="doctor", required=false) Long doctorId, 
			@RequestParam(value="service", required=false) String service,
			@RequestParam(value="time", required=true) CharSequence time,
			@RequestParam(value="date", required = false) CharSequence date,
			@RequestParam(value="appointmentToken") String token, Model model, User userAccount, Person person, 
			Authentication auth, HttpSession session, RedirectAttributes redirAttr) {

		Appointment appointment = appServ.getAppointmentByToken(token);
		Set<CompanyService> coServices = appointment.getCompanyServices();
		System.out.println("appointment's co services are here? = ==> " + coServices.toString());
		coservServ.deleteAllByAppointmentId(appointment.getAppointmentId());
		System.out.println("appointment's co services after delete = ==> " + coServices.toString());
		
		System.out.println("Appointment id is ---> " + appointment.getAppointmentId());
		
		LocalTime theTime = LocalTime.parse(time);
		LocalDate theDate = dtServ.formatToLocalDate(date);
		Person nextDoctor = persServ.findNextDoctorAvailable(theDate,theTime);
		
		long personId = 0L;
		
		//user logged in
		if(auth != null) {
			
			User user = (User) userServ.loadUserByUsername(auth.getName());
			model.addAttribute("userAccount", user);
			person = persServ.findPersonByUserId(user.getUserId());
			model.addAttribute("person", person);
			personId = person.getPersonId();
			//form a query to get next doctor available
			
			
			if(doctorId == null) {
				//appointment.setDoctor(null);
				appointment.setDoctor(nextDoctor);
				appointment.setPacient(person);
				//appointment.setAppointmentToken(UUID.randomUUID().toString());
				appointment.setAppointmentTime(theTime);
				appointment.setPacientEmail(person.getEmail());
				appointment.setDate(patchApp.getDate());
				appointment.setRescheduledId(person.getPersonId());
				appointment.setRescheduled(true);
				
				CompanyService coServ = new CompanyService();
				coServ.setName(service + "");
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);
				
				appServ.saveApp(appointment);
				

				
			} else {
				Person doctor = persServ.findPersonByUserId(doctorId);
				
				appointment.setDoctor(doctor);
				appointment.setPacient(person);
				
				appointment.setAppointmentTime(theTime);
				appointment.setPacientEmail(person.getEmail());
				appointment.setDate(patchApp.getDate());
				appointment.setCompanyServices(coServices);
				appointment.setRescheduledId(person.getPersonId());
				appointment.setRescheduled(true);
				
				CompanyService coServ = new CompanyService();
				coServ.setName(service + "");
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);
				
				appServ.saveApp(appointment);
					
				
			}
			

			
		} else {
			//no user logged in
			//check for a doctor if selected
			if(doctorId == null) {
				System.out.println("is not a doctor in the house?");
				//appointment.setDoctor(null);
				appointment.setDoctor(nextDoctor);
				appointment.setPacient(null);
				appointment.setPacientEmail(patchApp.getPacientEmail());
				appointment.setAppointmentTime(theTime);
				appointment.setDate(patchApp.getDate());
				appointment.setRescheduledId(person.getPersonId());
				appointment.setRescheduled(true);
				
				
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
				appointment.setPacientEmail(patchApp.getPacientEmail());
				appointment.setAppointmentTime(theTime);
				appointment.setDate(patchApp.getDate());
				appointment.setRescheduledId(person.getPersonId());
				appointment.setRescheduled(true);


				CompanyService coServ = new CompanyService();
				coServ.setName(service+ "");
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);

				
				appServ.saveApp(appointment);

				
			}
			
		}

		
//		SimpleMailMessage appointMail = emailServ.simpleAppointmentConfirmation(appointment.getPacientEmail(), 
//				"Your Appointment with Medicio", appointment.getAppointmentToken());
//		emailServ.sendEmail(appointMail);
		redirAttr.addFlashAttribute("appointmentMade", new String("Programarea dvs a fost efectuata. Am trimis detaliile pe adresa dvs: " + appointment.getPacientEmail() + "."));
		
		return "redirect:/client/historyAppointments?personId=" + personId;
	}
		
	
}
