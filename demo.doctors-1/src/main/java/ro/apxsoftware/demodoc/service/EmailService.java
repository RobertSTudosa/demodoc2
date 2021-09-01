package ro.apxsoftware.demodoc.service;

import java.io.File;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.entities.Appointment;



@Service("emailService")
public class EmailService {
	
	Transport transport;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	AppointmentService appServ;
	
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
		 
		
		 javaMailSender.send(email);
		 
	    }
	 
	 public MimeMessage confirmAppointmentMimeEmail(String to, String subject, String confirmationToken, String companyServices) {
		 
		 Appointment appointment = appServ.getAppointmentByToken(confirmationToken);
		 String appointmentCoServices = appointment.getStringCompanyServicesNames();
		 System.out.println("company Services are these ----------> " + appointmentCoServices);
		 System.out.println("companyServices as parameters --------------> " + companyServices );
		 
		 
		 MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		 try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom("robert@bpeople.ro");
			helper.setTo(to);
			helper.setSubject(subject);
			 helper.setText(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
				+ "<html xmlns:v=\"urn:schemas-microsoft-com:vml\">\r\n"
				+ "\r\n"
				+ "<head>\r\n"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0;\" />\r\n"
				+ "    <!--[if !mso]--><!-- -->\r\n"
				+ "    <link href='https://fonts.googleapis.com/css?family=Work+Sans:300,400,500,600,700' rel=\"stylesheet\">\r\n"
				+ "    <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel=\"stylesheet\">\r\n"
				+ "    <!--<![endif]-->\r\n"
				+ "\r\n"
				+ "    <title>Programarea ta la Dental132</title>\r\n"
				+ "\r\n"
				+ "    <style type=\"text/css\">\r\n"
				+ "        body {\r\n"
				+ "            width: 100%;\r\n"
				+ "            background-color: #ffffff;\r\n"
				+ "            margin: 0;\r\n"
				+ "            padding: 0;\r\n"
				+ "            -webkit-font-smoothing: antialiased;\r\n"
				+ "            mso-margin-top-alt: 0px;\r\n"
				+ "            mso-margin-bottom-alt: 0px;\r\n"
				+ "            mso-padding-alt: 0px 0px 0px 0px;\r\n"
				+ "        }\r\n"
				+ "\r\n"
				+ "        p,\r\n"
				+ "        h1,\r\n"
				+ "        h2,\r\n"
				+ "        h3,\r\n"
				+ "        h4 {\r\n"
				+ "            margin-top: 0;\r\n"
				+ "            margin-bottom: 0;\r\n"
				+ "            padding-top: 0;\r\n"
				+ "            padding-bottom: 0;\r\n"
				+ "        }\r\n"
				+ "\r\n"
				+ "        span.preheader {\r\n"
				+ "            display: none;\r\n"
				+ "            font-size: 1px;\r\n"
				+ "        }\r\n"
				+ "\r\n"
				+ "        html {\r\n"
				+ "            width: 100%;\r\n"
				+ "        }\r\n"
				+ "\r\n"
				+ "        table {\r\n"
				+ "            font-size: 14px;\r\n"
				+ "            border: 0;\r\n"
				+ "        }\r\n"
				+ "        /* ----------- responsivity ----------- */\r\n"
				+ "\r\n"
				+ "        @media only screen and (max-width: 640px) {\r\n"
				+ "            /*------ top header ------ */\r\n"
				+ "            .main-header {\r\n"
				+ "                font-size: 20px !important;\r\n"
				+ "            }\r\n"
				+ "            .main-section-header {\r\n"
				+ "                font-size: 28px !important;\r\n"
				+ "            }\r\n"
				+ "            .show {\r\n"
				+ "                display: block !important;\r\n"
				+ "            }\r\n"
				+ "            .hide {\r\n"
				+ "                display: none !important;\r\n"
				+ "            }\r\n"
				+ "            .align-center {\r\n"
				+ "                text-align: center !important;\r\n"
				+ "            }\r\n"
				+ "            .no-bg {\r\n"
				+ "                background: none !important;\r\n"
				+ "            }\r\n"
				+ "            /*----- main image -------*/\r\n"
				+ "            .main-image img {\r\n"
				+ "                width: 440px !important;\r\n"
				+ "                height: auto !important;\r\n"
				+ "            }\r\n"
				+ "            /* ====== divider ====== */\r\n"
				+ "            .divider img {\r\n"
				+ "                width: 440px !important;\r\n"
				+ "            }\r\n"
				+ "            /*-------- container --------*/\r\n"
				+ "            .container590 {\r\n"
				+ "                width: 440px !important;\r\n"
				+ "            }\r\n"
				+ "            .container580 {\r\n"
				+ "                width: 400px !important;\r\n"
				+ "            }\r\n"
				+ "            .main-button {\r\n"
				+ "                width: 220px !important;\r\n"
				+ "            }\r\n"
				+ "            /*-------- secions ----------*/\r\n"
				+ "            .section-img img {\r\n"
				+ "                width: 320px !important;\r\n"
				+ "                height: auto !important;\r\n"
				+ "            }\r\n"
				+ "            .team-img img {\r\n"
				+ "                width: 100% !important;\r\n"
				+ "                height: auto !important;\r\n"
				+ "            }\r\n"
				+ "        }\r\n"
				+ "\r\n"
				+ "        @media only screen and (max-width: 479px) {\r\n"
				+ "            /*------ top header ------ */\r\n"
				+ "            .main-header {\r\n"
				+ "                font-size: 18px !important;\r\n"
				+ "            }\r\n"
				+ "            .main-section-header {\r\n"
				+ "                font-size: 26px !important;\r\n"
				+ "            }\r\n"
				+ "            /* ====== divider ====== */\r\n"
				+ "            .divider img {\r\n"
				+ "                width: 280px !important;\r\n"
				+ "            }\r\n"
				+ "            /*-------- container --------*/\r\n"
				+ "            .container590 {\r\n"
				+ "                width: 280px !important;\r\n"
				+ "            }\r\n"
				+ "            .container590 {\r\n"
				+ "                width: 280px !important;\r\n"
				+ "            }\r\n"
				+ "            .container580 {\r\n"
				+ "                width: 260px !important;\r\n"
				+ "            }\r\n"
				+ "            /*-------- secions ----------*/\r\n"
				+ "            .section-img img {\r\n"
				+ "                width: 280px !important;\r\n"
				+ "                height: auto !important;\r\n"
				+ "            }\r\n"
				+ "        }\r\n"
				+ "    </style>\r\n"
				+ "    <!--[if gte mso 9]><style type=”text/css”>\r\n"
				+ "        body {\r\n"
				+ "        font-family: arial, sans-serif!important;\r\n"
				+ "        }\r\n"
				+ "        </style>\r\n"
				+ "    <![endif]-->\r\n"
				+ "</head>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "<body class=\"respond\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\r\n"
				+ "    <!-- pre-header -->\r\n"
				+ "    <table style=\"display:none!important;\">\r\n"
				+ "        <tr>\r\n"
				+ "            <td>\r\n"
				+ "                <div style=\"overflow:hidden;display:none;font-size:1px;color:#ffffff;line-height:1px;font-family:Arial;maxheight:0px;max-width:0px;opacity:0;\">\r\n"
				+ "                    Bine ati venit la DENTAL132!\r\n"
				+ "                </div>\r\n"
				+ "            </td>\r\n"
				+ "        </tr>\r\n"
				+ "    </table>\r\n"
				+ "    <!-- pre-header end -->\r\n"
				+ "    <!-- header -->\r\n"
				+ "    <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"ffffff\">\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td align=\"center\">\r\n"
				+ "                <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\">\r\n"
				+ "\r\n"
				+ "                            <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td align=\"center\" height=\"70\" style=\"height:70px;\">\r\n"
				+ "                                        <a href=\"\" style=\"display: block; border-style: none !important; border: 0 !important;\"><img width=\"100\" border=\"0\" style=\"display: block; width: 100px;\" src='cid:topLogo' alt=\"\" /></a>\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td align=\"center\">\r\n"
				+ "                                        <table width=\"360 \" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                            class=\"container590 hide\">\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"120\" align=\"center\" style=\"font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;\">\r\n"
				+ "                                                    <a href=\"https://apxsoftware.ro/demo/#services\" style=\"color: #312c32; text-decoration: none;\">Servicii</a>\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td width=\"120\" align=\"center\" style=\"font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;\">\r\n"
				+ "                                                    <a href=\"https://apxsoftware.ro/demo/#doctors\" style=\"color: #312c32; text-decoration: none;\">Doctori</a>\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td width=\"120\" align=\"center\" style=\"font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;\">\r\n"
				+ "                                                    <a href=\"https://apxsoftware.ro/demo/#pricing\" style=\"color: #312c32; text-decoration: none;\">Preturi</a>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "                            </table>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                </table>\r\n"
				+ "            </td>\r\n"
				+ "        </tr>\r\n"
				+ "    </table>\r\n"
				+ "    <!-- end header -->\r\n"
				+ "\r\n"
				+ "    <!-- big image section -->\r\n"
				+ "\r\n"
				+ "    <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"ffffff\" class=\"bg_color\">\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td align=\"center\">\r\n"
				+ "                <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\" style=\"color: #343434; font-size: 24px; font-family: Quicksand, Calibri, sans-serif; font-weight:700;letter-spacing: 3px; line-height: 35px;\"\r\n"
				+ "                            class=\"main-header\">\r\n"
				+ "                            <!-- section text ======-->\r\n"
				+ "\r\n"
				+ "                            <div style=\"line-height: 35px\">\r\n"
				+ "\r\n"
				+ "                                Programarea dvs la <span style=\"color: #3fbabf;\">DENTAL132</span>\r\n"
				+ "\r\n"
				+ "                            </div>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td height=\"10\" style=\"font-size: 10px; line-height: 10px;\">&nbsp;</td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\">\r\n"
				+ "                            <table border=\"0\" width=\"40\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"eeeeee\">\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td height=\"2\" style=\"font-size: 2px; line-height: 2px;\">&nbsp;</td>\r\n"
				+ "                                </tr>\r\n"
				+ "                            </table>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td height=\"20\" style=\"font-size: 20px; line-height: 20px;\">&nbsp;</td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"left\">\r\n"
				+ "                            <table border=\"0\" width=\"590\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td align=\"left\" style=\"color: #888888; font-size: 16px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;\">\r\n"
				+ "                                        <!-- section text ======-->\r\n"
				+ "\r\n"
				+ "                                        <p style=\"line-height: 24px; margin-bottom:15px;\">\r\n"
				+ "\r\n"
				+ "                                            Buna ziua, "
				+ appointment.getPacientName() 
				+ "\r\n"
				+ "                                        </p>\r\n"
				+ "                                        <p style=\"line-height: 24px;margin-bottom:15px;\">\r\n"
				+ "                                             Programarea dvs. pentru data de \r\n" 
				+ "<strong>" + appointment.getDate() + "</strong>" + ", la ora : " 
				+ "<strong>"		+ appointment.getAppointmentTime() + "</strong>" + "\r\n"
				+ "                                            a fost efectuata cu succes.\r\n"
				+ "                                            Doctorul dvs., " 
				+ appointment.getDoctorName() + " va asteapta pentru procedurile dorite de dvs: \r\n"
				+ "<strong>" + companyServices + "</strong>"   
				+ "                                        </p>\r\n"
				+ "                                        <p style=\"line-height: 24px; margin-bottom:20px;\">\r\n"
				+ "                                            Puteti accesa si edita programarea prin butonul de mai in jos: \r\n"
				+ "                                        </p>\r\n"
				+ "                                        <table border=\"0\" align=\"center\" width=\"180\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"3fbabf\" style=\"margin-bottom:20px;\">\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td height=\"10\" style=\"font-size: 10px; line-height: 10px;\">&nbsp;</td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td align=\"center\" style=\"color: #ffffff; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 22px; letter-spacing: 2px;\">\r\n"
				+ "                                                    <!-- main section button -->\r\n"
				+ "\r\n"
				+ "                                                    <div style=\"line-height: 22px;\">\r\n"
				+ "                                                        <a href=\"http:/localhost:8080/pacientAppointment?at=" + confirmationToken + "\" + "
				+ "style=\"color: #ffffff; text-decoration: none;\">Programarea dvs.</a>\r\n"
				+ "                                                    </div>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td height=\"10\" style=\"font-size: 10px; line-height: 10px;\">&nbsp;</td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                        </table>\r\n"
				+ "                                        <p style=\"line-height: 24px\">\r\n"
				+ "                                            Cu drag,</br>\r\n"
				+ "                                            Echipa Dental132\r\n"
				+ "                                        </p>\r\n"
				+ "\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "                            </table>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "                </table>\r\n"
				+ "\r\n"
				+ "            </td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td height=\"40\" style=\"font-size: 40px; line-height: 40px;\">&nbsp;</td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "    </table>\r\n"
				+ "\r\n"
				+ "    <!-- end section -->\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "    <!-- main section -->\r\n"
				+ "    <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"2a2e36\">\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td align=\"center\" style=\"background-image: url(cid:smileBanner); background-size: cover; background-position: top center; background-repeat: no-repeat;\"\r\n"
				+ "                background=\"cid:smileBanner\">\r\n"
				+ "\r\n"
				+ "                <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td height=\"50\" style=\"font-size: 50px; line-height: 50px;\">&nbsp;</td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\">\r\n"
				+ "                            <table border=\"0\" width=\"380\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                class=\"container590\">\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td align=\"center\">\r\n"
				+ "                                        <table border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"container580\">\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td align=\"center\" style=\"color: #cccccc; font-size: 16px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 26px;\">\r\n"
				+ "                                                    <!-- section text ======-->\r\n"
				+ "\r\n"
				+ "                                                    <div style=\"line-height: 26px\">\r\n"
				+ "\r\n"
				+ "                                                        Implantul Bredent este disponibil acum. Pentru zambetul care conteaza!\r\n"
				+ "\r\n"
				+ "                                                    </div>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "\r\n"
				+ "                            </table>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\">\r\n"
				+ "                            <table border=\"0\" align=\"center\" width=\"250\" cellpadding=\"0\" cellspacing=\"0\" style=\"border:2px solid #ffffff;\">\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td height=\"10\" style=\"font-size: 10px; line-height: 10px;\">&nbsp;</td>\r\n"
				+ "                                </tr>\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td align=\"center\" style=\"color: #ffffff; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 22px; letter-spacing: 2px;\">\r\n"
				+ "                                        <!-- main section button -->\r\n"
				+ "\r\n"
				+ "                                        <div style=\"line-height: 22px;\">\r\n"
				+ "                                            <a href=\"https://apxsoftware.ro/demo\" style=\"color: #fff; text-decoration: none;\">DETALII</a>\r\n"
				+ "                                        </div>\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td height=\"10\" style=\"font-size: 10px; line-height: 10px;\">&nbsp;</td>\r\n"
				+ "                                </tr>\r\n"
				+ "\r\n"
				+ "                            </table>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td height=\"50\" style=\"font-size: 50px; line-height: 50px;\">&nbsp;</td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                </table>\r\n"
				+ "            </td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "    </table>\r\n"
				+ "\r\n"
				+ "    <!-- end section -->\r\n"
				+ "\r\n"
				+ "    <!-- contact section -->\r\n"
				+ "    <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"ffffff\" class=\"bg_color\">\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td height=\"60\" style=\"font-size: 60px; line-height: 60px;\">&nbsp;</td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td align=\"center\">\r\n"
				+ "                <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590 bg_color\">\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\">\r\n"
				+ "                            <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590 bg_color\">\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table border=\"0\" width=\"300\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                            class=\"container590\">\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <!-- logo -->\r\n"
				+ "                                                <td align=\"left\">\r\n"
				+ "                                                    <a href=\"https://apxsoftware.ro\" style=\"display: block; border-style: none !important; border: 0 !important;\"><img width=\"80\" border=\"0\" style=\"display: block; width: 80px;\" src='cid:bottomLogo' alt=\"\" /></a>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td align=\"left\" style=\"color: #888888; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 23px;\"\r\n"
				+ "                                                    class=\"text_color\">\r\n"
				+ "                                                    <div style=\"color: #333333; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; font-weight: 600; mso-line-height-rule: exactly; line-height: 23px;\">\r\n"
				+ "\r\n"
				+ "                                                        Email: <br/> <a href=\"mailto:\" style=\"color: #888888; font-size: 14px; font-family: 'Hind Siliguri', Calibri, Sans-serif; font-weight: 400;\">contact@dental132.ro</a>\r\n"
				+ "\r\n"
				+ "                                                    </div>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                        </table>\r\n"
				+ "\r\n"
				+ "                                        <table border=\"0\" width=\"2\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                            class=\"container590\">\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"2\" height=\"10\" style=\"font-size: 10px; line-height: 10px;\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </table>\r\n"
				+ "\r\n"
				+ "                                        <table border=\"0\" width=\"200\" align=\"right\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                            class=\"container590\">\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td class=\"hide\" height=\"45\" style=\"font-size: 45px; line-height: 45px;\">&nbsp;</td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td height=\"15\" style=\"font-size: 15px; line-height: 15px;\">&nbsp;</td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td>\r\n"
				+ "                                                    <table border=\"0\" align=\"right\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "                                                        <tr>\r\n"
				+ "                                                            <td>\r\n"
				+ "                                                                <a href=\"https://www.facebook.com/mdbootstrap\" style=\"display: block; border-style: none !important; border: 0 !important;\"><img width=\"24\" border=\"0\" style=\"display: block;\" src=\"http://i.imgur.com/Qc3zTxn.png\" alt=\"\"></a>\r\n"
				+ "                                                            </td>\r\n"
				+ "                                                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n"
				+ "                                                            <td>\r\n"
				+ "                                                                <a href=\"https://twitter.com/MDBootstrap\" style=\"display: block; border-style: none !important; border: 0 !important;\"><img width=\"24\" border=\"0\" style=\"display: block;\" src=\"http://i.imgur.com/RBRORq1.png\" alt=\"\"></a>\r\n"
				+ "                                                            </td>\r\n"
				+ "                                                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n"
				+ "                                                            <td>\r\n"
				+ "                                                                <a href=\"https://plus.google.com/u/0/b/107863090883699620484/107863090883699620484/posts\" style=\"display: block; border-style: none !important; border: 0 !important;\"><img width=\"24\" border=\"0\" style=\"display: block;\" src=\"http://i.imgur.com/Wji3af6.png\" alt=\"\"></a>\r\n"
				+ "                                                            </td>\r\n"
				+ "                                                        </tr>\r\n"
				+ "                                                    </table>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "                            </table>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "                </table>\r\n"
				+ "            </td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td height=\"60\" style=\"font-size: 60px; line-height: 60px;\">&nbsp;</td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "    </table>\r\n"
				+ "    <!-- end section -->\r\n"
				+ "\r\n"
				+ "    <!-- footer ====== -->\r\n"
				+ "    <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"f4f4f4\">\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td align=\"center\">\r\n"
				+ "\r\n"
				+ "                <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\r\n"
				+ "\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td>\r\n"
				+ "                            <table border=\"0\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                class=\"container590\">\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td align=\"left\" style=\"color: #aaaaaa; font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;\">\r\n"
				+ "                                        <div style=\"line-height: 24px;\">\r\n"
				+ "\r\n"
				+ "                                            <span style=\"color: #333333;\">Customization from apxsoftware.ro</span>\r\n"
				+ "\r\n"
				+ "                                        </div>\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "                            </table>\r\n"
				+ "\r\n"
				+ "                            <table border=\"0\" align=\"left\" width=\"5\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                class=\"container590\">\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td height=\"20\" width=\"5\" style=\"font-size: 20px; line-height: 20px;\">&nbsp;</td>\r\n"
				+ "                                </tr>\r\n"
				+ "                            </table>\r\n"
				+ "\r\n"
				+ "                            <table border=\"0\" align=\"right\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\r\n"
				+ "                                class=\"container590\">\r\n"
				+ "\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td align=\"center\">\r\n"
				+ "                                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td align=\"center\">\r\n"
				+ "                                                    <a style=\"font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;color: #5caad2; text-decoration: none;font-weight:bold;\"\r\n"
				+ "                                                        href=\"{{UnsubscribeURL}}\">UNSUBSCRIBE</a>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                </tr>\r\n"
				+ "\r\n"
				+ "                            </table>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "\r\n"
				+ "                </table>\r\n"
				+ "            </td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "        <tr>\r\n"
				+ "            <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\r\n"
				+ "        </tr>\r\n"
				+ "\r\n"
				+ "    </table>\r\n"
				+ "    <!-- end footer ====== -->\r\n"
				+ "\r\n"
				+ "</body>\r\n"
				+ "\r\n"
				+ "</html>"	, true); 
					 

			 helper.addInline("topLogo",
					 new ClassPathResource("static/email/logo.png"));
			 
			 helper.addInline("bottomLogo",
					 new ClassPathResource("static/email/logo.png"));
			 
			 helper.addInline("smileBanner",
					 new ClassPathResource("static/email/slide_email2.jpg"));
		 
		        
			
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 
		 return mimeMessage;
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
