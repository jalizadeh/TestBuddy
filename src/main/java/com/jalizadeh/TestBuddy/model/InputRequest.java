package com.jalizadeh.TestBuddy.model;

import java.util.List;

import com.jalizadeh.TestBuddy.types.Filters;

import lombok.Data;


@Data
public class InputRequest {

	List<Filters> filters;
	
}
