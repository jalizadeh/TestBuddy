package com.jalizadeh.TestBuddy.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jalizadeh.TestBuddy.exception.RestTemplateResponseErrorHandler;
import com.jalizadeh.TestBuddy.filter.EmptyFilter;
import com.jalizadeh.TestBuddy.interfaces.iFilter;
import com.jalizadeh.TestBuddy.model.PostmanBody;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.model.PostmanHeader;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanRequest;
import com.jalizadeh.TestBuddy.model.PostmanResponse;

@Service
public class RestService {

	private final RestTemplate restTemplate;
	private StringBuffer result= new StringBuffer();
	private List<PostmanResponse> responseList = new ArrayList<PostmanResponse>();
	private PostmanItem item = null;
	
	private int delay = 0; //optional delay between each request (during test)
	
	private int pNum; //number of parameters in the request's body 
	private int lenght;
	
	
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

	
	public PostmanCollection parseCollection(PostmanCollection collection, Optional<Integer> inDelay) 
			throws Exception, CloneNotSupportedException {
		responseList.clear();
		
		//optional delay between requests
		delay = (int) inDelay.orElse(0); 
		
		//for now, only the first request in the first item is selected
		item = collection.item.get(0).item.get(0);
		
		String httpMethod = item.request.method;
		String url = item.request.url.raw;

		//how data should be parsed and sent to endpoint
		String bodyMode = item.request.body.mode;
		String bodyOptions = item.request.body.options.raw.language;
		String dataType = bodyMode + "-" + bodyOptions;
		//System.out.println("Request Type: " + dataType);
		
		Map<String, String> header = item.request.getHeaders();
		
		String data = item.request.getData();
		Map<String, String> dataMap = new HashMap<String, String>();
		
		if(dataType.equals("raw-text")) {
			String[] dataPair = data.split("&");
			for(String dp : dataPair) {
				String[] d = dp.split("=");
				dataMap.put(d[0], d[1]);
			}	
		} else {
			System.out.println("item data: j: " + data);
		}
		
		
		System.out.println(httpMethod + "\n" + url + "\n" + header.size() + "\n" + dataMap.size());
		for (Entry<String, String> entry : dataMap.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue().toString());
		}
		
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		

		boolean[][] scenarioTable = scenarioTable(dataMap.size());
		
		
		String[] dataArr = new String[dataMap.size()]; 
		int dataCount = 0;
		System.err.println("Using Array : \n");
		for (Entry<String, String> entry : dataMap.entrySet()) {
			dataArr[dataCount++] = entry.getKey();
			System.out.println("OK\t" + entry.getKey() + "\t" + entry.getValue().toString());
		}

		
		//for now only raw_text is supported
		//TODO: change to oop to support Urlencode / JSON 
		
		
		//run first case which all are OK
		responseList.add(handleCollectionRequestRaw_Text(0, "OK", "", url, dataMap, headers));
		
		
		List<iFilter> filters = new ArrayList<iFilter>();
		filters.add(new EmptyFilter());
		//filters.add(new InvalidFilter());
		//filters.add(new MissingFilter());

		Map<String, String> modifiedParameters = new HashMap<>();
		List<String> paramName = new ArrayList<String>();
		
		for(iFilter filter : filters) {
			for(int j = 1; j < lenght; j++) {
				modifiedParameters.clear();
				modifiedParameters.putAll(dataMap);
				for(int i = 0; i < pNum; i++) {
					if(!scenarioTable[i][j]) {
						paramName.add(dataArr[i]);
						modifiedParameters = filter.applyFilter(modifiedParameters, dataArr[i]);
					}
				}
				
				String paramNames = String.join(",", paramName);
				paramName.clear();
				responseList.add(handleCollectionRequestRaw_Text(j, filter.getFilterName(), paramNames, url, modifiedParameters, headers));
			}
		}
		
		
		item.response = responseList;
		collection.info.name = collection.info.name + " " + LocalDateTime.now(); 
		
		return collection;
	}
	
	
	private boolean[][] scenarioTable(int dataMapSize) {
		pNum = dataMapSize;
		lenght = (int) Math.pow(2, pNum);
		boolean[][] arr = new boolean[pNum][lenght];

		int space = 0;
		int counter = 0;
		boolean lastVal = true;
		for (int i = 0; i < pNum; i++) {
			space = (int) Math.pow(2, i);
			counter = 1;
			lastVal = true;
			for (int j = 0; j < lenght; j++) {
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
		
		
		return arr;
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
	
	
	private PostmanResponse handleCollectionRequestRaw_Text(int count, String testCase, String paramName, 
			String url, Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException {

		//start delay
		try {
			TimeUnit.SECONDS.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String concatData = dataMap.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
		System.out.println(concatData);
		
		HttpEntity<String> entity = new HttpEntity<String>(concatData, headers);		
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		
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
		postmanResponse.header = new ArrayList<PostmanHeader>();
		
		/**
		 * As I need change the data for each body, I need to clone the original object
		 * otherwise, every time I modify the original reference
		 */
		PostmanRequest newReq = (PostmanRequest) item.request.clone();
		PostmanBody newBody = (PostmanBody) item.request.body.clone();
		newBody.raw = concatData;
		newReq.body = newBody;
		postmanResponse.originalRequest = newReq;
		
		return postmanResponse;
	}
	
	
	
	
	
	public ResponseEntity<String> test() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type","password");
		map.add("username","user@name.com");
		map.add("password","123456");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

		return restTemplate.postForEntity("http://localhost:8080/xform", entity, String.class);
		
	}
}