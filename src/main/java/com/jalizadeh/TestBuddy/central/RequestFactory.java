package com.jalizadeh.TestBuddy.central;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jalizadeh.TestBuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.TestBuddy.requestImpl.RequestRawText;
import com.jalizadeh.TestBuddy.requestImpl.RequestUrlencodedText;

@Component
public class RequestFactory {

	private List<RequestPostmanAbstract> postmanRequests = Arrays.asList(
				new RequestRawText(),
				new RequestUrlencodedText()
			); 
	
	public RequestPostmanAbstract getRequest(String type) throws Exception {
		for(RequestPostmanAbstract req : postmanRequests) {
			if(req.bodyType().equals(type))
				return req;
		}
		
		throw new Exception("Request type is not correct / supported yet");
	}
}
