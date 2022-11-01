package com.jalizadeh.TestBuddy.interfaces;

import java.util.Map;

import com.jalizadeh.TestBuddy.filter.Filters;

public interface iFilter {
	
	Filters getFilterName();
	Map<String, String> applyFilter(Map<String, String> parameters, String parameterName);
	
}
