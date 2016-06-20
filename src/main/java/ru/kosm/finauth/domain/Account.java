package ru.kosm.finauth.domain;

/** Account with separate balance attached to the user
 * 
 * @author kosm
 */
public class Account {
	
	private String accountId;
	private String userId; 
	private long balance;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	

}
