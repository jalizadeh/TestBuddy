package com.jalizadeh.TestBuddy.requestImpl;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.TestBuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.TestBuddy.model.PostmanBody;
import com.jalizadeh.TestBuddy.model.PostmanItem;
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
		
		PostmanResponse generatedResponse = 
				initialResponse(item, count, testCase, paramName, dataMap, headers, response);
		
		PostmanBody newBody = (PostmanBody) item.request.body.clone();
		newBody.raw = mapToString(dataMap);
		generatedResponse.originalRequest.body = newBody;

		return generatedResponse;
	}
	
}
