package com.jalizadeh.TestBuddy.filter;

import java.util.Map;

import com.jalizadeh.TestBuddy.interfaces.iFilter;

public class MissingFilter implements iFilter{

	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName) {
		parameters.remove(parameterName);
		return parameters; 
	}

	@Override
	public Filters getFilterName() {
		return Filters.MISSING;
	}

}
