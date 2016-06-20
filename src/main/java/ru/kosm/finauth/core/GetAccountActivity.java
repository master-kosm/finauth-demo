package ru.kosm.finauth.core;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.domain.Account;

/**
 * Activity getting the account information by accountId
 * 
 * @author kosm
 */
public class GetAccountActivity implements Activity {

	private final static transient Logger logger = LogManager.getLogger(GetAccountActivity.class);

	@Override
	public void execute(AppContext appContext, Map<String, Object> operContext, Map<String, Object> operOutput)
			throws ActivityException {
		Account account = Objects.requireNonNull(appContext.getAccountCache()
				.get(operContext.get("accountId")), "No such account");
		operOutput.put("account", account);
		logger.trace("Got account with id {} belonging to user with id {}", account.getAccountId(), account.getUserId());
	}
	
}
