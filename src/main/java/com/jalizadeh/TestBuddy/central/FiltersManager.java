package com.jalizadeh.testbuddy.central;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jalizadeh.testbuddy.interfaces.FilterAbstract;
import com.jalizadeh.testbuddy.types.Filters;

public class FiltersManager {

	private static FiltersManager instance;
	private Map<Filters, FilterAbstract> filters;
	
	
	private FiltersManager() {
		this.filters = new HashMap<>();
	}
	
	
	public static FiltersManager getInstance() {
		if(instance == null)
			instance = new FiltersManager();
		
		return instance;
	}
	
	
	public void addFilter(FilterAbstract filter) {
		this.filters.put(filter.getFilterName(), filter);
	}
	
	//List is sorted ascending by filter's name
	public List<FilterAbstract> getFilters(){
		return this.filters.values().stream()
				.sorted((a,b) -> a.getFilterName().compareTo(b.getFilterName()))
				.collect(Collectors.toList());
	}


	public void clearFilters() {
		this.filters.clear();
	}
	
}
