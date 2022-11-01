package com.jalizadeh.TestBuddy.central;

import java.util.ArrayList;
import java.util.List;

import com.jalizadeh.TestBuddy.interfaces.iFilter;

public class FiltersManager {

	private static FiltersManager instance;
	private List<iFilter> listFilters;
	
	
	private FiltersManager() {
		this.listFilters = new ArrayList<>();
	}
	
	
	public static FiltersManager getInstance() {
		if(instance == null)
			instance = new FiltersManager();
		
		return instance;
	}
	
	
	public void addFilter(iFilter filter) {
		this.listFilters.add(filter);
	}
	
	public List<iFilter> getFilters(){
		return this.listFilters;
	}
	
}
