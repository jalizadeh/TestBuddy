package com.jalizadeh.testbuddy.interfaces;

import java.util.Map;

import com.jalizadeh.testbuddy.model.postman.PostmanParameterDescriptionJSON;
import com.jalizadeh.testbuddy.types.FilterType;

public abstract class FilterAbstract {
	
	public abstract FilterType getFilterName();
	public abstract Map<String, String> applyFilter(Map<String, String> parameters, String parameterName, PostmanParameterDescriptionJSON... desc);

	protected void getTypedFilter(PostmanParameterDescriptionJSON desc) {
		
	}
	
	protected boolean hasDesc(PostmanParameterDescriptionJSON... desc) {
		return (desc != null && desc.length != 0);
	}
}
