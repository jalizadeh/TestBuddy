package com.jalizadeh.testbuddy.central;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jalizadeh.testbuddy.filter.EmptyFilter;
import com.jalizadeh.testbuddy.filter.InvalidFilter;
import com.jalizadeh.testbuddy.filter.MissingFilter;
import com.jalizadeh.testbuddy.filter.RandomFilter;
import com.jalizadeh.testbuddy.interfaces.FilterAbstract;
import com.jalizadeh.testbuddy.types.FilterType;

@Component
public class FilterFactory {

	
	private List<FilterAbstract> listFilters = Arrays.asList(
				new EmptyFilter(),
				new InvalidFilter(),
				new MissingFilter(),
				new RandomFilter()
			);
	
	
	
	public FilterAbstract create(FilterType filterName) throws Exception {
		return listFilters.stream()
			.filter(f -> f.getFilterName().equals(filterName))
			.findFirst()
			.orElseThrow(() -> new Exception("The filter: \"" + filterName + "\" doesnt exist"));
	}
}
