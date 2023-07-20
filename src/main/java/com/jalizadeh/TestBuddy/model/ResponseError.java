package com.jalizadeh.testbuddy.model;

import lombok.Data;

@Data
public class ResponseError {

    private String error;
    private String error_message;


    public ResponseError(String error, String error_message) {
        this.error = error;
        this.error_message = error_message;
    }

}
