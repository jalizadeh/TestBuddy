package com.jalizadeh.TestBuddy.interfaces;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.jalizadeh.TestBuddy.exception.RestTemplateResponseErrorHandler;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanResponse;

public abstract class RequestPostmanAbstract extends RequestAbstract{
	
	public abstract PostmanResponse handleRequest(PostmanItem item, int count, String testCase, String paramName, 
			String url, Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException;
	
}
