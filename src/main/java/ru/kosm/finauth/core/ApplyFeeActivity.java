package ru.kosm.finauth.core;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Activity applying the fee to the amount
 * 
 * @author kosm
 *
 */
public class ApplyFeeActivity implements Activity {
	
	private final static transient Logger logger = LogManager.getLogger(ApplyFeeActivity.class);
	
	// For test purposes the fee is static 
	private final static long fee = 100;  

	@Override
	public void execute(AppContext appContext, Map<String, Object> operContext, Map<String, Object> operOutput)
			throws ActivityException {
		long amount = Long.parseLong((String)Objects.requireNonNull(operContext.get("amount"), "No amount"));
		operContext.put("amount", Long.toString(amount + fee));
		
		logger.trace("Applied fee, fee is {}, old amount is {}, new amount is {}", fee, amount, amount + fee); 
	}

}
