package com.jalizadeh.TestBuddy.central;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jalizadeh.TestBuddy.filter.Filters;
import com.jalizadeh.TestBuddy.interfaces.iFilter;

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
	
	public List<iFilter> getFilters(){
		return this.filters.values().stream().collect(Collectors.toList());
	}
	
}
