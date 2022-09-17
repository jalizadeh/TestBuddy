package com.jalizadeh.TestBuddy.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jalizadeh.TestBuddy.exception.RestTemplateResponseErrorHandler;

@Service
public class RestService {

	private final RestTemplate restTemplate;
	private StringBuffer result= new StringBuffer();
	
	public RestService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

	public String getRequest() {
		String url = "https://jsonplaceholder.typicode.com/posts";
		return this.restTemplate.getForObject(url, String.class);
	}
	

	public StringBuffer sendRequest(String httpMethod, String url, Map<String, String> headerMap, Map<String, String> dataMap) {
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		int pNum = dataMap.size();
		int lenght = (int) Math.pow(2, pNum);
		// iTestCase[][] arr = new TestCase[pNum][lenght];
		boolean[][] arr = new boolean[pNum][lenght];

		int space = 0;
		int counter = 0;
		boolean lastVal = true;
		for (int i = 0; i < pNum; i++) {
			space = (int) Math.pow(2, i);
			counter = 1;
			lastVal = true;
			for (int j = 0; j < lenght; j++) {
				// arr[i][j] = new TestCase(lastVal);
				arr[i][j] = lastVal;

				if (counter == space) {
					lastVal = !lastVal;
					counter = 1;
				} else {
					counter++;
				}

				// System.out.println(i + ":" + j + " = " + arr[i][j] + "\t\t"+ counter + "/" + space);
			}
		}
		
		System.out.print("\t");
		for(int j = 0; j < lenght; j++) {
			System.out.print(j + "\t");
		}
		System.out.println();
		for(int i = 0; i < pNum; i++) {
			System.out.print(i + ":\t");
			for(int j = 0; j < lenght; j++) {
				System.out.print( arr[i][j] + "\t");
			}
			
			System.out.println();
		}
		
		
		String[] dataArr = new String[dataMap.size()]; 
		int dataCount = 0;
		System.err.println("Using Array : \n");
		Map<String, String> temp = new HashMap<>();
		for (Entry<String, String> entry : dataMap.entrySet()) {
			dataArr[dataCount++] = entry.getKey();
			
			temp.putAll(dataMap);
			System.out.println("OK\t" + entry.getKey() + "\t" + entry.getValue().toString());
			
		}

		
		//run first case which all are OK
		result.append(handleRequest("OK - #0", url, dataMap, headers));
		
		//empty
		for(int j = 1; j < lenght; j++) {
			temp.clear();
			temp.putAll(dataMap);
			for(int i = 0; i < pNum; i++) {
				if(!arr[i][j]) {
					//System.out.println(dataArr[i] + " - " + i + "/"+j);
					temp.put(dataArr[i], "");
				}
			}
			
			result.append(handleRequest("EMPTY - #" + j, url, temp, headers));
		}
		
		//invalid
		for (int j = 1; j < lenght; j++) {
			temp.clear();
			temp.putAll(dataMap);
			for (int i = 0; i < pNum; i++) {
				if (!arr[i][j]) {
					// System.out.println(dataArr[i] + " - " + i + "/"+j);
					temp.put(dataArr[i], dataMap.get(dataArr[i]) + "_INVALID");
				}
			}

			result.append(handleRequest("INVALID - #" + j, url, temp, headers));
		}
		
		
		//missing
		for (int j = 1; j < lenght; j++) {
			temp.clear();
			temp.putAll(dataMap);
			for (int i = 0; i < pNum; i++) {
				if (!arr[i][j]) {
					// System.out.println(dataArr[i] + " - " + i + "/"+j);
					temp.remove(dataArr[i]);
				}
			}

			result.append(handleRequest("MISSING - #" + j, url, temp, headers));
		}
		
		
		return result;
	}

	
	private String handleRequest(String testCase, String url, Map<String, String> dataMap, HttpHeaders headers) {
		//start delay
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(dataMap, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);
		
		String title = "<br><h3>Case: " + testCase + "</h3>";
		String body = response.getBody();
		String resp = response.toString();
		
		System.err.println(title);
		System.out.println(body + "\n" + resp);
		
		return title + body + "<br>" + resp;
	}
}