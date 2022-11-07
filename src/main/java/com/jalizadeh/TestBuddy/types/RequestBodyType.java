package com.jalizadeh.TestBuddy.types;

public enum RequestBodyType {

	RAW_TEXT {
		public String type() { return "raw-text"; }
	},
	URLENCODEC_TEXT {
		public String type() { return "urlencoded-text"; }
	};

	
	public abstract String type();
}
