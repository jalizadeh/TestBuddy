package com.jalizadeh.testbuddy.model;

import java.util.List;

import com.jalizadeh.testbuddy.types.Filters;

import lombok.Data;


@Data
public class InputRequest {

	List<Filters> filters;
	
}
