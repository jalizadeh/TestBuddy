package com.jalizadeh.TestBuddy.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.TestBuddy.model.PostmanHeader;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanResponse;

public abstract class RequestPostmanAbstract extends RequestAbstract{
	
	public abstract String bodyType();
	
	public abstract PostmanResponse handleRequest(PostmanItem item, int count, String testCase, 
			String paramName, Map<String, String> dataMap, HttpHeaders headers,
			ResponseEntity<String> response) throws CloneNotSupportedException;
	
	
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
	
	protected List<PostmanHeader> setModifiedResponseHeader(HttpHeaders headers) {
		if(headers != null) {
			List<PostmanHeader> headerList = new ArrayList<>();
			PostmanHeader ph = new PostmanHeader();
			headers.forEach((k, v) -> {
				ph.key = k;
				ph.value = v.get(0);
				headerList.add(ph);
			});
			
			return headerList;
		}
		
		return null;
	}
}
