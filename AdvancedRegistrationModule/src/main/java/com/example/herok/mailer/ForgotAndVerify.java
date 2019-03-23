package com.example.herok.mailer;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.herok.enities.User;
import com.example.herok.repositories.UserRepo;

@Component
public class ForgotAndVerify {
	
	private final Logger logs = LoggerFactory.getLogger(ForgotAndVerify.class);
	
	@Autowired
	ServiceAct  sact;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	UserRepo userRepo;
	
	public void mailForgot(String email) throws AddressException, MessagingException, IOException{
		Optional<User> userCheck= userRepo.findByEmail(email);
		
		if(userCheck.isPresent()) {
			User user = userCheck.get();
			user.setResetToken(UUID.randomUUID().toString());
			sact.sendMail(email,user.getResetToken(),"forgot");
			userRepo.save(user);
		}
		else {
			logs.info("No Such User Exists");
		}
		
		
	}
	
	public void mailVerify(String email,String username,String password) throws AddressException, MessagingException, IOException{
		Optional<User> userCheck= userRepo.findByEmail(email);
		
		if(userCheck.isPresent()) {
			User user =userCheck.get();
			if(!user.isEnabled()) {
				user.setResetToken(UUID.randomUUID().toString());
				sact.sendMail(user.getEmail(), user.getResetToken(), "verify");
				userRepo.save(user);
			}
		}
		else {
			User user=new User();
			user.setEmail(email);
			user.setUsername(username);
			user.setPassword(encoder.encode(password));
			user.setResetToken(UUID.randomUUID().toString());
			sact.sendMail(email,user.getResetToken(),"verify");
			userRepo.save(user);		}
		
		
	}
}
