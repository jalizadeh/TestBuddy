package com.jalizadeh.testbuddy.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jalizadeh.testbuddy.central.StatisticsManager;
import com.jalizadeh.testbuddy.interfaces.ServiceRequest;
import com.jalizadeh.testbuddy.model.postman.PostmanCollection;
import com.jalizadeh.testbuddy.model.postman.PostmanItem;
import com.jalizadeh.testbuddy.model.postman.PostmanResponse;
import com.jalizadeh.testbuddy.types.FilterTarget;

@Service
public class RestService {

	private int delay = 0; // optional delay between each request (during test)
	private List<PostmanResponse> responseList;
	private ServiceRequest request;

	public PostmanCollection parseCollection(PostmanCollection collection, Optional<Integer> inDelay)
			throws Exception, CloneNotSupportedException {

		// optional delay between requests
		delay = (int) inDelay.orElse(0);

		StatisticsManager statMng = StatisticsManager.getInstance();

		for (PostmanItem item : collection.item.get(0).item) {

			// to capture the time for Statistics > totalTimeMs
			Instant startTime = Instant.now();

			responseList = new ArrayList<PostmanResponse>();
			request = new ServiceRequest(item);

			// 200 OK (positive!) or the request as it is, without any change
			responseList.add(request.sendRequest(0, "OK", ""));
			
			if(request.hasQuery())
				responseList.addAll(request.applyParameterFiltersOn(FilterTarget.QUERY));
			
			if(request.hasHeader())
				responseList.addAll(request.applyParameterFiltersOn(FilterTarget.HEADER));
			
			if(request.hasBodyData())
				responseList.addAll(request.applyParameterFiltersOn(FilterTarget.BODY));

			
			// Add all responses of that request to the Postman collection
			item.response = responseList;

			Instant endTime = Instant.now();
			statMng.addTime(Duration.between(startTime, endTime).toMillis());
		}

		collection.info.name = collection.info.name + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		return collection;
	}

}