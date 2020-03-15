package com.devexperts.service.exception;

public class UserAccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserAccountNotFoundException() {
		super();
	}

	public UserAccountNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UserAccountNotFoundException(final String message) {
		super(message);
	}

	public UserAccountNotFoundException(final Throwable cause) {
		super(cause);
	}
}
