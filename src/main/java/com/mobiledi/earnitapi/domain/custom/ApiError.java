package com.mobiledi.earnitapi.domain.custom;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ApiError {

	private HttpStatus status;
	private int code;
	private List<String> message;

	public ApiError(HttpStatus status, int code, List<String> message) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public ApiError(HttpStatus status, int code, String message) {
		super();
		this.status = status;
		this.code = code;
		this.message = Arrays.asList(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}
}