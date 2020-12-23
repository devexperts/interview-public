package com.devexperts.exception;

public class AccountNotRegisteredException extends RuntimeException {

	private static final long serialVersionUID = -4875504762856220676L;
	
	public AccountNotRegisteredException(String message) {
		super(message);
	}

}
