package ru.kosm.finauth.core;

/** The exception class that may be thrown by an activity during the execution
 * 
 * @author kosm
 */
public class ActivityException extends Exception {

	private static final long serialVersionUID = -415408540092937207L;
	
	public ActivityException(String message) {
		super(message);
	}
	
	public ActivityException(String message, Throwable cause) {
		super(message, cause);
	}

}
