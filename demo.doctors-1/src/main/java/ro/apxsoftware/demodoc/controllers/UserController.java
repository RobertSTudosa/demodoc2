package ro.apxsoftware.demodoc.controllers;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.apxsoftware.demodoc.entities.ConfirmationToken;
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.entities.User;
import ro.apxsoftware.demodoc.entities.UserRole;
import ro.apxsoftware.demodoc.service.ConfirmationTokenService;
import ro.apxsoftware.demodoc.service.EmailService;
import ro.apxsoftware.demodoc.service.PersonService;
import ro.apxsoftware.demodoc.service.RoleService;
import ro.apxsoftware.demodoc.service.UserService;


@Controller
@SessionAttributes({ "person", "userAccount", "picList", "lastPicList" })
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	EntityManager entityManager;

	@Autowired
	PersonService persServ;

	@Autowired
	UserService userServ;

	@Autowired
	RoleService roleServ;
	
	@Autowired
	BCryptPasswordEncoder bCryptEncoder;
	
	@Autowired
	ConfirmationTokenService confTokenServ;
	
	@Autowired
	EmailService emailService;
	
	
	@Autowired
	AuthenticationManager authManager;
	

	@GetMapping(value={"","/"})
	public String displayRegisterForm(Model model, HttpSession session, Authentication auth) {
		
		
		if(auth != null) {
			System.out.println("User is " + auth.getName());
			return "redirect: ";
		}
		
		if (!model.containsAttribute("person")) {
			Person person = new Person();
			model.addAttribute("person", person);
			session.setAttribute("person", person);
		}

		if (!model.containsAttribute("userAccount")) {
			User userAccount = new User();
			model.addAttribute("userAccount", userAccount);
		}

		System.out.println("Getting new user and person entity ready");

		return "usermodals :: #registerNewUserForm";
	}

	@PostMapping("/save")
	public String createAndSaveUserAndPerson(Model model, User userAccount, Person person,
			RedirectAttributes redirAttr, HttpSession session,SessionStatus status) {
		
			System.out.println("person is " + person.getFirstName() + "--> person's ID is " + person.getPersonId());
								
			int countSaves= 0;
				if (person.getFirstName() != null) {

					person.setAppStatus(1);

					if (persServ.checkBirthDate(person)) {
						
						System.out.println("Persons are : " + persServ.getAll().toString());
//						Person checkingPerson = persServ.findPersonById(person.getPersonId()); // this is returning a null object

						
						
						//used to avoid detach exception 
//						if(!persServ.getAll().contains(persServ.findPersonById(person.getPersonId()))) {
						
						if(persServ.findPersonById(person.getPersonId()) == null) {								
							UserRole role1 = new UserRole("USER");
							UserRole role2 = new UserRole("PACIENT");
							roleServ.saveRole(role1);
							roleServ.saveRole(role2);
							userAccount.addRole(role1);
							userAccount.addRole(role2);
							
							userAccount.setPerson(person);
							userAccount.setUserName(userAccount.getEmail());
							
							//encrypt the password for the user
							persServ.save(person);
							userAccount.setPassword(bCryptEncoder.encode(userAccount.getPassword()));							
							userServ.save(userAccount);	
							countSaves++;
							ConfirmationToken confirmationToken = new ConfirmationToken(userAccount);
							System.out.println("confirmationToken is " + confirmationToken.getConfirmationToken());
							confTokenServ.saveToken(confirmationToken);
				
							MimeMessage confirmMailMime = emailService.confirmAccountMimeEmail(userAccount.getEmail(), 
									"Dental132 - Your account is waiting for confirmation", confirmationToken.getConfirmationToken());
							emailService.sendMimeEmail(confirmMailMime);

//							SimpleMailMessage mailMessage = emailService.simpleConfirmationMessage(userAccount.getEmail(), 
//									"BZBees - please confirm your account, jobs are ready", confirmationToken.getConfirmationToken());
//				emailService.sendEmail(mailMessage);
				System.out.println("Email was sent according to spring boot");
				
				User checkUser = entityManager.find(User.class, 1L);
//				System.out.println("What usery is this? : " + checkUser.getEmail());
				
				redirAttr.addAttribute("createdUser", "Thank you for signing up. The user account " + "'" + userAccount.getUsername() +"'" + " is created.");
				redirAttr.addAttribute("activateUser", "We sent an email to " + userAccount.getEmail() + ". Please check your email to activate your account");

						}
							
											
					} else {
						System.out.println("Invalid dates baby");
						
						return "user/login";
					}
		
		}
		
		int usersNo = userServ.getAll().size();
		System.out.println("Number of users " + " " + "is "+ usersNo);

		System.out.println("Hitting the save from user/save POST ");
		
		session.setAttribute("person", person);
		session.setAttribute("userAccount", userAccount);
		
		redirAttr.addFlashAttribute("person", person);
		redirAttr.addFlashAttribute("userAccount", userAccount);
		
//		status.setComplete();
		
		System.out.println(countSaves);

		return "redirect:/";

	}
	
	@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST}) 
	public String confirmAccount (Model model, @RequestParam("t0") String confirmationToken, User userAccount, Person person,
			HttpServletRequest request, RedirectAttributes redirAttr, HttpSession session	) {
		
		ConfirmationToken checkedToken = confTokenServ.findConfirmationTokenByConfirmationToken(confirmationToken);
		if(checkedToken !=null) {
			
			userAccount = checkedToken.getUser();
			person = userAccount.getPerson();
					
			System.out.println("Who is this person ? " + person.getLastName());
			System.out.println("Who is this user? " + userAccount.getUsername());
			System.out.println("authorities are : " + userAccount.getAuthorities().toString());
			System.out.println("What password is passed here via user.getpassword " + userAccount.getPassword());
			
			userAccount.setActive(true);
			userAccount.setPerson(person);
			persServ.save(person);
			userServ.save(userAccount);
			
			//used to auto login the user coming from email			
			session = request.getSession();	
			Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount,null, userAccount.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			
			System.out.println("This is user activation is " + userAccount.isActive());
			redirAttr.addAttribute("message", "The account for" + userAccount.getUsername() + " is confirmed");
			model.addAttribute("userAccount", userAccount);
			session.setAttribute("userAccount", userAccount);
			
//			//get the agency if any is associated with current user 
//			if(agencyServ.findAgencyByUserId(userAccount.getUserId()) !=null) {
//				Agency agency = agencyServ.findAgencyByUserId(userAccount.getUserId());
//				System.out.println("Agency in user controller " + agency.getAgencyName());
//				model.addAttribute("agency", agency);
//			} else {
//				Agency agency = new Agency();
//				model.addAttribute("agency", agency);
//				System.out.println("THERE IS NO AGENCY in USER CONTROLLER EMAIL USER ACTIVATION");
//			}

			
		}
		
		

	
	return "index";
	}	
	
	
	
	@PostMapping("/finishAccount")
	public String displayHomePage(SessionStatus status, Model model, HttpServletRequest request,
			User userAccount) {
		
		model.addAttribute("message", "Account is set up, please login or confirm you account.");

		status.setComplete();
		return "redirect:/";
	}

}
