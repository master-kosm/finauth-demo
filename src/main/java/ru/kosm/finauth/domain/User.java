package ru.kosm.finauth.domain;

import java.util.ArrayList;
import java.util.List;

/** User is an authorization subject
 * 
 * @author kosm
 */
public class User {
	
	private String id;
	private String login;
	private String firstName;
	private String lastName;
	private final List<String> accountIds = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
