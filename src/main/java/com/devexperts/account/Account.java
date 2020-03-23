package com.devexperts.account;

public class Account {
	
    private final AccountKey accountKey;
    private final String firstName;
    private final String lastName;
    private Double balance;

    public Account(AccountKey accountKey, String firstName, String lastName, Double balance) {
        this.accountKey = accountKey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public AccountKey getAccountKey() {
        return accountKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getBalance() {
        return balance;
    }

    //Only this method is not thread safe as all other fields are immutable
    public void setBalance(Double balance) {
        this.balance = balance;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountKey == null) ? 0 : accountKey.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountKey == null) {
			if (other.accountKey != null)
				return false;
		} else if (!accountKey.equals(other.accountKey))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		return true;
	}
    
    
}
