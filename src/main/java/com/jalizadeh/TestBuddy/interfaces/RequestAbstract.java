package com.jalizadeh.TestBuddy.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.jalizadeh.TestBuddy.exception.RestTemplateResponseErrorHandler;

public abstract class RequestAbstract {
	
	@Autowired
	protected final RestTemplate restTemplate;
	
	@Autowired
	protected RestTemplateBuilder restTemplateBuilder;
	
	
	public RequestAbstract() {
		this.restTemplateBuilder = new RestTemplateBuilder();
		this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

}
