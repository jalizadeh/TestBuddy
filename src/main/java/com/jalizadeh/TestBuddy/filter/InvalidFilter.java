package com.jalizadeh.TestBuddy.filter;

import java.util.Map;

import com.jalizadeh.TestBuddy.interfaces.iFilter;

public class InvalidFilter implements iFilter{

	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName) {
		parameters.put(parameterName, parameters.get(parameterName) + "_INVALID");
		return parameters; 
	}

	@Override
	public Filters getFilterName() {
		return Filters.INVALID;
	}


}
