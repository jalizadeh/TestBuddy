package com.jalizadeh.testbuddy.requestImpl;

import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.jalizadeh.testbuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.testbuddy.model.postman.PostmanBody;
import com.jalizadeh.testbuddy.model.postman.PostmanItem;
import com.jalizadeh.testbuddy.model.postman.PostmanResponse;
import com.jalizadeh.testbuddy.types.RequestBodyType;

public class RequestRawText extends RequestPostmanAbstract {
	
	@Override
	public String bodyType() {
		return RequestBodyType.RAW_TEXT.type();
	}
	
	
	@Override
	public PostmanResponse handleResponseBody(PostmanItem item, PostmanResponse initializedResponse,
			Map<String, String> queryMap, Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException {
		
		PostmanBody newBody = (PostmanBody) item.request.body.clone();
		newBody.raw = mapToString(dataMap);
		initializedResponse.originalRequest.body = newBody;

		return initializedResponse;
	}
	
}
