package com.jalizadeh.TestBuddy.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatReport {

	private long totalTimeMs;
	private int totalRequests;
	private int totalCalls;
	private int totalPositive;
	private int totalNegative;
	
}
