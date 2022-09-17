package com.jalizadeh.TestBuddy.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalizadeh.TestBuddy.service.RestService;

@RestController
public class Handler {

	@Autowired
	private RestService restService;
	
	@GetMapping("/parse")
	public StringBuffer parse() {
		
		restService = new RestService(new RestTemplateBuilder());
		
		/**
		 * Total cases:
		 * (2^p-1)*scenarios + 1 (ok case which is only 1)
		 * 
		 * P = 3 (user, pass, grant)
		 * scenarios = 3 (empty, invalid, missing)
		 * => 7 * 3 + 1 = 22 test case
		 */
		
		final String sample="curl POST 'https://exmaple.com' --header 'Accept: application/json' --header 'Content-Type: application/x-www-form-urlencoded'  --header 'Authorization: Basic XYZ' --data-raw 'username=user@name.com&password=password123&grant_type=password'";
		
		
		String httpMethod = "";
		String url = "";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> data = new HashMap<String, String>();
		
		
		final String curl  = sample;
		
		//if(curl.trim().length() == 0) return;
		
		
		String inputSplit[] = sample.split(" ");
		for(String i : inputSplit) {
			if(i.contains("GET") || i.contains("POST"))	
				httpMethod = i;
			
			if(i.contains("https://"))
				url = i.substring(1,i.length()-1);
			
			if(i.contains(": ")) {
				String[] hPair = i.split(": ");
				header.put(hPair[0], hPair[1]);
			}
			
			if(i.contains("&")) {
				
				String[] dataPair = i.substring(1,i.length()-1).split("&");
				for(String dp : dataPair) {
					String[] d = dp.split("=");
					data.put(d[0], d[1]);
				}
			}
		}
		
		System.out.println(httpMethod + " / " + url + " / " + header.size() + " / " + data.size());
		for (Entry<String, String> entry : data.entrySet()) {
		    System.out.println(entry.getKey() + "\t" + entry.getValue().toString());
		}
		
		
		return restService.sendRequest(httpMethod, url, header, data);
	}
	
	
}
