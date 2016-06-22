package ru.kosm.finauth.core.activity;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.Operation;

/**
 * Activity applying the fee to the amount
 * 
 * @author kosm
 *
 */
public class ApplyFeeActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(ApplyFeeActivity.class);
	
	// For test purposes the fee is static 
	public final static long fee = 100;  

	@Override
	public void execute(AppContext appContext, Operation operation, Map<String, Object> operOutput)
			throws ActivityException {
		long amount = Long.parseLong((String)Objects.requireNonNull(operation.getContext().get("amount"), "No amount"));
		operation.getContext().put("amount", Long.toString(amount + fee));
		
		logger.trace("Applied fee, fee is {}, old amount is {}, new amount is {}", fee, amount, amount + fee); 
	}

}
