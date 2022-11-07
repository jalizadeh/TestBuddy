package com.jalizadeh.TestBuddy.controller;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalizadeh.TestBuddy.central.FilterFactory;
import com.jalizadeh.TestBuddy.central.FiltersManager;
import com.jalizadeh.TestBuddy.filter.Filters;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.runner.PostmanCollectionRunner;
import com.jalizadeh.TestBuddy.service.RestService;
import com.jalizadeh.TestBuddy.types.InputRequest;

@RestController
public class Handler {

	@Autowired
	private RestService restService;
	
	@Autowired
	private FilterFactory filterFactory;

	@PostMapping("/json")
	public PostmanCollection parseJson(
			@RequestParam(required = false) Optional<Integer> delay, 
			@RequestBody InputRequest input
			) throws Exception {
		
		if(!validateInput(input))
			throw new Exception("Reuest body is not valid");
		
		extractFilters(input);
		
		String jsonPath = "C:/sample.json";

		PostmanCollection collection = new PostmanCollection();
		ObjectMapper mapper = new ObjectMapper();
		PostmanCollection parsedCollection = null;
		
		try {
			collection = new PostmanCollectionRunner().parseCollection(jsonPath, null, "Test", false, false);
			parsedCollection = restService.parseCollection(collection, delay);
			mapper.writeValue(new File("C:/Users/Windows/Desktop/result-full.json"), parsedCollection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parsedCollection;
	}


	private void extractFilters(InputRequest input) {
		FiltersManager instance = FiltersManager.getInstance();
		
		for(Filters f : input.getFilters()) {
			try {
				instance.addFilter(filterFactory.create(f));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private boolean validateInput(InputRequest input) {
		if(input == null || input.getFilters().size() == 0)
			return false;

		return true;
	}
	

}
