package com.jalizadeh.TestBuddy.central;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jalizadeh.TestBuddy.filter.EmptyFilter;
import com.jalizadeh.TestBuddy.filter.Filters;
import com.jalizadeh.TestBuddy.filter.InvalidFilter;
import com.jalizadeh.TestBuddy.filter.MissingFilter;
import com.jalizadeh.TestBuddy.filter.RandomFilter;
import com.jalizadeh.TestBuddy.interfaces.iFilter;

@Component
public class FilterFactory {

	
	private List<iFilter> listFilters = Arrays.asList(
				new EmptyFilter(),
				new InvalidFilter(),
				new MissingFilter(),
				new RandomFilter()
			);
	
	
	
	public iFilter create(Filters filterName) throws Exception {
		for(iFilter f : listFilters) {
			if(f.getFilterName().equals(filterName))
				return f;
		}
		
		throw new Exception("The filter: \"" + filterName + "\" doesnt exist");
	}
}
