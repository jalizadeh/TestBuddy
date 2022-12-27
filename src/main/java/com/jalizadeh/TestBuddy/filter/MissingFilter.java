package com.jalizadeh.testbuddy.filter;

import java.util.Map;

import com.jalizadeh.testbuddy.interfaces.FilterAbstract;
import com.jalizadeh.testbuddy.model.PostmanParameterDescriptionJSON;
import com.jalizadeh.testbuddy.types.Filters;

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
