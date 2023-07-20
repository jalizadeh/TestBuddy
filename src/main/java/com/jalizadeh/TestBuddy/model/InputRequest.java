package com.jalizadeh.testbuddy.model;

import java.util.List;

import com.jalizadeh.testbuddy.types.FilterType;

import lombok.Data;


@Data
public class InputRequest {

	List<FilterType> filters;
	
}
