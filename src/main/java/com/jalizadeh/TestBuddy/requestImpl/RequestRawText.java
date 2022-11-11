package com.jalizadeh.TestBuddy.requestImpl;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.TestBuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.TestBuddy.model.PostmanBody;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanRequest;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.types.RequestBodyType;

public class RequestRawText extends RequestPostmanAbstract {
	
	@Override
	public String bodyType() {
		return RequestBodyType.RAW_TEXT.type();
	}
	
	
	@Override
	public PostmanResponse handleRequest(PostmanItem item, int count, String testCase, String paramName, String url,
			Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException {

		/*
		// start delay
		try {
			TimeUnit.SECONDS.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/

		String concatData = dataMap.entrySet().stream()
				.map(e -> e.getKey() + "=" + e.getValue())
				.collect(Collectors.joining("&"));
		System.out.println(concatData);

		HttpEntity<String> entity = new HttpEntity<String>(concatData, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

		PostmanResponse postmanResponse = new PostmanResponse();

		String title = "<br><h3>Case: " + testCase + "</h3>";
		String body = response.getBody();
		String resp = response.toString();

		System.err.println(title);
		System.out.println(body + "\n" + resp);
		
		postmanResponse.name = response.getStatusCodeValue() + " - " + testCase + " " + paramName;
		postmanResponse.status = response.getStatusCode().name();
		postmanResponse.code = response.getStatusCodeValue();
		postmanResponse.header = extractResponseHeader(response);
		postmanResponse._postman_previewlanguage = "json";
		postmanResponse.body = response.getBody();

		/**
		 * As I need change the data for each body, I need to clone the original object
		 * otherwise, every time I modify the original reference
		 */
		PostmanRequest newReq = (PostmanRequest) item.request.clone();
		PostmanBody newBody = (PostmanBody) item.request.body.clone();
		newBody.raw = concatData;
		newReq.body = newBody;
		postmanResponse.originalRequest = newReq;

		return postmanResponse;
	}

	
}
