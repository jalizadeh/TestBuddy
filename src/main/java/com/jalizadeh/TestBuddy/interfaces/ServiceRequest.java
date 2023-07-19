package com.jalizadeh.testbuddy.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.testbuddy.central.FiltersManager;
import com.jalizadeh.testbuddy.central.RequestFactory;
import com.jalizadeh.testbuddy.central.StatisticsManager;
import com.jalizadeh.testbuddy.model.postman.PostmanItem;
import com.jalizadeh.testbuddy.model.postman.PostmanRequest;
import com.jalizadeh.testbuddy.model.postman.PostmanResponse;
import com.jalizadeh.testbuddy.model.postman.PostmanUrl;
import com.jalizadeh.testbuddy.types.FilterTarget;

public class ServiceRequest extends RequestAbstract {

	@Autowired
	RequestFactory requestFactory;

	private int pNum; // number of parameters in the request's body
	private int lenght;

	private final PostmanItem item;
	private final String name;
	private String url;
	private final String method;
	private final String bodyMode;
	private HttpEntity<?> entity;
	private String queries;
	private Map<String, String> queriesNoDescMap;
	private HttpHeaders headers;
	private String data;
	private Map<String, String> bodyDataMap;
	
	private boolean hasQuery = false;
	private boolean hasHeader = false;
	private boolean hasBodyData = false;

	public ServiceRequest(PostmanItem item) {
		this.item = item;
		this.requestFactory = new RequestFactory();
		this.name = item.name;
		this.url = item.request.getFullUrl();
		this.method = item.request.method;
		this.bodyMode = item.request.getBodyMode();
		this.entity = new HttpEntity<>(null);
		this.queries = "";
		this.queriesNoDescMap = new HashMap<>();
		this.bodyDataMap = new HashMap<>();

		// set request query, if exists
		if (item.request.url.getQueriesNoDescMap().size() > 0)
			setQueries(item.request.url);

		// set headers, if exists
		if (item.request.getHeaders().size() > 0)
			setHeaders(mapToHttpHeader(item.request.getHeaders()));

		// set request body data, if exists
		if (item.request.getDataMap().size() > 0)
			setData(item.request);

	}
	
	public boolean hasQuery() {
		return hasQuery;
	}

	public boolean hasHeader() {
		return hasHeader;
	}

	public boolean hasBodyData() {
		return hasBodyData;
	}


	private ServiceRequest setQueries(PostmanUrl url) {
		this.hasQuery = true;
		this.queries = url.getQueriesAsString();
		this.queriesNoDescMap = url.getQueriesNoDescMap();
		return this;
	}

	private ServiceRequest setHeaders(HttpHeaders headers) {
		this.hasHeader = true;
		this.headers = headers;
		this.entity = new HttpEntity<>(this.headers);
		return this;
	}
	
	public ServiceRequest setData(Map<String, String> dataMap) {
		this.data = mapToString(dataMap);
		return setDataHeader(this.data, this.headers);
	}

	private ServiceRequest setData(PostmanRequest request) {
		this.hasBodyData = true;
		this.data = request.getData();
		this.bodyDataMap = request.getDataMap();
		return setDataHeader(this.data, this.headers);
	}

	private ServiceRequest setDataHeader(String data, HttpHeaders headers) {
		this.headers = headers;
		this.data = data;
		this.entity = new HttpEntity<>(this.data, this.headers);
		return this;
	}

	/**
	 * The only handler that sends the request using the <i>dataMap</i>. Before
	 * using this method, the request (<i>this</i>) should be instantiated correctly
	 */
	public PostmanResponse sendRequest(int count, String testCase, String parameterName) 
			throws Exception {

		ResponseEntity<String> response = null;

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
			default:
				throw new Exception("Request could not be initialized. It can be due to not supported HTTP method");
		}

		StatisticsManager statMng = StatisticsManager.getInstance();
		statMng.addStat(this.name, this.method, this.url, response.getStatusCode().toString(),
					response.getStatusCode().is2xxSuccessful());

		//Based on the item's body data, the appropriate request handler is selected
		RequestPostmanAbstract request = requestFactory.getRequest(this.bodyMode);

		PostmanResponse initializeResponse = request.initializeResponseObject(item, count, testCase, parameterName,
				this.queriesNoDescMap, this.bodyDataMap, headers, response);
		return request.handleResponseBody(item, initializeResponse, this.queriesNoDescMap, this.bodyDataMap, headers);
	}

	private String getUrl() {
		return this.url + (this.queries.length() > 0 ? "?" + this.queries : "");
	}

	
	public List<PostmanResponse> applyParameterFiltersOn(FilterTarget target) throws Exception {
		List<PostmanResponse> generatedResponses = new ArrayList<>();
		Map<String, String> dataMap = new HashMap<>();
		
		switch (target) {
			case QUERY:
				dataMap = this.queriesNoDescMap;
				break;
			case HEADER:
				dataMap = (this.headers == null ? null : this.item.request.getHeaders(true));
				break;
			case BODY:
				dataMap = this.bodyDataMap;
				break;
			default:
				return new ArrayList<>();
		}
		
		// T/F table of possibilities / cases
		boolean[][] scenarioTable = scenarioTable(dataMap.size());

		int dataCount = 0;
		String[] dataArr = new String[dataMap.size()];
		for (Entry<String, String> entry : dataMap.entrySet()) {
			dataArr[dataCount++] = entry.getKey();
			// System.out.println("OK\t" + entry.getKey() + "\t" + entry.getValue().toString());
		}

		//if only the target has item to apply filter on it and at least, a filter is selected
		if ( !dataMap.isEmpty() && !FiltersManager.getInstance().getFilters().isEmpty() ) {
			// List of filters should provided in the request's body
			List<FilterAbstract> filters = FiltersManager.getInstance().getFilters();

			List<String> testCaseParametersList = new ArrayList<String>();
			Map<String, String> modifiedParametersMap = new HashMap<>();

			for (FilterAbstract filter : filters) {
				for (int testCount = 1; testCount < lenght; testCount++) {
					testCaseParametersList.clear();
					modifiedParametersMap.clear();
					modifiedParametersMap.putAll(dataMap);
					
					for (int parameterCount = 0; parameterCount < pNum; parameterCount++) {
						if (!scenarioTable[parameterCount][testCount]) {
							testCaseParametersList.add(dataArr[parameterCount]);
							if (target.equals(FilterTarget.QUERY)) {
								modifiedParametersMap = filter.applyFilter(modifiedParametersMap, dataArr[parameterCount],
										item.request.url.getQueriesFullMap().get(dataArr[parameterCount]).descJson);
							} else {
								modifiedParametersMap = filter.applyFilter(modifiedParametersMap, dataArr[parameterCount]);
							}
						}
					}

					String testCaseParametersName = String.join(",", testCaseParametersList);

					if (target.equals(FilterTarget.HEADER)) {
						setHeaders(mapToHttpHeader(modifiedParametersMap));
					} else if (target.equals(FilterTarget.QUERY)) {
						this.queries = mapToString(modifiedParametersMap);
					} else {
						setData(modifiedParametersMap);
					}

					generatedResponses.add(sendRequest(testCount, filter.getFilterName().toString(), testCaseParametersName));
				}
			}
		}

		return generatedResponses;
	}

	
	/**
	 * Total cases: (2^p-1)*scenarios + 1 (OK case which is only 1)
	 * 
	 * P = 3 (user, pass, grant) scenarios = 3 (empty, invalid, missing) => 7 * 3 +
	 * 1 = 22 test case
	 */
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
		 * System.out.print("\t"); for (int j = 0; j < lenght; j++) { System.out.print(j
		 * + "\t"); } System.out.println(); for (int i = 0; i < pNum; i++) {
		 * System.out.print(i + ":\t"); for (int j = 0; j < lenght; j++) {
		 * System.out.print(arr[i][j] + "\t"); }
		 * 
		 * System.out.println(); }
		 */

		return arr;
	}

}
