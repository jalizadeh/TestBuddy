package com.jalizadeh.TestBuddy.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalizadeh.TestBuddy.central.FiltersManager;
import com.jalizadeh.TestBuddy.central.StatisticsManager;
import com.jalizadeh.TestBuddy.interfaces.ServiceRequest;
import com.jalizadeh.TestBuddy.interfaces.iFilter;
import com.jalizadeh.TestBuddy.model.PostmanCollection;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanParameter;
import com.jalizadeh.TestBuddy.model.PostmanParameterDescriptionJSON;
import com.jalizadeh.TestBuddy.model.PostmanResponse;
import com.jalizadeh.TestBuddy.types.FilterTarget;

@Service
public class RestService {

	private int delay = 0; // optional delay between each request (during test)

	private int pNum; // number of parameters in the request's body
	private int lenght;
	
	private List<PostmanResponse> responseList;
	private ServiceRequest request;

	/**
	 * Total cases: (2^p-1)*scenarios + 1 (ok case which is only 1)
	 * 
	 * P = 3 (user, pass, grant) scenarios = 3 (empty, invalid, missing) => 7 * 3 +
	 * 1 = 22 test case
	 */
	public PostmanCollection parseCollection(PostmanCollection collection, Optional<Integer> inDelay)
			throws Exception, CloneNotSupportedException {

		// optional delay between requests
		delay = (int) inDelay.orElse(0);
		
		StatisticsManager statMng = StatisticsManager.getInstance();

		for (PostmanItem item : collection.item.get(0).item) {
			
			// to capture the time for Statistics > totalTimeMs
			Instant startTime = Instant.now();
				
			responseList = new ArrayList<PostmanResponse>();
			request = new ServiceRequest(item.name, item.request.getFullUrl(), item.request.method,
					item.request.getBodyMode());

			// having body as Map is easier to apply filters on the parameters
			Map<String, String> dataMap = item.request.getDataMap();

			// set headers, if exists
			if (item.request.getHeaders().size() > 0)
				request.setHeaders(extractHttpHeader(item.request.getHeaders()));

			// set request body data, if exists
			if (dataMap.size() > 0)
				request.setData(item.request.getData());
			
			
			Map<String, PostmanParameter> queries = item.request.url.getQueries();
			if(queries.size() > 0)
				request.setQueries(queries);

			/*
			 * System.out.println(httpMethod + "\n" + url + "\n" + header.size() + "\n" +
			 * dataMap.size()); for (Entry<String, String> entry : dataMap.entrySet()) {
			 * System.out.println(entry.getKey() + "\t" + entry.getValue().toString()); }
			 */

			// 200 OK (positive!) or the request as it is, without any change
			responseList.add(request.handleRequest(item, 0, "OK", "", dataMap));
			
			applyParameterFiltersOn(FilterTarget.BODY, item, dataMap);
			
			applyParameterFiltersOn(FilterTarget.HEADER, item, item.request.getHeaders(true));
			
			//Add all responses of that request to the Postman collection
			item.response = responseList;

			Instant endTime = Instant.now();
			statMng.addTime(Duration.between(startTime, endTime).toMillis());
		}

		collection.info.name = collection.info.name + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		return collection;
	}

	private void applyParameterFiltersOn(FilterTarget target, PostmanItem item, Map<String, String> dataMap) throws Exception {
		// T/F table of possibilities / cases
		boolean[][] scenarioTable = scenarioTable(dataMap.size());

		String[] dataArr = new String[dataMap.size()];
		int dataCount = 0;
		for (Entry<String, String> entry : dataMap.entrySet()) {
			dataArr[dataCount++] = entry.getKey();
			// System.out.println("OK\t" + entry.getKey() + "\t" + entry.getValue().toString());
		}

		// Not all requests have body to apply filters on them
		if (dataMap.size() > 0 && FiltersManager.getInstance().getFilters().size() > 0) {
			// List of filters should provided in the request's body
			List<iFilter> filters = FiltersManager.getInstance().getFilters();

			List<String> parameterName = new ArrayList<String>();
			Map<String, String> modifiedParameters = new HashMap<>();

			for (iFilter filter : filters) {
				for (int j = 1; j < lenght; j++) {
					parameterName.clear();
					modifiedParameters.clear();
					
					//TODO: improve
					if(target.equals(FilterTarget.HEADER)) {
						modifiedParameters.putAll(item.request.getHeaders());
					} else {
						modifiedParameters.putAll(dataMap);
					}

					for (int i = 0; i < pNum; i++) {
						if (!scenarioTable[i][j]) {
							parameterName.add(dataArr[i]);
							modifiedParameters = filter.applyFilter(modifiedParameters, dataArr[i]);
						}
					}

					String paramNames = String.join(",", parameterName);
					
					if(target.equals(FilterTarget.HEADER)) {
						request.setHeaders(extractHttpHeader(modifiedParameters));
					}
					
					responseList.add(request.handleRequest(item, j, filter.getFilterName().toString(), paramNames, modifiedParameters));
				}
			}
		}
	}

	private HttpHeaders extractHttpHeader(Map<String, String> headers) {
		HttpHeaders httpHeaders = new HttpHeaders();
		for (Entry<String, String> e : headers.entrySet()) {
			httpHeaders.add(e.getKey(), e.getValue());
		}
		return httpHeaders;
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

		/*
		System.out.print("\t");
		for (int j = 0; j < lenght; j++) {
			System.out.print(j + "\t");
		}
		System.out.println();
		for (int i = 0; i < pNum; i++) {
			System.out.print(i + ":\t");
			for (int j = 0; j < lenght; j++) {
				System.out.print(arr[i][j] + "\t");
			}

			System.out.println();
		}
		*/
		
		return arr;
	}
}