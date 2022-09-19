package com.jalizadeh.TestBuddy.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import com.jalizadeh.TestBuddy.filter.EmptyFilter;
import com.jalizadeh.TestBuddy.filter.InvalidFilter;
import com.jalizadeh.TestBuddy.filter.MissingFilter;
import com.jalizadeh.TestBuddy.interfaces.iFilter;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.model.PostmanHeader;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.runner.PostmanRunResult;

@Service
public class RestService {

	private final RestTemplate restTemplate;
	private StringBuffer result= new StringBuffer();
	private List<PostmanResponse> responseList = new ArrayList<PostmanResponse>();
	
	
	public RestService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

	public String getRequest() {
		String url = "https://jsonplaceholder.typicode.com/posts";
		return this.restTemplate.getForObject(url, String.class);
	}
	

	public StringBuffer sendRequest() {
		
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
		Map<String, String> dataMap = new HashMap<String, String>();
		
		
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
					dataMap.put(d[0], d[1]);
				}
			}
		}
		
		System.out.println(httpMethod + " / " + url + " / " + header.size() + " / " + dataMap.size());
		for (Entry<String, String> entry : dataMap.entrySet()) {
		    System.out.println(entry.getKey() + "\t" + entry.getValue().toString());
		}
		
		
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
		
		
		List<iFilter> filters = new ArrayList<iFilter>();
		filters.add(new EmptyFilter());
		//filters.add(new InvalidFilter());
		//filters.add(new MissingFilter());
		
		for(iFilter filter : filters) {
			for(int j = 1; j < lenght; j++) {
				temp.clear();
				temp.putAll(dataMap);
				for(int i = 0; i < pNum; i++) {
					if(!arr[i][j]) {
						temp = filter.applyFilter(temp, dataArr[i]);
					}
				}
				
				result.append(handleRequest(filter.getFilterName() + " - #" + j, url, temp, headers));
			}
		}
		
		
		return result;
	}

	
	public List<PostmanResponse> parseCollection(PostmanCollection collection) {
		PostmanItem item = collection.item.get(0).item.get(0);
		
		String name = item.name;
		
		
		String httpMethod = item.request.method;
		String url = item.request.url.raw;
		Map<String, String> header = item.request.getHeaders();
		String data = item.request.getData();
		Map<String, String> dataMap = new HashMap<String, String>();
		
		String[] dataPair = data.split("&");
		for(String dp : dataPair) {
			String[] d = dp.split("=");
			dataMap.put(d[0], d[1]);
		}
		
		System.out.println(httpMethod + "\n" + url + "\n" + header.size() + "\n" + dataMap.size());
		for (Entry<String, String> entry : dataMap.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue().toString());
		}
		
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
		responseList.add(handleCollectionRequest(0, "OK", "", url, dataMap, headers));
		
		
		List<iFilter> filters = new ArrayList<iFilter>();
		filters.add(new EmptyFilter());
		//filters.add(new InvalidFilter());
		//filters.add(new MissingFilter());

		List<String> paramName = new ArrayList<String>();
		
		for(iFilter filter : filters) {
			for(int j = 1; j < lenght; j++) {
				temp.clear();
				temp.putAll(dataMap);
				for(int i = 0; i < pNum; i++) {
					if(!arr[i][j]) {
						paramName.add(dataArr[i]);
						temp = filter.applyFilter(temp, dataArr[i]);
					}
				}
				
				String paramNames = String.join(",", paramName);
				paramName.clear();
				responseList.add(handleCollectionRequest(j, filter.getFilterName(), paramNames, url, temp, headers));
			}
		}
		
		
		return responseList;
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
	
	
	private PostmanResponse handleCollectionRequest(int count, String testCase, String paramName, String url, Map<String, String> dataMap, HttpHeaders headers) {
		//start delay
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(dataMap, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);
		
		PostmanResponse postmanResponse = new PostmanResponse();
		
		String title = "<br><h3>Case: " + testCase + "</h3>";
		String body = response.getBody();
		String resp = response.toString();
		
		
		System.err.println(title);
		System.out.println(body + "\n" + resp);
		
		postmanResponse.name = response.getStatusCodeValue() + " - " + testCase + " " + paramName;
		postmanResponse.status = response.getStatusCode().name();
		postmanResponse.code = response.getStatusCodeValue();
		postmanResponse._postman_previewlanguage = "json";
		postmanResponse.body = response.getBody();
		
		
		//return title + body + "<br>" + resp;
		return postmanResponse;
	}
}