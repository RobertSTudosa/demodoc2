package ro.apxsoftware.demodoc.controllers;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.apxsoftware.demodoc.entities.ConfirmationToken;
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.entities.User;
import ro.apxsoftware.demodoc.service.ConfirmationTokenService;
import ro.apxsoftware.demodoc.service.EmailService;
import ro.apxsoftware.demodoc.service.PersonService;
import ro.apxsoftware.demodoc.service.UserService;

//import com.bzbees.hrma.entities.ConfirmationToken;
//import com.bzbees.hrma.entities.Notification;
//import com.bzbees.hrma.entities.Person;
//import com.bzbees.hrma.entities.User;
//import com.bzbees.hrma.services.ConfirmationTokenService;
//import com.bzbees.hrma.services.EmailService;
//import com.bzbees.hrma.services.NotificationService;
//import com.bzbees.hrma.services.PersonService;
//import com.bzbees.hrma.services.UserService;

@Controller
@SessionAttributes({"emailAddress","userAccount","newPassword","token","userNotifs", "person"})
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	UserService userServ;
	
	@Autowired
	PersonService persServ;
	
	@Autowired
	ConfirmationTokenService confTokenServ;
	
	@Autowired
	EmailService emailServ;
	
	@Autowired
	BCryptPasswordEncoder bCryptEncoder;
	
//	@Autowired
//	NotificationService notifServ;
	
	
	@GetMapping(value={"","/"})
	public String displayLoginForm (Model model, HttpSession session, Authentication authentication) {
		
		if(authentication != null) {
			System.out.println("User is logged in " +  authentication.getName());			
		}
		
		Person person = new Person();
		model.addAttribute("person", person);
		session.setAttribute("person", person);
		
		
		User userAccount = new User();
		model.addAttribute("userAccount", userAccount);
		
		
		
		System.out.println("is the login controller here");
		
		return "login";
	}
	
	
	 
	
	
	@GetMapping("/forgot")
	public String displayForgotPassword (@ModelAttribute ("sendPassChange") String sendPassChange, Model model) {
		
		String emailAddress = "";
		
		model.addAttribute("emailAddress", emailAddress);
		
		model.addAttribute("sendPassChange", sendPassChange);
		
		return "forgot";
	}
	
	@PostMapping("/sendPassChange")
	public String sendPassChangeEmail ( Model model, @RequestParam("emailAddress") String emailAddress, User userAccount, RedirectAttributes redirAttr ) {
		
		System.out.println("select agency query... 1");
		if(userServ.findUserByEmailAddress(emailAddress) != null) {
			System.out.println("select agency query... 2");
			userAccount = userServ.findUserByEmailAddress(emailAddress);
			System.out.println("user before sending email is ---> " + userAccount.getUsername());
			ConfirmationToken confirmationToken = new ConfirmationToken(userAccount);
			confTokenServ.saveToken(confirmationToken);
			
			SimpleMailMessage mailMessage = emailServ.simpleChangePassMessage(emailAddress, "BZBees - Change your password", confirmationToken.getConfirmationToken());
			emailServ.sendEmail(mailMessage);
			redirAttr.addAttribute("sendPassChange", "We sent you an email for reseting your password.");
		} else {
			redirAttr.addAttribute("sendPassChange", "Email incorrect");
		}
		
		return "redirect:/login/forgot";
	}
	
	@GetMapping("/emailChangePassword")
	public String showPasswordChange(Model model, @RequestParam("c4") String confirmationToken,  
			 HttpSession session, HttpServletRequest request, Authentication auth, 
			 RedirectAttributes redirAttr) {
		
		ConfirmationToken checkedToken = confTokenServ.findConfirmationTokenByConfirmationToken(confirmationToken);
		User userAccount = checkedToken.getUser();
		System.out.println("User found by token " + userAccount.getUsername());
		Person person = persServ.findPersonByUserId(userAccount.getUserId());
		
		
//		if(auth != null) {
//			if(!notifServ.findNotificationsByUserId(userAccount.getUserId()).isEmpty()) {
//				List<Notification> allUserNotif = notifServ.reverseFindNotificationsByUserId(userAccount.getUserId());
//				model.addAttribute("userNotifs", allUserNotif);
//			} else {
//				model.addAttribute("userNotifs", new ArrayList<>());
//			}
//			
//		} else {
//			model.addAttribute("userNotifs", new ArrayList<>());
//		}
				
//		request.getSession();		
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount,null, userAccount.getAuthorities());
//		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String newPassword = "";
		model.addAttribute("newPassword", newPassword);
		model.addAttribute("token", checkedToken);
		model.addAttribute("userAccount", userAccount);
		model.addAttribute("person", person);
		model.addAttribute("userEmail", userAccount.getEmail());

		return "changePassword";
	}
	
	
//	@RequestMapping(value="/changePassword", method= {RequestMethod.GET, RequestMethod.POST})
	@PostMapping("/changePassword")
	public String changePassword(Model model, @ModelAttribute("userAccount") User userAccount, HttpServletRequest request, 
			Person person,
			 RedirectAttributes redirAttr, Authentication auth) {
		
		String newPassword = request.getParameter("newPassword");
		String username = request.getParameter(userAccount.getUsername());
		String userEmail = request.getParameter("userEmail");
//		System.out.println("What password is here : " + newPassword);
//		System.out.println("Username from the request " + username);
//		System.out.println("Username's email is also : " + userEmail);
//		System.out.println("Token is here " + request.getParameter("token"));
//		System.out.println("Why is the user nul?????!!!!" + userAccount.getUsername());
		
		person = persServ.findPersonByUserId(userAccount.getUserId());
		userAccount.setPassword(bCryptEncoder.encode(newPassword));		
		persServ.save(person);
		userServ.save(userAccount);
		
//		if(auth != null) {
//			if(!notifServ.findNotificationsByUserId(userAccount.getUserId()).isEmpty()) {
//				List<Notification> allUserNotif = notifServ.reverseFindNotificationsByUserId(userAccount.getUserId());
//				model.addAttribute("userNotifs", allUserNotif);
//			} else {
//				model.addAttribute("userNotifs", new ArrayList<>());
//			}
//			
//		} else {
//			model.addAttribute("userNotifs", new ArrayList<>());
//		}
		
		
		
		redirAttr.addAttribute("passChanged", "Password successfully updated.");
		redirAttr.addAttribute("success", "Changed password for user: " + userAccount.getUsername());
		model.addAttribute("userAccount", userAccount);
		model.addAttribute("person", person);
		
		return "redirect:/login/confirmation";
	}
	

	@GetMapping("/confirmation")
	public String confirmPassChange(Model model, 
			@ModelAttribute("passChanged") String passChanged,
			@ModelAttribute("success") String success,
			@ModelAttribute("userAccount") User userAccount, 
			Authentication auth,
			HttpServletRequest request) {
		
		System.out.println("User in the confirmation page: " + userAccount.getUsername());
		User user = userServ.findUserByEmailAddress(userAccount.getEmail());
		
//		if(auth != null) {
//			if(!notifServ.findNotificationsByUserId(userAccount.getUserId()).isEmpty()) {
//				List<Notification> allUserNotif = notifServ.reverseFindNotificationsByUserId(userAccount.getUserId());
//				model.addAttribute("userNotifs", allUserNotif);
//			} else {
//				model.addAttribute("userNotifs", new ArrayList<>());
//			}
//			
//		} else {
//			model.addAttribute("userNotifs", new ArrayList<>());
//		}
		
		request.getSession();		
		Authentication authentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "confirmation";
		
	}
	
	
	

	
}
