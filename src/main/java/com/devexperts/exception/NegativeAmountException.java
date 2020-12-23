package com.devexperts.exception;

public class NegativeAmountException extends RuntimeException {

	private static final long serialVersionUID = 3414091812324627216L;

	public NegativeAmountException(String message) {
		super(message);
	}

}
