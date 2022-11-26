package com.jalizadeh.TestBuddy.filter;

import java.util.Map;

import com.jalizadeh.TestBuddy.interfaces.FilterAbstract;
import com.jalizadeh.TestBuddy.model.PostmanParameterDescriptionJSON;
import com.jalizadeh.TestBuddy.types.Filters;

/**
 * This filter will put empty value instead of the parameter's <i>value</i>
 */
public class EmptyFilter extends FilterAbstract{
	
	@Override
	public Filters getFilterName() {
		return Filters.EMPTY;
	}
	
	
	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName,
			PostmanParameterDescriptionJSON... desc) {
		parameters.put(parameterName, "");
		return parameters; 
	}

}
