package com.jalizadeh.testbuddy.filter;

import java.util.Map;

import com.jalizadeh.testbuddy.interfaces.FilterAbstract;
import com.jalizadeh.testbuddy.model.postman.PostmanParameterDescriptionJSON;
import com.jalizadeh.testbuddy.types.FilterType;

public class InvalidFilter extends FilterAbstract{
	
	@Override
	public FilterType getFilterName() {
		return FilterType.INVALID;
	}

	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName,
			PostmanParameterDescriptionJSON... desc) {
		if(hasDesc(desc)) {
			//TODO: FIX > for test hard-coded value is set
			parameters.put(parameterName, "0..0");
		} else {
			parameters.put(parameterName, parameters.get(parameterName) + "_INVALID");
		}
		return parameters; 
	}

}
