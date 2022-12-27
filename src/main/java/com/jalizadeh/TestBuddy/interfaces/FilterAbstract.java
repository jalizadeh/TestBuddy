package com.jalizadeh.testbuddy.interfaces;

import java.util.Map;

import com.jalizadeh.testbuddy.model.PostmanParameterDescriptionJSON;
import com.jalizadeh.testbuddy.types.Filters;

public abstract class FilterAbstract {
	
	public abstract Filters getFilterName();
	public abstract Map<String, String> applyFilter(Map<String, String> parameters, String parameterName, PostmanParameterDescriptionJSON... desc);

	protected void getTypedFilter(PostmanParameterDescriptionJSON desc) {
		
	}
	
	protected boolean hasDesc(PostmanParameterDescriptionJSON... desc) {
		return (desc != null && desc.length != 0) ? true : false;
	}
}
