package com.devexperts.exception;

public class NotEnoughAmountException extends RuntimeException{

	private static final long serialVersionUID = 8014104003977157184L;

	public NotEnoughAmountException(String message) {
		super(message);
	}

}
