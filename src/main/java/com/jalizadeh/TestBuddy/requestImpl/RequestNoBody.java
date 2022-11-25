package com.jalizadeh.TestBuddy.requestImpl;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.TestBuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.types.RequestBodyType;

public class RequestNoBody extends RequestPostmanAbstract {
	
	@Override
	public String bodyType() {
		return RequestBodyType.NO_BODY.type();
	}
	
	
	@Override
	public PostmanResponse handleRequest(PostmanItem item, int count, String testCase, String paramName, 
			Map<String, String> dataMap, HttpHeaders headers, ResponseEntity<String> response) throws CloneNotSupportedException {

		return initialResponse(item, count, testCase, paramName, dataMap, headers, response);
	}
	
}