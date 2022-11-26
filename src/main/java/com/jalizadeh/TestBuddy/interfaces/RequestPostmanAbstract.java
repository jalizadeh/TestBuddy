package com.jalizadeh.TestBuddy.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.jalizadeh.TestBuddy.model.PostmanHeader;
import com.jalizadeh.TestBuddy.model.PostmanItem;
import com.jalizadeh.TestBuddy.model.PostmanParameter;
import com.jalizadeh.TestBuddy.model.PostmanRequest;
import com.jalizadeh.TestBuddy.model.PostmanResponse;

public abstract class RequestPostmanAbstract extends RequestAbstract {

	public abstract String bodyType();

	public abstract PostmanResponse handleResponseBody(PostmanItem item, PostmanResponse initializedResponse,
			Map<String, String> queryMap, Map<String, String> dataMap, HttpHeaders headers) throws CloneNotSupportedException;

	protected List<PostmanHeader> extractResponseHeader(ResponseEntity<String> response) {
		List<PostmanHeader> responseHeaderList = new ArrayList<PostmanHeader>();
		for (Entry<String, String> e : response.getHeaders().toSingleValueMap().entrySet()) {
			PostmanHeader ph = new PostmanHeader();
			ph.key = e.getKey();
			ph.value = e.getValue();
			responseHeaderList.add(ph);
		}

		return responseHeaderList;
	}

	/**
	 * Initially the original request body is cloned and in each Request Parser, the
	 * extra stuff, can be populated
	 * 
	 * <ul>
	 * <li>RequestNoBody</li>
	 * <li>RequestRawText</li>
	 * <li>RequestUrlEncoded</li>
	 * </ul>
	 */
	protected PostmanResponse initializeResponseObject(PostmanItem item, int testNumber, String testCase, String parameterName,
			Map<String, String> queryMap, Map<String, String> dataMap, HttpHeaders headerMap, ResponseEntity<String> response)
			throws CloneNotSupportedException {
		PostmanResponse generatedResponse = new PostmanResponse();
		PostmanRequest clonedOriginalRequest = (PostmanRequest) item.request.clone();

		generatedResponse.name = response.getStatusCodeValue() + " - " + testCase + " " + parameterName;
		generatedResponse.status = response.getStatusCode().name();
		generatedResponse.code = response.getStatusCodeValue();
		generatedResponse.header = extractResponseHeader(response);
		generatedResponse._postman_previewlanguage = "json";
		generatedResponse.body = response.getBody();
		generatedResponse.originalRequest = clonedOriginalRequest;
		generatedResponse.originalRequest.header = (headerMap == null ? null : convertModifiedMapToPP(headerMap.toSingleValueMap()));
		generatedResponse.originalRequest.url.query = convertModifiedMapToPP(queryMap);

		return generatedResponse;
	}

	// Populate the generated example using response headers received from server
	private List<PostmanParameter> convertModifiedMapToPP(Map<String, String> headers) {
		if (headers != null) {
			List<PostmanParameter> headerList = new ArrayList<>();
			PostmanParameter ph = new PostmanParameter();
			headers.forEach((k, v) -> {
				ph.key = k;
				ph.value = v;
				headerList.add(ph);
			});

			return headerList;
		}

		return null;
	}

}
