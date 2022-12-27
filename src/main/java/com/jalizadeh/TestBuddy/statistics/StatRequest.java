package com.jalizadeh.testbuddy.statistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatRequest {

		private String name;
		private String method;
		private String url;
		private int positive;
		private int negative;
		private List<String> status;
		
}
