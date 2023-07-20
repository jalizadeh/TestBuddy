package com.jalizadeh.testbuddy.exception;

import com.jalizadeh.testbuddy.model.ResponseError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorController {

    private Logger logger = LogManager.getLogger(ErrorController.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseError> handleException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        logger.error(exception.getMessage());
        ResponseError error = new ResponseError("invalid_request", "The request is not valid.");

        if(exception.getMessage().contains("not one of the values accepted for Enum class")){
            error.setError_message("One or more of the filters is not valid");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.badRequest().body(error);
    }

}
