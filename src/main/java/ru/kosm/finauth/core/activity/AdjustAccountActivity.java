package ru.kosm.finauth.core.activity;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.Account;
import ru.kosm.finauth.domain.Operation;

/**
 * Activity adjusting the account balance
 * 
 * @author kosm
 */
public class AdjustAccountActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(AdjustAccountActivity.class);
	
	private final String idParamName;
	
	/**
	 * Constructor
	 * 
	 * @param idParamName Name of context parameter to be used to refer to the account
	 */
	public AdjustAccountActivity(String idParamName) {
		this.idParamName = idParamName;
	}

	@Override
	public void execute(AppContext appContext, Operation operation, Map<String, Object> operOutput)
			throws ActivityException {
		Account account = Objects.requireNonNull(appContext.getAccountCache()
				.get(operation.getContext().get(idParamName)), "No such account");
		String amountStr = (String)Objects.requireNonNull(operation.getContext().get("amount"), "No amount");
		long amount = Long.parseLong(amountStr);
		if (account.getBalance() + amount < 0) {
			throw new ActivityException("Not enough money, accountId: " + account.getAccountId());
		}
		operation.getCacheActions().add((a, o) -> account.setBalance(account.getBalance() + amount));

		logger.trace("Adjusted account with id {}, amount {}", account.getAccountId(),
				(amount > 0 ? "+" : "") + amountStr);
	}

	
}
