package com.jalizadeh.TestBuddy.filter;

import java.util.Map;
import java.util.UUID;

import com.jalizadeh.TestBuddy.interfaces.FilterAbstract;
import com.jalizadeh.TestBuddy.model.PostmanParameterDescriptionJSON;
import com.jalizadeh.TestBuddy.types.Filters;

public class RandomFilter extends FilterAbstract{

	@Override
	public Filters getFilterName() {
		return Filters.RANDOM;
	}

	@Override
	public Map<String, String> applyFilter(Map<String, String> parameters, String parameterName,
			PostmanParameterDescriptionJSON... desc) {
		if(hasDesc(desc)) {
			//TODO: FIX > for test hard-coded value is set
			parameters.put(parameterName, Math.random()*10000 + "");
		} else {
			parameters.put(parameterName, UUID.randomUUID().toString().split("-")[0]);
		}
		return parameters; 
	}

}
