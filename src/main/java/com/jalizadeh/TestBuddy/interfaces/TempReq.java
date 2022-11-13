package com.jalizadeh.TestBuddy.interfaces;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class TempReq  extends RequestAbstract{

	
	private final String url;
	private final String method;
	private HttpEntity<?> entity;
	private HttpHeaders headers;
	private String data;
	
	
	public TempReq(String url, String method) {
		this.url = url;
		this.method = method;
		this.entity = new HttpEntity<>(null);
	}
	
	
	public TempReq setHeaders(HttpHeaders headers) {
		this.headers = headers;
		this.entity = new HttpEntity<>(this.headers);
		return this;
	}
	
	public TempReq setData(String data) {
		this.data = data;
		return setDataHeader(this.data, this.headers);
	}
	
	public TempReq setDataHeader(String data, HttpHeaders headers) {
		this.headers = headers;
		this.data = data;
		this.entity = new HttpEntity<>(this.data, this.headers);
		return this;
	}
	
	
	public ResponseEntity<String> handleRequest(){
		switch (this.method) {
			case "GET":
				return restTemplate.getForEntity(this.url, String.class);
			case "POST":
				return restTemplate.postForEntity(this.url, this.entity, String.class);
			default:
				return null;
		}
	}
	
	
}
