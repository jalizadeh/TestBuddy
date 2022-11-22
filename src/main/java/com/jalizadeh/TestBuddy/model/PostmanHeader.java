package com.jalizadeh.TestBuddy.model;

public class PostmanHeader implements Cloneable {
	
	public String key;
	public String value;
	public String type;
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
