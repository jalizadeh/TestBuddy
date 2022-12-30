package com.jalizadeh.testbuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	
	@GetMapping("/")
	public String homePage(ModelMap model){
		model.put("PageTitle", "Home");
		return "todo";
	}
	
}
