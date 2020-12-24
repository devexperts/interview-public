package com.devexperts.account;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class Account {
    private final long ID;
    
    @NotBlank
    private final String firstName;
    
    @NotBlank
    private final String lastName;
    
    private Double balance;
    
    public void increaseBalance(double balance) {
    	this.balance += balance;
    }
    
    public void decreaseBalance(double balance) {
    	this.balance -= balance;
    }

	public void setBalance(Double balance) {
		this.balance = balance;
	}
}
