package ru.kosm.finauth.core;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.domain.User;

/**
 * Activity getting the user information by userId
 * 
 * @author kosm
 */
public class GetUserActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(GetUserActivity.class);

	@Override
	public void execute(AppContext appContext, Map<String, Object> operContext, Map<String, Object> operOutput)
			throws ActivityException {
		User user = Objects.requireNonNull(appContext.getUserCache().get(operContext.get("userId")), "No such user");
		operOutput.put("user", user);
		
		logger.trace("Got user with id {} and account Ids {}", user.getId(), user.getAccountIds());
	}

}
