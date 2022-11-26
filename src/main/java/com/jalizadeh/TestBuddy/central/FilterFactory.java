package com.jalizadeh.TestBuddy.central;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jalizadeh.TestBuddy.filter.EmptyFilter;
import com.jalizadeh.TestBuddy.filter.InvalidFilter;
import com.jalizadeh.TestBuddy.filter.MissingFilter;
import com.jalizadeh.TestBuddy.filter.RandomFilter;
import com.jalizadeh.TestBuddy.interfaces.FilterAbstract;
import com.jalizadeh.TestBuddy.types.Filters;

@Component
public class FilterFactory {

	
	private List<FilterAbstract> listFilters = Arrays.asList(
				new EmptyFilter(),
				new InvalidFilter(),
				new MissingFilter(),
				new RandomFilter()
			);
	
	
	
	public FilterAbstract create(Filters filterName) throws Exception {
		return listFilters.stream()
			.filter(f -> f.getFilterName().equals(filterName))
			.findFirst()
			.orElseThrow(() -> new Exception("The filter: \"" + filterName + "\" doesnt exist"));
	}
}
