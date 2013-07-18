package com.ruleoftech.lab.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Exception ex) {
		super(message, ex);
	}

}
