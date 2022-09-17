package com.jalizadeh.TestBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalizadeh.TestBuddy.service.RestService;

@RestController
public class Handler {

	@Autowired
	private RestService restService;
	
	@GetMapping("/parse")
	public StringBuffer parse() {
		restService = new RestService(new RestTemplateBuilder());
		return restService.sendRequest();
	}
	
	
}
