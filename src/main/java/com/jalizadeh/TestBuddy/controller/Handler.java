package com.jalizadeh.TestBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.runner.PostmanCollectionRunner;
import com.jalizadeh.TestBuddy.runner.PostmanRunResult;
import com.jalizadeh.TestBuddy.service.RestService;

@RestController
public class Handler {

	@Autowired
	private RestService restService;
	
	
	public Handler() {
		this.restService = new RestService(new RestTemplateBuilder());
	}
	
	
	@GetMapping("/curl")
	public StringBuffer parseCurl() {
		return restService.sendRequest();
	}
	
	
	@GetMapping("/json")
	public List<PostmanResponse> parseJson() {  
	    String jsonPath = "C:/sample.json";
	    
	    PostmanCollection collection = new PostmanCollection();
	    
	    try {
	    	collection = new PostmanCollectionRunner().parseCollection(jsonPath, null, "Test", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		return restService.parseCollection(collection);
	}
}
