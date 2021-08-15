package ro.apxsoftware.demodoc.controllers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.apxsoftware.demodoc.entities.Appointment;
import ro.apxsoftware.demodoc.entities.CompanyService;
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.entities.User;
import ro.apxsoftware.demodoc.service.AppointmentService;
import ro.apxsoftware.demodoc.service.CompanyServiceService;
import ro.apxsoftware.demodoc.service.PersonService;
import ro.apxsoftware.demodoc.service.UserService;

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
	
	@GetMapping("/")
	public String displayIndex(Model model, HttpSession session, Authentication auth) {

		model.addAttribute("appointment", new Appointment());
		
		//get all the doctors in the house
		//and send them to the view
		
		Set<Person> doctors = persServ.getDoctors();
		model.addAttribute("doctors", doctors);
		
		if(auth != null ) {
			

			//getting the list of authorities for the logged in user to 
			//present the right view 
			List<GrantedAuthority> listAuth = (List<GrantedAuthority>) auth.getAuthorities();
			for(GrantedAuthority userauth : listAuth) {
				if(userauth.getAuthority().equals("DOCTOR")) {
					return "doctor/dashboard";
				}
				
				if(userauth.getAuthority().equals("PACIENT")) {
					

					
					//get all the services
					
					return "index";
				}
				
				
				System.out.println(userauth.getAuthority());
			}
			
			
		}
			
			

		return "index";
		
		
	}
	
	
	
	@PostMapping("/makeAppointment")
	public String makeAnAppointment(Model model, @RequestParam(value="doctor", required=false) Long doctorId, @RequestParam(value="service", required=false) String service,
				Appointment appointment, User userAccount, Person person, Authentication auth, RedirectAttributes redirAttr) {

		
		if(auth != null) {
			
			
			appointment.setDoctor(null);
			appointment.setPacient(person);
			appServ.saveApp(appointment);
			CompanyService coServ = new CompanyService();
			coServ.setName(service);
			coServ.setAppointment(appointment);
			coservServ.saveCompanyService(coServ);

			
		} else {
			if(doctorId == null) {
				System.out.println("is not a doctor in the house?");
				appointment.setDoctor(null);
				appointment.setPacient(null);
				appointment.setAppointmentToken(UUID.randomUUID().toString());
				appServ.saveApp(appointment);
				CompanyService coServ = new CompanyService();
				coServ.setName(service);
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);
				
			} else {
				Person doctor = persServ.findPersonByUserId(doctorId);
				
				appointment.setDoctor(doctor);
				appointment.setPacient(null);
				appointment.setAppointmentToken(UUID.randomUUID().toString());
				appServ.saveApp(appointment);
				CompanyService coServ = new CompanyService();
				coServ.setName(service);
				coServ.setAppointment(appointment);
				coservServ.saveCompanyService(coServ);
			
			}
			//get the doctor by Id
	
			//save in the database the appointment 
			//and use the data to store as a pacient so create a person with a useraccount 
			//but not active, that can't log on etc.
		}
		
		redirAttr.addFlashAttribute("appointmentMade", new String("Your appointment was made. You have an email with your appointment details."));
		
		
		
		return "redirect:/";
	}
}
