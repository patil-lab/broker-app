package com.prive.broker.constants;

import org.springframework.http.HttpStatus;

public enum BrokerServiceError {
	UNKNOWN(0, "unknown error", HttpStatus.BAD_REQUEST),
	NO_RECORD_FOUND(1001,"No record found",HttpStatus.BAD_REQUEST);

	private final int errorCode;
	private final String message;
	private final HttpStatus httpStatus;

	BrokerServiceError(int errorCode , String message, HttpStatus status){
		this.message=message;
		this.errorCode=errorCode;
		this.httpStatus=status;
	}

	public String getMessage(){
		return this.message;
	}

	public int getErrorCode(){
		return this.errorCode;
	}

	public HttpStatus getHttpStatus(){
		return this.httpStatus;
	}
}
