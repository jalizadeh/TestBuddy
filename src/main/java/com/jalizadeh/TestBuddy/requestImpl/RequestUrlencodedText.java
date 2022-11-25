package com.jalizadeh.TestBuddy.requestImpl;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.TestBuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.TestBuddy.model.PostmanBody;
import com.jalizadeh.TestBuddy.model.PostmanItem;
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
			Map<String, String> dataMap, HttpHeaders headers, ResponseEntity<String> response) throws CloneNotSupportedException {
		
		PostmanResponse generatedResponse = 
				initialResponse(item, count, testCase, paramName, dataMap, headers, response);
		
		
		PostmanBody newBody = (PostmanBody) item.request.body.clone();
		newBody.urlencoded = dataMap.entrySet().stream()
				.map(e -> {
					PostmanUrlEncoded u = new PostmanUrlEncoded();
					u.key = e.getKey();
					u.value = e.getValue();
					u.type = "text";
					return u;
				}).collect(Collectors.toList());
		generatedResponse.originalRequest.body = newBody;

		return generatedResponse;
	}

}
