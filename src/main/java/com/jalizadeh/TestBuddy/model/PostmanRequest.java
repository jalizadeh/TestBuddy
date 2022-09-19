package com.jalizadeh.TestBuddy.model;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostmanRequest {
	
	public PostmanAuth auth;
	public String method;
	public List<PostmanHeader> header;
	public PostmanBody body;
	public PostmanUrl url;
	
	public String getData() {
		if (body == null || body.mode == null)  {
			return "";
		} else {
			switch (body.mode) {
				case "raw":
					return body.raw;
				/*
				case "urlencoded":
					return urlFormEncodeData(body.urlencoded);
				*/
				default:
					return "";
			}
		}
	}

	public String getData(PostmanVariables var) {
		if (body == null || body.mode == null)  {
			return "";
		} else {
			switch (body.mode) {
				case "raw":
					return var.replace(body.raw);
				case "urlencoded":
					return urlFormEncodeData(var, body.urlencoded);
				default:
					return "";
			}
		}
	}

	public String urlFormEncodeData(PostmanVariables var, List<PostmanUrlEncoded> formData) {
		String result = "";
		int i = 0;
		for (PostmanUrlEncoded encoded : formData) {
			result += encoded.key + "=" + URLEncoder.encode(var.replace(encoded.value));
			if (i < formData.size() - 1) {
				result += "&";
			}
		}
		return result;
	}

	public String getUrl(PostmanVariables var) {
		return var.replace(url.raw);
	}

	public Map<String, String> getHeaders(PostmanVariables var) {
		Map<String, String> result = new HashMap<>();
		if (header == null || header.isEmpty()) {
			return result;
		}
		for (PostmanHeader head : header) {
			if (head.key.toUpperCase().equals(PoyntHttpHeaders.REQUEST_ID_HEADER)) {
				result.put(head.key.toUpperCase(), var.replace(head.value));
			} else {
				result.put(head.key, var.replace(head.value));
			}
		}
		return result;
	}
	
	public Map<String, String> getHeaders() {
		Map<String, String> result = new HashMap<>();
		if (header == null || header.isEmpty()) {
			return result;
		}
		for (PostmanHeader head : header) {
			if (head.key.toUpperCase().equals(PoyntHttpHeaders.REQUEST_ID_HEADER)) {
				result.put(head.key.toUpperCase(), head.value);
			} else {
				result.put(head.key, head.value);
			}
		}
		return result;
	}
}
