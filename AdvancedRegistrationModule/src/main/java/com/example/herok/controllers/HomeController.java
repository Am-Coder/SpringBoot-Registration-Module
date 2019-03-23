package com.example.herok.controllers;
			
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.herok.AjaxMsg.AjaxMessage;
import com.example.herok.enities.Email;
import com.example.herok.enities.ResetParam;
import com.example.herok.mailer.ForgotAndVerify;
import com.example.herok.mailer.Reset;
import com.example.herok.mailer.Verify;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
			
@Controller	
public class HomeController {
	
	private final Logger logs = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired 
	AjaxMessage msg;
	
	@Autowired
	private Verify verify;
	
	@Autowired
	private ForgotAndVerify forgot;
	
	@Autowired
	private  Reset reset;
	
	@GetMapping(value="/")
	String home() {
		return "SignIn";
	}			
	
	@GetMapping(value="/SignUp")
	String signup() {
		return "SignUp";
	}
	
	@GetMapping(value="/SignIn")
	String signin() {
		return "SignIn";
	}
	
	@GetMapping(value="/forgot")
	String forgot() {
		return "ForgotMail";
	}
	
	@GetMapping(value="/success")
	String entry() {
		return "Verify";
	}
	
	@GetMapping(value="/verify")
	String verifyMail(@RequestParam("token") String token) {
		if(verify.validateToken(token)) {
			return "Verify";
		}else {
			return "Error";
		}
	}
	
	@PostMapping(value="/forgot")
	@ResponseBody
	public String forgotPassword(@RequestBody String JSONemail) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		//Convert JSON to POJO
		Email email = mapper.readValue(JSONemail, Email.class);
		try{
			forgot.mailForgot(email.getEmail());
			return "Mail Sent.Check your Inbox.";
		}catch(Exception e) {
			return "Mail Could Not be Send, Retry";
		}
		
	}
	
	@PostMapping(value="/registerUser")
	public String verify(Model mv,@RequestParam("email") String email,@RequestParam("username") String username,@RequestParam("password") String password) {
		try{
			forgot.mailVerify(email, username, password);
			mv.addAttribute("email", email);
			mv.addAttribute("username", username);
			mv.addAttribute("password",password);
			return "VerifyMail";
		}catch(Exception e) {
			return "Error";
		}
		
	}
	
	@GetMapping(value="/reset")
	public ModelAndView reset(@RequestParam("token") String token,ModelAndView mv) {
			if(reset.validateToken(token)) {
				mv.addObject("token", token);
				mv.setViewName("Reset");
				return mv;
			}else {
				mv.setViewName("Error");
				return mv;
			}
	}
	
	@PostMapping(value="/reset")
	@ResponseBody
	public String resetPassord(@RequestBody ResetParam requestParams) {
		if(reset.changePassword(requestParams.getToken(), requestParams.getPassword())) {
			return "Password Reset Complete.";
		}else {
			return "Link Expired";
		}	
		}
	
	
}	
	