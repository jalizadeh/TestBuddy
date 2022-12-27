package com.jalizadeh.testbuddy.requestImpl;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;

import com.jalizadeh.testbuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.testbuddy.model.PostmanBody;
import com.jalizadeh.testbuddy.model.PostmanItem;
import com.jalizadeh.testbuddy.model.PostmanResponse;
import com.jalizadeh.testbuddy.model.PostmanUrlEncoded;
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
