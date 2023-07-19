package com.jalizadeh.testbuddy.model.postman;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jalizadeh.testbuddy.types.PostmanParameterDescriptionType;

/**
 * This model expresses the details of the parameter's <i>key</i>
 * which is necessary for the processor to choose the appropriate filter's settings
 * based on the <i>key</i>'s structure
 */
public class PostmanParameterDescriptionJSON {

	@JsonProperty("type")
	public PostmanParameterDescriptionType type;
	
}
