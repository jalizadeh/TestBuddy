package com.jalizadeh.testbuddy.filter;

import java.util.Map;

import com.jalizadeh.testbuddy.interfaces.FilterAbstract;
import com.jalizadeh.testbuddy.model.postman.PostmanParameterDescriptionJSON;
import com.jalizadeh.testbuddy.types.FilterType;

public class MissingFilter extends FilterAbstract{

	@Override
	public FilterType getFilterName() {
		return FilterType.MISSING;
	}
	
	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName,
			PostmanParameterDescriptionJSON... desc) {
		parameters.remove(parameterName);
		return parameters; 
	}

}
