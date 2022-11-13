package com.jalizadeh.TestBuddy.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jalizadeh.TestBuddy.exception.RestTemplateResponseErrorHandler;
import com.jalizadeh.TestBuddy.model.PostmanHeader;

public abstract class RequestAbstract {
	
	@Autowired
	protected final RestTemplate restTemplate;
	
	@Autowired
	protected RestTemplateBuilder restTemplateBuilder;
	
	
	public RequestAbstract() {
		this.restTemplateBuilder = new RestTemplateBuilder();
		this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

	protected List<PostmanHeader> extractResponseHeader(ResponseEntity<String> response) {
		List<PostmanHeader> responseHeaderList = new ArrayList<PostmanHeader>();
		for(Entry<String, String> e : response.getHeaders().toSingleValueMap().entrySet()) {
			PostmanHeader ph = new PostmanHeader();
			ph.key = e.getKey();
			ph.value = e.getValue();
			responseHeaderList.add(ph);
		}
		
		return responseHeaderList;
	}
	
	
	protected String mapToString(Map<String,String> dataMap) {
		return dataMap.entrySet().stream()
				.map(i -> i.getKey() + "=" + i.getValue())
				.collect(Collectors.joining("&"));
	}
}
