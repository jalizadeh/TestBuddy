package com.jalizadeh.testbuddy.requestImpl;

import com.jalizadeh.testbuddy.interfaces.RequestAbstract;

public class RequestTestGet extends RequestAbstract{

	public String getRequest() {
		String url = "https://jsonplaceholder.typicode.com/posts";
		return restTemplate.getForObject(url, String.class);
	}
	
}
