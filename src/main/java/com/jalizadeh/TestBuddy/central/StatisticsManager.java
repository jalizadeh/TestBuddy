package com.jalizadeh.TestBuddy.central;

import com.jalizadeh.TestBuddy.statistics.StatReport;

import lombok.Getter;

@Getter
public class StatisticsManager {
	
	private static StatisticsManager instance;
	
	private long totalTimeMs;
	private int totalRequests;
	private int totalCalls;
	private int totalPositive;
	private int totalNegative;
	
	
	private StatisticsManager() { }
	
	
	public static StatisticsManager getInstance() {
		if(instance == null)
			instance = new StatisticsManager();
		
		return instance;
	}


	public StatReport getReport() {
		StatReport report = new StatReport(this.totalTimeMs,this.totalRequests,
					this.totalCalls, this.totalPositive, this.totalNegative );
		resetStat();
		return report;
	}


	public void addStat(boolean isPos) {
		this.totalRequests++;
		this.totalCalls++ ;
		
		if(isPos) 	this.totalPositive++;
		else 		this.totalNegative++;
	}
	
	
	private void resetStat() {
		this.totalTimeMs = 0;
		this.totalRequests = 0;
		this.totalCalls = 0;
		this.totalPositive = 0;
		this.totalNegative = 0;
	}


	public void addTime(long millis) {
		this.totalTimeMs += millis;
	}
}
