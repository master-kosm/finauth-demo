package ru.kosm.finauth.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgroups.util.UUID;

import ru.kosm.finauth.domain.User;

public class UserManager {
	
	private final AppContext appContext;
	
	private final static transient Logger logger = LogManager.getLogger(UserManager.class);
	
	public UserManager(final AppContext appContext) {
		this.appContext = appContext;
	}
	
	public User addUser(String firstName, String lastName, String login) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setLogin(login);
		user.setId(UUID.randomUUID().toString());
		appContext.getUserCache().put(user.getId(), user);
		logger.trace("Added user with id {}", user.getId());
		return user;
	}

}
