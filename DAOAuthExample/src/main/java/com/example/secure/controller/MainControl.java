package com.example.secure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

	
@Controller	
public class MainControl {
	
	@RequestMapping(value="/")
	public String disp() {
		return "index";
	}
	
	@RequestMapping(value= "/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value= "/logmeout")
	public String logout() {
		return "main";
	}
	
	@RequestMapping(value="/success")
	public String login_success() {
		return "success";
	}
}
