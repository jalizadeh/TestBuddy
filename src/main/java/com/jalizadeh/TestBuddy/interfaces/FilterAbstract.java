package com.jalizadeh.TestBuddy.interfaces;

import java.util.Map;

import com.jalizadeh.TestBuddy.model.PostmanParameterDescriptionJSON;
import com.jalizadeh.TestBuddy.types.Filters;

public abstract class FilterAbstract {
	
	public abstract Filters getFilterName();
	public abstract Map<String, String> applyFilter(Map<String, String> parameters, String parameterName, PostmanParameterDescriptionJSON... desc);

	protected void getTypedFilter(PostmanParameterDescriptionJSON desc) {
		
	}
	
	protected boolean hasDesc(PostmanParameterDescriptionJSON... desc) {
		return (desc != null && desc.length != 0) ? true : false;
	}
}
