package com.jalizadeh.TestBuddy.filter;

import java.util.Map;

import com.jalizadeh.TestBuddy.interfaces.FilterAbstract;
import com.jalizadeh.TestBuddy.model.PostmanParameterDescriptionJSON;
import com.jalizadeh.TestBuddy.types.Filters;

public class MissingFilter extends FilterAbstract{

	@Override
	public Filters getFilterName() {
		return Filters.MISSING;
	}
	
	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName,
			PostmanParameterDescriptionJSON... desc) {
		parameters.remove(parameterName);
		return parameters; 
	}

}
