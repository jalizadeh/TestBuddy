package com.jalizadeh.testbuddy.central;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jalizadeh.testbuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.testbuddy.requestImpl.RequestNoBody;
import com.jalizadeh.testbuddy.requestImpl.RequestRawText;
import com.jalizadeh.testbuddy.requestImpl.RequestUrlencodedText;

@Component
public class RequestFactory {

	private List<RequestPostmanAbstract> postmanRequests = Arrays.asList(
				new RequestNoBody(),
				new RequestRawText(),
				new RequestUrlencodedText()
			); 
	
	public RequestPostmanAbstract getRequest(String type) throws Exception {
		return postmanRequests.stream()
			.filter(r -> r.bodyType().equals(type))
			.findFirst()
			.orElseThrow(() -> new Exception("Request type is not correct / supported yet"));
	}
}
