package com.jalizadeh.testbuddy.types;

public enum RequestBodyType {

	NO_BODY {
		public String type() {return "";}
	},
	RAW_TEXT {
		public String type() { return "raw"; }
	},
	URLENCODEC_TEXT {
		public String type() { return "urlencoded"; }
	};

	
	public abstract String type();
}
