package com.jalizadeh.TestBuddy.filter;

import java.util.Map;

import com.jalizadeh.TestBuddy.interfaces.iFilter;

public class EmptyFilter implements  iFilter{
	
	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName) {
		parameters.put(parameterName, "");
		return parameters; 
	}

	@Override
	public String getFilterName() {
		return "EMPTY";
	}


}
