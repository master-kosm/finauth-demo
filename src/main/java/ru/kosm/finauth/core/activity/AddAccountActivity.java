package ru.kosm.finauth.core.activity;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.Account;
import ru.kosm.finauth.domain.Operation;
import ru.kosm.finauth.domain.User;

/**
 * Activity adding new account to an existing user 
 * 
 * @author kosm
 */
public class AddAccountActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(AddAccountActivity.class);

	@Override
	public void execute(AppContext appContext, Operation operation, Map<String, Object> operOutput)
			throws ActivityException {
		User user = (User)Objects.requireNonNull(operation.getContext().get("user"), "No such user");
		Account account = new Account(user.getUserId());
		user.getAccountIds().add(account.getAccountId());
		operOutput.put("accountId", account.getAccountId());
		operation.getCacheActions().add((c, o) -> c.getAccountCache().put(account.getAccountId(), account));

		logger.trace("Added account with id {} to user with id {}", account.getAccountId(), user.getUserId());
	}

}
