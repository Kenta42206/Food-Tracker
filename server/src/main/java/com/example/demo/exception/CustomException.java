package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String message;
	private final HttpStatus httpStatus;
	private final String title;

	public CustomException( String title, String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
		this.title = title;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getTitle() {
		return title;
	}

}
