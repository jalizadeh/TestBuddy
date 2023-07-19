package com.jalizadeh.testbuddy.filter;

import java.util.Map;

import com.jalizadeh.testbuddy.interfaces.FilterAbstract;
import com.jalizadeh.testbuddy.model.postman.PostmanParameterDescriptionJSON;
import com.jalizadeh.testbuddy.types.FilterType;

/**
 * This filter will put empty value instead of the parameter's <i>value</i>
 */
public class EmptyFilter extends FilterAbstract{
	
	@Override
	public FilterType getFilterName() {
		return FilterType.EMPTY;
	}
	
	
	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName,
			PostmanParameterDescriptionJSON... desc) {
		parameters.put(parameterName, "");
		return parameters; 
	}

}
