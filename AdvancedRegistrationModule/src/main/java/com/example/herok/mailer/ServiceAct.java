package com.example.herok.mailer;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ServiceAct	{
	
	private final Logger logger = LoggerFactory.getLogger(ServiceAct.class);
	
	@Autowired
	JavaMailSender jms;
	
	@Autowired
	HttpServletRequest request;


	@Async
	public void sendMail (String to,String token,String type) throws AddressException, MessagingException, IOException
	{	
		
		
		try {
		String appUrl = request.getScheme() + "://" + request.getServerName();
		
		MimeMessage msg = jms.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		helper.setFrom("ChitChat.co");
		helper.setTo(to);
		if(type.equals("forgot")) {
		helper.setSubject("Forgot Password");
		helper.setText("Follow the link to reset the password:\n" + appUrl + "/reset?token=" + token);
		}
		if(type.equals("verify")) {
			helper.setSubject("Verify Password");
			helper.setText("Follow the link to verify the password:\n" + appUrl + "/verify?token=" + token);			
		}
		logger.info(msg.toString());
		
	    jms.send(msg);
		}
		catch(Exception e) {
			logger.info(e.getMessage());
		}
	}
}
