package com.jalizadeh.TestBuddy.requestImpl;

import java.util.Map;

import org.springframework.http.HttpHeaders;

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
	public PostmanResponse handleResponseBody(PostmanItem item, PostmanResponse initializedResponse,
			Map<String, String> queryMap, Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException {

		return initializedResponse;
	}
	
}