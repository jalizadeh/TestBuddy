package com.jalizadeh.TestBuddy.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.runner.PostmanCollectionRunner;
import com.jalizadeh.TestBuddy.runner.PostmanRunResult;
import com.jalizadeh.TestBuddy.service.RestService;

@RestController
public class Handler {

	@Autowired
	private RestService restService;

	@GetMapping("/curl")
	public StringBuffer parseCurl() {
		return restService.sendRequest();
	}

	@GetMapping("/json")
	public PostmanCollection parseJson(@RequestParam(required = false) Optional<Integer> delay) throws CloneNotSupportedException {
		String jsonPath = "C:/sample.json";

		PostmanCollection collection = new PostmanCollection();
		ObjectMapper mapper = new ObjectMapper();
		PostmanCollection parsedCollection = null;

		try {
			collection = new PostmanCollectionRunner().parseCollection(jsonPath, null, "Test", false, false);
			parsedCollection = restService.parseCollection(collection, delay);
			mapper.writeValue(new File("C:/Users/Javad Alizadeh/Desktop/result-full.json"), parsedCollection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parsedCollection;
	}

}
