package ru.kosm.finauth.core.activity;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.Operation;
import ru.kosm.finauth.domain.User;

/**
 * Activity adding a new user
 * 
 * @author kosm
 */
public class AddUserActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(AddUserActivity.class);

	@Override
	public void execute(AppContext appContext, Operation operation,
			Map<String, Object> operOutput) throws ActivityException {
		User user = new User();
		user.setFirstName((String)Objects.requireNonNull(operation.getContext().get("firstName"), "No first name"));
		user.setLastName((String)Objects.requireNonNull(operation.getContext().get("lastName"), "No last name"));
		user.setLogin((String)Objects.requireNonNull(operation.getContext().get("login"), "No login"));
		operation.getContext().put("user", user);
		operOutput.put("userId", user.getUserId());
		operation.getCacheActions().add((a, o) -> a.getUserCache().put(user.getUserId(), user));
		
		logger.trace("Added user with id {}", user.getUserId());
	}
	
}
