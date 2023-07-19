package com.jalizadeh.testbuddy.model.postman;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is a general model that can be used in query / header / form body
 * instead of having separate model for each and each will hold it's own descJson.
 * <br/><br/>
 * <b>Header has <i>type</i> which is ignored for now</b>
 */
public class PostmanParameter implements Cloneable {

	public String key;
	public String value;
	public String description;
	
	@JsonIgnore
	public PostmanParameterDescriptionJSON descJson = null;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
