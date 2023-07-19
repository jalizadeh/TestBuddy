package com.jalizadeh.testbuddy.requestImpl;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;

import com.jalizadeh.testbuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.testbuddy.model.postman.PostmanBody;
import com.jalizadeh.testbuddy.model.postman.PostmanItem;
import com.jalizadeh.testbuddy.model.postman.PostmanResponse;
import com.jalizadeh.testbuddy.model.postman.PostmanUrlEncoded;
import com.jalizadeh.testbuddy.types.RequestBodyType;

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
