package com.jalizadeh.testbuddy.model;

import com.jalizadeh.testbuddy.interfaces.iTestCase;

public class TestCase implements iTestCase{

	private boolean status;

	public TestCase( boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return this.status;
	}
	
}
