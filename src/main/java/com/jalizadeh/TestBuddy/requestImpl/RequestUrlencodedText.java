package com.jalizadeh.TestBuddy.requestImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.jalizadeh.TestBuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.TestBuddy.model.PostmanBody;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanRequest;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.model.PostmanUrlEncoded;
import com.jalizadeh.TestBuddy.types.RequestBodyType;

public class RequestUrlencodedText extends RequestPostmanAbstract{

	@Override
	public String bodyType() {
		return RequestBodyType.URLENCODEC_TEXT.type();
	}
	
	@Override
	public PostmanResponse handleRequest(PostmanItem item, int count, String testCase, String paramName, String url,
			Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException {
		
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		//extract data from "urlencoded" field of the Postman collection
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		dataMap.entrySet().stream().forEach(e -> map.add(e.getKey(), e.getValue()));
		

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		
		
		/*
		String concatData = dataMap.entrySet().stream()
				.map(e -> e.getKey() + "=" + e.getValue())
				.collect(Collectors.joining("&"));
		System.out.println(concatData);

		HttpEntity<String> entity = new HttpEntity<String>(concatData, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		 */

		PostmanResponse postmanResponse = new PostmanResponse();

		String title = "<br><h3>Case: " + testCase + "</h3>";
		String body = response.getBody();
		String resp = response.toString();

		System.err.println(title);
		System.out.println(body + "\n" + resp);

		postmanResponse.name = response.getStatusCodeValue() + " - " + testCase + " " + paramName;
		postmanResponse.status = response.getStatusCode().name();
		postmanResponse.code = response.getStatusCodeValue();
		postmanResponse.body = response.getBody();
		postmanResponse.header = extractResponseHeader(response);
		
		//TODO: based on response's Content-Type
		postmanResponse._postman_previewlanguage = "json";
		

		/**
		 * As I need change the data for each body, I need to clone the original object
		 * otherwise, every time I modify the original reference
		 */
		PostmanRequest newReq = (PostmanRequest) item.request.clone();
		PostmanBody newBody = (PostmanBody) item.request.body.clone();
		
		List<PostmanUrlEncoded> urlencodedList = new ArrayList<PostmanUrlEncoded>();
		dataMap.entrySet().stream()
			.forEach(e -> {
				PostmanUrlEncoded u = new PostmanUrlEncoded();
				u.key = e.getKey();
				u.value = e.getValue();
				u.type = "text";
				urlencodedList.add(u);
			});
		
		newBody.urlencoded = urlencodedList;
		newReq.body = newBody;
		postmanResponse.originalRequest = newReq;

		return postmanResponse;
	}

	

}
