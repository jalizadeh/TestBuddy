package com.jalizadeh.TestBuddy.central;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.jalizadeh.TestBuddy.statistics.StatReport;
import com.jalizadeh.TestBuddy.statistics.StatRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class StatisticsManager {
	
	private static StatisticsManager instance;
	
	private long totalTimeMs;
	private int totalRequests;
	private int totalCalls;
	private int totalPositive;
	private int totalNegative;
	private Map<String, Request> request = new HashMap<>();
	
	
	private StatisticsManager() { }
	
	
	public static StatisticsManager getInstance() {
		if(instance == null)
			instance = new StatisticsManager();
		
		return instance;
	}


	public StatReport getReport(String collectionName) {
		this.totalRequests = this.request.size();
		StatReport report = new StatReport(collectionName, this.totalTimeMs,this.totalRequests,
					this.totalCalls, this.totalPositive, this.totalNegative, mapToList(this.request));
		resetStat();
		return report;
	}


	private List<StatRequest> mapToList(Map<String, Request> req) {
		return req.values().stream()
				.sorted((a,b) -> a.getName().compareTo(b.getName()))
				.map(r -> new StatRequest(r.getName(), r.getMethod(), r.getUrl(), r.getPositive(), r.getNegative()))
				.collect(Collectors.toList());
	}


	public void addStat(String name, String method, String url, boolean isPos) {
		
		this.totalCalls++ ;
		
		if(isPos) 	this.totalPositive++;
		else 		this.totalNegative++;
		
		if(this.request.containsKey(name)) {
			this.request.get(name).setPosNeg(isPos);
		} else {
			this.request.put(name, new Request(name, method, url,0,0));
			this.request.get(name).setPosNeg(isPos);
		}
	}
	
	
	private void resetStat() {
		this.totalTimeMs = 0;
		this.totalRequests = 0;
		this.totalCalls = 0;
		this.totalPositive = 0;
		this.totalNegative = 0;
		this.request.clear();
	}


	public void addTime(long millis) {
		this.totalTimeMs += millis;
	}
}



@Data
@AllArgsConstructor
class Request {
	private String name;
	private String method;
	private String url;
	private int positive;
	private int negative;
	
	public void setPosNeg(boolean isPos) {
		if(isPos) 	positive++;
		else 		negative++;
	}
}
