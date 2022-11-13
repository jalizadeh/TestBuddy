package com.jalizadeh.TestBuddy.requestImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

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
	public PostmanResponse handleRequest(PostmanItem item, int count, String testCase, String paramName,
			Map<String, String> dataMap, ResponseEntity<String> response) throws CloneNotSupportedException {
		
		PostmanResponse postmanResponse = new PostmanResponse();

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
