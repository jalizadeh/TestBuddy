package com.jalizadeh.testbuddy.interfaces;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.jalizadeh.testbuddy.exception.RestTemplateResponseErrorHandler;

public abstract class RequestAbstract {
	
	@Autowired
	protected final RestTemplate restTemplate;
	
	@Autowired
	protected RestTemplateBuilder restTemplateBuilder;
	
	
	public RequestAbstract() {
		this.restTemplateBuilder = new RestTemplateBuilder();
		this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

	protected String mapToString(Map<String,String> map) {
		return map.entrySet().stream()
				.map(i -> i.getKey() + "=" + i.getValue())
				.collect(Collectors.joining("&"));
	}
	
	protected HttpHeaders mapToHttpHeader(Map<String, String> map) {
		HttpHeaders httpHeaders = new HttpHeaders();
		for (Entry<String, String> e : map.entrySet()) {
			httpHeaders.add(e.getKey(), e.getValue());
		}
		return httpHeaders;
	}
}
