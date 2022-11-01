package com.jalizadeh.TestBuddy.model;

import java.util.List;

import com.jalizadeh.TestBuddy.filter.Filters;

import lombok.Data;


@Data
public class InputRequest {

	List<Filters> filters;
	
}
