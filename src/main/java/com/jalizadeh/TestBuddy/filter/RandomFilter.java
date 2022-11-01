package com.jalizadeh.TestBuddy.filter;

import java.util.Map;
import java.util.UUID;

import com.jalizadeh.TestBuddy.interfaces.iFilter;

public class RandomFilter implements iFilter{

	@Override
	public Filters getFilterName() {
		return Filters.RANDOM;
	}

	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName) {
		parameters.put(parameterName, UUID.randomUUID().toString().split("-")[0]);
		return parameters; 
	}

}
