package com.devexperts.service.exception;

public class HttpParametersException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HttpParametersException() {
		super();
	}

	public HttpParametersException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public HttpParametersException(final String message) {
		super(message);
	}

	public HttpParametersException(final Throwable cause) {
		super(cause);
	}
}
