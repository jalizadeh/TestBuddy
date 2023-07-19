package com.jalizadeh.testbuddy.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PostmanBody implements Cloneable{
	public String mode;
	public String raw;
	public List<PostmanUrlEncoded> urlencoded;
	public PostmanBodyOptions options;
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
