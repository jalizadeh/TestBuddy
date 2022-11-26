package com.jalizadeh.TestBuddy.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	public String getQueriesAsString() {
		if (query == null || query.isEmpty()) {
			return "";
		}

		return getQueriesNoDescMap().entrySet().stream().map(p -> {
					return p.getKey() + "=" + p.getValue();
				}).collect(Collectors.joining("&"));
	}
	
	//Map of the key & value of the query 
	@JsonIgnore
	public Map<String, String> getQueriesNoDescMap() {
		if (query == null || query.isEmpty()) {
			return new HashMap<>();
		}

		Map<String, String> dataMap = new HashMap<>();
		this.query.stream()
				// .filter(i -> Objects.nonNull(i.disabled))
				// .filter(i -> !i.disabled)
				.forEach(i -> dataMap.put(i.key, i.value));

		return dataMap;
	}
	
	//Map of the query and it's object
	@JsonIgnore
	public Map<String, PostmanParameter> getQueriesFullMap() {
		if (query == null || query.isEmpty()) {
			return new HashMap<>();
		}
		
		Map<String, PostmanParameter> result = new HashMap<>(); 
		query.forEach(q -> {
			//System.err.println(q.key + "/" + q.value + "/" + q.description);
			if(q.description != null && !q.description.isEmpty() && !q.description.isBlank()) {
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
