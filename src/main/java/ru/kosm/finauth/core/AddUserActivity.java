package ru.kosm.finauth.core;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgroups.util.UUID;

import ru.kosm.finauth.domain.User;

/**
 * Activity adding a new user
 * 
 * @author kosm
 */
public class AddUserActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(AddUserActivity.class);

	@Override
	public void execute(AppContext appContext, Map<String, Object> operContext,
			Map<String, Object> operOutput) throws ActivityException {
		User user = new User();
		user.setFirstName((String)Objects.requireNonNull(operContext.get("firstName"), "No first name"));
		user.setLastName((String)Objects.requireNonNull(operContext.get("lastName"), "No last name"));
		user.setLogin((String)Objects.requireNonNull(operContext.get("login"), "No login"));
		user.setId(UUID.randomUUID().toString());
		operContext.put("user", user);
		appContext.getUserCache().put(user.getId(), user);
		operOutput.put("userId", user.getId());
		
		logger.trace("Added user with id {}", user.getId());
	}
	
}
