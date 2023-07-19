package com.jalizadeh.testbuddy.requestImpl;

import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.jalizadeh.testbuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.testbuddy.model.postman.PostmanItem;
import com.jalizadeh.testbuddy.model.postman.PostmanResponse;
import com.jalizadeh.testbuddy.types.RequestBodyType;

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