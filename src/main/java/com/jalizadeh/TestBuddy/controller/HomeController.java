package com.jalizadeh.testbuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	
	@GetMapping("/")
	public String homePage(ModelMap model){
		model.put("PageTitle", "Home");
		return "todo";
	}
	
}
