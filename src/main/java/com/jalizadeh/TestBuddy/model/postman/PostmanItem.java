package com.jalizadeh.testbuddy.model.postman;

import java.util.List;

public class PostmanItem {
	public String name;
	public PostmanProtocolProfileBehavior protocolProfileBehavior;
	public List<PostmanEvent> event;
	public PostmanRequest request;
	public List<PostmanResponse> response;
}
