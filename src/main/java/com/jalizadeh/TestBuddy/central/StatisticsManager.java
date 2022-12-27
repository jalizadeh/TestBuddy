package com.jalizadeh.testbuddy.central;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jalizadeh.testbuddy.statistics.StatReport;
import com.jalizadeh.testbuddy.statistics.StatRequest;

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
				.map(r -> new StatRequest(r.getName(), r.getMethod(), r.getUrl(), 
						r.getPositive(), r.getNegative(), 
						r.getStatus().stream().sorted().collect(Collectors.toList())))
				.collect(Collectors.toList());
	}


	public void addStat(String name, String method, String url, String string, boolean isPos) {
		
		this.totalCalls++ ;
		
		if(isPos) 	this.totalPositive++;
		else 		this.totalNegative++;
		
		Map<String, Request> reqMap = this.request;
		if(!reqMap.containsKey(name)) {
//			reqMap.get(name).setPosNeg(isPos);
			reqMap.put(name, new Request(name, method, url,0,0, null));
		}
		reqMap.get(name).setPosNeg(isPos);
		reqMap.get(name).setStatusCode(string);
		
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
	private Set<String> status;
	
	public void setPosNeg(boolean isPos) {
		if(isPos) 	positive++;
		else 		negative++;
	}

	public void setStatusCode(String statusCode) {
		if(status == null) {
			status = new HashSet<>();
		}
		
		status.add(statusCode);
	}
}
