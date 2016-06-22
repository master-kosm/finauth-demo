package ru.kosm.finauth.domain;

import java.util.UUID;

/** Account attached to the user
 * 
 * @author kosm
 */
public class Account {
	
	private final String accountId;
	private final String userId; 
	private long balance;
	
	public Account(String userId) {
		this.accountId = UUID.randomUUID().toString();
		this.userId = userId; 
	};
	
	public String getAccountId() {
		return accountId;
	}
	public String getUserId() {
		return userId;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}

}
