package com.jalizadeh.TestBuddy.central;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jalizadeh.TestBuddy.interfaces.iFilter;
import com.jalizadeh.TestBuddy.types.Filters;

public class FiltersManager {

	private static FiltersManager instance;
	private Map<Filters, iFilter> filters;
	
	
	private FiltersManager() {
		this.filters = new HashMap<>();
	}
	
	
	public static FiltersManager getInstance() {
		if(instance == null)
			instance = new FiltersManager();
		
		return instance;
	}
	
	
	public void addFilter(iFilter filter) {
		this.filters.put(filter.getFilterName(), filter);
	}
	
	//List is sorted ascending by filter's name
	public List<iFilter> getFilters(){
		return this.filters.values().stream()
				.sorted((a,b) -> a.getFilterName().compareTo(b.getFilterName()))
				.collect(Collectors.toList());
	}


	public void clearFilters() {
		this.filters.clear();
	}
	
}
