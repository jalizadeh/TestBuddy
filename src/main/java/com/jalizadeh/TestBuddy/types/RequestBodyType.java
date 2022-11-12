package com.jalizadeh.TestBuddy.types;

public enum RequestBodyType {

	RAW_TEXT {
		public String type() { return "raw"; }
	},
	URLENCODEC_TEXT {
		public String type() { return "urlencoded"; }
	};

	
	public abstract String type();
}
