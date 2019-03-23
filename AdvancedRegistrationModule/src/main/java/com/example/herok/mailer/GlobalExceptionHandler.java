package com.example.herok.mailer;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler({AddressException.class, MessagingException.class, IOException.class})
	public String exceptionHandler(Exception e) {
			logger.info("An exception occured"+e.getMessage());
		return "redirect:index.html";
	}
}
