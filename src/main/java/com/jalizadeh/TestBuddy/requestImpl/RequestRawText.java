package com.jalizadeh.TestBuddy.requestImpl;

import java.util.Map;

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
	public PostmanResponse handleRequest(PostmanItem item, int count, String testCase, String paramName, 
			Map<String, String> dataMap, HttpHeaders headers, ResponseEntity<String> response) throws CloneNotSupportedException {

		PostmanResponse postmanResponse = new PostmanResponse();

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
		newBody.raw = mapToString(dataMap);
		newReq.body = newBody;
		postmanResponse.originalRequest = newReq;
		postmanResponse.originalRequest.header = setModifiedResponseHeader(headers);

		return postmanResponse;
	}

	
}
