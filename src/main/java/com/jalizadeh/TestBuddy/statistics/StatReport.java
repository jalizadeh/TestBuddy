package com.jalizadeh.testbuddy.statistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatReport {

	private String collectionName;
	private long totalTimeMs;
	private int totalRequests;
	private int totalCalls;
	private int totalPositive;
	private int totalNegative;
	private List<StatRequest> requests;
	
}
