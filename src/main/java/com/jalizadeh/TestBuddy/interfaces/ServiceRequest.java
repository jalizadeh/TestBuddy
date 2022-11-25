package com.jalizadeh.TestBuddy.interfaces;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.TestBuddy.central.RequestFactory;
import com.jalizadeh.TestBuddy.central.StatisticsManager;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanResponse;

public class ServiceRequest extends RequestAbstract {

	@Autowired
	RequestFactory requestFactory;
	
	private final String name;
	private String url;
	private final String method;
	private final String bodyMode;
	private HttpEntity<?> entity;
	private String queries;
	private HttpHeaders headers;
	private String data;

	public ServiceRequest(String name, String url, String method, String bodyMode) {
		this.requestFactory = new RequestFactory();
		this.name = name;
		this.url = url;
		this.method = method;
		this.bodyMode = bodyMode;
		this.entity = new HttpEntity<>(null);
		this.queries = "";
	}

	public ServiceRequest setQueries(Map<String, String> map) {
		this.queries = map.entrySet().stream()
				.map(p -> { return p.getKey() + "=" + p.getValue(); })
				.collect(Collectors.joining("&"));
		return this;
	}

	
	public ServiceRequest setHeaders(HttpHeaders headers) {
		this.headers = headers;
		this.entity = new HttpEntity<>(this.headers);
		return this;
	}

	public ServiceRequest setData(String data) {
		this.data = data;
		return setDataHeader(this.data, this.headers);
	}

	public ServiceRequest setDataHeader(String data, HttpHeaders headers) {
		this.headers = headers;
		this.data = data;
		this.entity = new HttpEntity<>(this.data, this.headers);
		return this;
	}

	/**
	 * The only handler that sends the request using the <i>dataMap</i>. Before using this method,
	 * the request (<i>this</i>) should be instantiated correctly  
	 */
	public PostmanResponse handleRequest(PostmanItem item, int count, String testCase, 
			String paramName, Map<String, String> dataMap) throws Exception {
		
		ResponseEntity<String> response = null;
		
		setData(mapToString(dataMap));
		

		switch (this.method) {
			case "GET":
				response = restTemplate.exchange(getUrl(), HttpMethod.GET, this.entity, String.class);
				break;
			case "POST":
				response = restTemplate.exchange(getUrl(), HttpMethod.POST, this.entity, String.class);
				break;
			case "PUT":
				response = restTemplate.exchange(getUrl(), HttpMethod.PUT, this.entity, String.class);
				break;
			case "DELETE":
				response = restTemplate.exchange(getUrl(), HttpMethod.DELETE, this.entity, String.class);
				break;
		}	

		StatisticsManager statMng = StatisticsManager.getInstance();
		statMng.addStat(this.name, this.method, this.url, response.getStatusCode().toString(), response.getStatusCode().is2xxSuccessful() ? true : false);
		
		//based on the input collection, the appropriate request handler is selected
		RequestPostmanAbstract request = requestFactory.getRequest(this.bodyMode);
		return request.handleRequest(item, count, testCase, paramName, dataMap, this.headers, response);
	}

	private String getUrl() {
		return this.url + (this.queries.length() > 0 ? "?" + this.queries : "");
	}

}
