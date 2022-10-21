package com.jalizadeh.TestBuddy.interfaces;

import java.util.Map;
import org.springframework.http.HttpHeaders;

import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanResponse;

public interface iRequest {

	PostmanResponse handleRequest(PostmanItem item, int count, String testCase, String paramName, 
			String url, Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException;
	
}
