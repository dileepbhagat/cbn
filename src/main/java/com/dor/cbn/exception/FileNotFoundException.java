package com.dor.cbn.exception;

@SuppressWarnings("serial")
public class FileNotFoundException extends RuntimeException {
    private String message;

	public FileNotFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
