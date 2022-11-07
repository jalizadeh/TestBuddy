package com.jalizadeh.TestBuddy.types;

import java.util.List;

import com.jalizadeh.TestBuddy.filter.Filters;

import lombok.Data;


@Data
public class InputRequest {

	List<Filters> filters;
	
}
