package com.jalizadeh.testbuddy.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PostmanRequest implements Cloneable{
	
	public PostmanAuth auth;
	public String method;
	public List<PostmanParameter> header;
	public PostmanBody body;
	public PostmanUrl url;
	
	@JsonIgnore
	public String getBodyMode() {
		return (body == null || body.mode == null) ? "" : body.mode;
	}

	@JsonIgnore
	public String getData() {
		if (body == null || body.mode == null)  {
			return "";
		} else {
			switch (body.mode) {
				case "raw":
					return body.raw;
				case "urlencoded":
					return urlFormEncodeData(body.urlencoded);
				default:
					return "";
			}
		}
	}
	
	@JsonIgnore
	public Map<String,String> getDataMap() {
		if (body == null || body.mode == null)  {
			return new HashMap<>();
		} else {
			switch (body.mode) {
				case "raw":
					return rawDataMap(body.raw);
				case "urlencoded":
					return urlFormEncodeDataMap(body.urlencoded);
				default:
					return new HashMap<>();
			}
		}
	}

	
	

	
	/*
	public String getData(PostmanVariables var) {
		if (body == null || body.mode == null)  {
			return "";
		} else {
			switch (body.mode) {
				case "raw":
					return var.replace(body.raw);
				case "urlencoded":
					return urlFormEncodeData(body.urlencoded);
				default:
					return "";
			}
		}
	}
	*/
	
	
	

	@JsonIgnore
	public String getUrl(PostmanVariables pv) {
		return pv.replace(url.raw);
	}
	
	
	@JsonIgnore
	public String getFullUrl() {
		PostmanUrl u = this.url;
		String difiniteUrl = u.protocol + "://" 
				+ u.host.stream().collect(Collectors.joining(".")) 
				+ ":" 
				+ u.port;
		
		if(u.path != null && u.path.size() > 0)
			return difiniteUrl + "/" + u.path.stream().collect(Collectors.joining("/"));
		
		return difiniteUrl;
	}
	

	@JsonIgnore
	public Map<String, String> getHeaders(PostmanVariables pv) {
		Map<String, String> result = new HashMap<>();
		if (header == null || header.isEmpty()) {
			return result;
		}
		for (PostmanParameter head : header) {
			if (head.key.toUpperCase().equals(PoyntHttpHeaders.REQUEST_ID_HEADER)) {
				result.put(head.key.toUpperCase(), pv.replace(head.value));
			} else {
				result.put(head.key, pv.replace(head.value));
			}
		}
		return result;
	}
	
	
	@JsonIgnore
	public Map<String, String> getHeaders(boolean... forTest) {
		Map<String, String> result = new HashMap<>();
		if (header == null || header.isEmpty()) {
			return result;
		}
		for (PostmanParameter head : header) {
			if (head.key.toUpperCase().equals(PoyntHttpHeaders.REQUEST_ID_HEADER)) {
				result.put(head.key.toUpperCase(), head.value);
			} 
			
			//TODO: improve logic
			if (forTest.length == 0) {
				result.put(head.key, head.value);
			} else if (forTest.length > 0 && head.key.toLowerCase().contains("authorization")) {
				result.put(head.key, head.value);
			}
		}
		return result;
	}
	
	
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	//TODO: fix for request with raw-json which causes serialization issue
	private Map<String, String> rawDataMap(String raw) {
		if (body == null || body.raw == null) {
			return new HashMap<>();
		}

		Map<String, String> dataMap = new HashMap<>();
		for (String dp : raw.split("&")) {
			String[] d = dp.split("=");
			dataMap.put(d[0], (d.length == 1) ? "" : d[1]);
		}
		return dataMap;
	}

	private String urlFormEncodeData(/* PostmanVariables var, */ List<PostmanUrlEncoded> formData) {
		String result = "";
		int i = 0;
		for (PostmanUrlEncoded encoded : formData) {
			// result += encoded.key + "=" + URLEncoder.encode(var.replace(encoded.value));
			result += encoded.key + "=" + encoded.value;
			if (i < formData.size() - 1) {
				result += "&";
				i++;
			}
		}
		return result;
	}

	// TODO: it should be
	// MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
	private Map<String, String> urlFormEncodeDataMap(/* PostmanVariables var, */ List<PostmanUrlEncoded> formData) {
		if (body == null || body.urlencoded == null) {
			return new HashMap<>();
		}

		Map<String, String> dataMap = new HashMap<>();
		formData.stream()
				// .filter(i -> Objects.nonNull(i.disabled))
				// .filter(i -> !i.disabled)
				.forEach(i -> dataMap.put(i.key, i.value));

		return dataMap;
	}
	
}
