package com.jalizadeh.TestBuddy.requestImpl;

import com.jalizadeh.TestBuddy.interfaces.RequestAbstract;

public class RequestTestGet extends RequestAbstract{

	public String getRequest() {
		String url = "https://jsonplaceholder.typicode.com/posts";
		return restTemplate.getForObject(url, String.class);
	}
	
}
