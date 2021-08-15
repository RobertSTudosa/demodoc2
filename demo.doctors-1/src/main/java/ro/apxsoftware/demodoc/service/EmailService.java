package ro.apxsoftware.demodoc.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service("emailService")
public class EmailService {
	
	Transport transport;
	
	@Autowired
	private JavaMailSender javaMailSender;
	

	
	public String localhost1 = "hrm.bpeople.ro:8080";
	
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender= javaMailSender;
	}
	
	@Async
	public void sendSMTPEmail(Message message, String username, String password) throws MessagingException {
		 
		Transport.send(null, localhost1, localhost1);
	}
	
	 @Async
	 public void sendEmail(SimpleMailMessage email) {
		
		 javaMailSender.send(email);
	    }
	 
	 @Async
	 public void sendMimeEmail(MimeMessage email) {
		 
		 MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    }
	 
	 
	 public SimpleMailMessage simpleConfirmationMessage (String to, String subject, String confirmationToken ) {
		 
		 
		 SimpleMailMessage mailMessage = new SimpleMailMessage();
		 mailMessage.setTo(to);
		 mailMessage.setSubject(subject);
		 mailMessage.setFrom("robert@bpeople.ro");
		 //1st. try was http://hrm.bpeople.ro
		 //2nd try is going to be: http://hrm.bpeople.ro:8080 - aaaannd is right... we have a winner ladies and gentleman
		 mailMessage.setText("To confirm your account, please click here: " + 
				 			"http:/localhost:8080/user/confirm-account?t0=" + 
				 			confirmationToken);
		 return mailMessage;
		 
	 }
	 
	 public SimpleMailMessage simpleChangePassMessage (String to, String subject, String confirmationToken ) {
		 
		 
		 SimpleMailMessage mailMessage = new SimpleMailMessage();
		 mailMessage.setTo(to);
		 mailMessage.setSubject(subject);
		 mailMessage.setFrom("robert@bpeople.ro");
		 mailMessage.setText("To change your password, please click here: " + 
				 			"http:/localhost:8080/login/emailChangePassword?c4=" + 
				 			confirmationToken);
		 return mailMessage;
		 
	 }
	 
	 public SimpleMailMessage simpleAppointmentConfirmation (String to, String subject, String appointmentToken) {
		 SimpleMailMessage mailMessage = new SimpleMailMessage();
		 mailMessage.setTo(to);
		 mailMessage.setSubject(subject);
		 mailMessage.setFrom("robert@bpeople.ro");
		 mailMessage.setText("To see your appointment, please click here: " + 
				 			"http:/localhost:8080/pacientAppointment?at=" + 
				 			appointmentToken);
		 return mailMessage;
	 }
	 
	
	
}
