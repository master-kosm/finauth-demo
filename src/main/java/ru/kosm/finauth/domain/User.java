package ru.kosm.finauth.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** User is an authorization subject
 * 
 * @author kosm
 */
public class User {
	
	private final String userId;
	private String login;
	private String firstName;
	private String lastName;
	private final List<String> accountIds = new ArrayList<>();
	
	public User() {
		userId = UUID.randomUUID().toString();
	}
	
	public String getUserId() {
		return userId;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public List<String> getAccountIds() {
		return accountIds;
	}

}
