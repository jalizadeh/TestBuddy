package com.jalizadeh.TestBuddy.interfaces;

import java.util.Map;

public interface iFilter {
	
	String getFilterName();
	Map<String, String> applyFilter(Map<String, String> parameters, String parameterName);
	
}
