package com.castro.drones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class ValidationException extends RuntimeException{
	
	public ValidationException(String message) {
		super(message);
	}
	
	

}
