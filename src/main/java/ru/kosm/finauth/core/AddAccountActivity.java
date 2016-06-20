package ru.kosm.finauth.core;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.domain.Account;
import ru.kosm.finauth.domain.User;

/**
 * Activity adding new account to an existing user 
 * 
 * @author kosm
 */
public class AddAccountActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(AddAccountActivity.class);

	@Override
	public void execute(AppContext appContext, Map<String, Object> operContext, Map<String, Object> operOutput)
			throws ActivityException {
		Account account = new Account();
		User user = (User)Objects.requireNonNull(operContext.get("user"), "No such user");
		account.setUserId(user.getId());
		account.setAccountId(UUID.randomUUID().toString());
		user.getAccountIds().add(account.getAccountId());
		appContext.getAccountCache().put(account.getAccountId(), account);
		appContext.getUserCache().put(user.getId(), user);
		operOutput.put("accountId", account.getAccountId());

		logger.trace("Added account with id {} to user with id {}", account.getAccountId(), user.getId());
	}

}
