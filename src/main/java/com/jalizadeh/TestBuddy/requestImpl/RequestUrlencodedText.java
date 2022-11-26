package com.jalizadeh.TestBuddy.requestImpl;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;

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
	public PostmanResponse handleResponseBody(PostmanItem item, PostmanResponse initializedResponse,
			Map<String, String> queryMap, Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException {
		
		PostmanBody newBody = (PostmanBody) item.request.body.clone();
		newBody.urlencoded = dataMap.entrySet().stream()
				.map(e -> {
					PostmanUrlEncoded u = new PostmanUrlEncoded();
					u.key = e.getKey();
					u.value = e.getValue();
					u.type = "text";
					return u;
				}).collect(Collectors.toList());
		initializedResponse.originalRequest.body = newBody;

		return initializedResponse;
	}

}
