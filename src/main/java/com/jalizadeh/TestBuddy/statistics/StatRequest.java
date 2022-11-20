package com.jalizadeh.TestBuddy.statistics;

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
		
}
