package com.example.herok.mailer;
		
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.herok.enities.User;
import com.example.herok.repositories.UserRepo;

@Component
public class Verify {
	private final Logger logs = LoggerFactory.getLogger(Reset.class);
		
		
	@Autowired
	UserRepo userRepo;
		

		
	public Boolean validateToken(String token) {
		Optional<User> userCheck = userRepo.findByResetToken(token);
		
		if(userCheck.isPresent()) {
			User user=userCheck.get();
			user.setEnabled(true);
			user.setResetToken(null);
			userRepo.save(user);
			
			return true;
			}
		else	{
			
			return false;
		}
	}	
}		
		