package com.jalizadeh.TestBuddy.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.jalizadeh.TestBuddy.filter.EmptyFilter;
import com.jalizadeh.TestBuddy.filter.InvalidFilter;
import com.jalizadeh.TestBuddy.filter.MissingFilter;
import com.jalizadeh.TestBuddy.filter.RandomFilter;
import com.jalizadeh.TestBuddy.interfaces.RequestPostmanAbstract;
import com.jalizadeh.TestBuddy.interfaces.iFilter;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.requestImpl.RequestRawText;
import com.jalizadeh.TestBuddy.requestImpl.RequestUrlencodedText;

@Service
public class RestService {

	private int delay = 0; //optional delay between each request (during test)
	
	private int pNum; //number of parameters in the request's body 
	private int lenght;
	
	

	/**
	 * Total cases:
	 * (2^p-1)*scenarios + 1 (ok case which is only 1)
	 * 
	 * P = 3 (user, pass, grant)
	 * scenarios = 3 (empty, invalid, missing)
	 * => 7 * 3 + 1 = 22 test case
	 */
	public PostmanCollection parseCollection(PostmanCollection collection, Optional<Integer> inDelay) 
			throws Exception, CloneNotSupportedException {
		
		//optional delay between requests
		delay = (int) inDelay.orElse(0); 
		
		for(PostmanItem item : collection.item.get(0).item) {

			RequestPostmanAbstract request = null;
			List<PostmanResponse> responseList = new ArrayList<PostmanResponse>();
			
			String url = item.request.url.raw;
			String httpMethod = item.request.method;

			//how data should be parsed and sent to endpoint
			String bodyMode = item.request.body.mode;
			String bodyOptions = item.request.body.options.raw.language;
			String dataType = bodyMode + "-" + bodyOptions;
			
			// set headers
			HttpHeaders headers = new HttpHeaders();
			for(Entry<String, String> e : item.request.getHeaders().entrySet()) {
				headers.add(e.getKey(), e.getValue());
			}
			
			
			String data = item.request.getData();
			Map<String, String> dataMap = new HashMap<String, String>();
			
			
			String[] dataPair = data.split("&");
			for(String dp : dataPair) {
				String[] d = dp.split("=");
				dataMap.put(d[0], d[1]);
			}
			
			
			/*
			System.out.println(httpMethod + "\n" + url + "\n" + header.size() + "\n" + dataMap.size());
			for (Entry<String, String> entry : dataMap.entrySet()) {
				System.out.println(entry.getKey() + "\t" + entry.getValue().toString());
			}
			*/
			
			// T/F table of possibilites / cases
			boolean[][] scenarioTable = scenarioTable(dataMap.size());
			
			String[] dataArr = new String[dataMap.size()]; 
			int dataCount = 0;
			System.err.println("Using Array : \n");
			for (Entry<String, String> entry : dataMap.entrySet()) {
				dataArr[dataCount++] = entry.getKey();
				System.out.println("OK\t" + entry.getKey() + "\t" + entry.getValue().toString());
			}
			
			
			switch (dataType) {
				case "raw-text":
					request = new RequestRawText();
					break;
				case "urlencoded-text":
					request = new RequestUrlencodedText();
					break;
				default:
					throw new Exception("Request type is not correct / supported yet");
			}
		
			
			//run first case which all are OK (the only test with all correct parameters)
			responseList.add(request.handleRequest(item, 0, "OK", "", url, dataMap, headers));
			
			
			List<iFilter> filters = new ArrayList<iFilter>();
			filters.add(new EmptyFilter());
			filters.add(new InvalidFilter());
			filters.add(new MissingFilter());
			filters.add(new RandomFilter());

			List<String> parameterName = new ArrayList<String>();
			Map<String, String> modifiedParameters = new HashMap<>();
			
			for(iFilter filter : filters) {
				for(int j = 1; j < lenght; j++) {
					parameterName.clear();
					modifiedParameters.clear();
					modifiedParameters.putAll(dataMap);
					
					for(int i = 0; i < pNum; i++) {
						if(!scenarioTable[i][j]) {
							parameterName.add(dataArr[i]);
							modifiedParameters = filter.applyFilter(modifiedParameters, dataArr[i]);
						}
					}
					
					String paramNames = String.join(",", parameterName);
					responseList.add(request.handleRequest(item, j, filter.getFilterName(), paramNames, url, modifiedParameters, headers));
				}
			}
			
			item.response = responseList;
			collection.info.name = collection.info.name + " " + LocalDateTime.now(); 
		}
		
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
}