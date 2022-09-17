package com.jalizadeh.TestBuddy.model;

import com.jalizadeh.TestBuddy.interfaces.iTestCase;

public class TestCase implements iTestCase{

	private boolean status;

	public TestCase( boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return this.status;
	}
	
}
