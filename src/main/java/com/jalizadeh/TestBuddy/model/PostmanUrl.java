package com.jalizadeh.TestBuddy.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostmanUrl {
	public String raw;
	public String protocol;
	public String port;
	public List<String> host;
	public List<String> path;
	public List<PostmanParameter> query;
	
	
	@JsonIgnore
	public Map<String, PostmanParameter> getQueries() {
		Map<String, PostmanParameter> result = new HashMap<>();
		if (query == null || query.isEmpty()) {
			return result;
		}
		
		System.err.println("Query list: ");
		query.forEach(q -> {
			System.err.println(q.key + "/" + q.value + "/" + q.description);
			if(!q.description.isEmpty() && !q.description.isBlank()) {
				PostmanParameterDescriptionJSON desc = null;
				try {
					desc = new ObjectMapper().readValue(q.description, PostmanParameterDescriptionJSON.class);
					q.descJson = desc;
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			
			result.put(q.key, q);
		});
		
		return result;
	}
}
