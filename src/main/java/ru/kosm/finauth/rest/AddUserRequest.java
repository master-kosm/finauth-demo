package ru.kosm.finauth.rest;

/** POJO for "Add new user" request
 * 
 * @author kosm
 */
public class AddUserRequest {
	private String firstName;
	private String lastName;
	private String login;
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getLogin() {
		return login;
	}
}
