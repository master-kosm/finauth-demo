package ru.kosm.finauth.core.activity;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.Operation;

/**
 * Activity getting a specified operation
 * 
 * @author kosm
 */
public class GetOperationActivity implements Activity {

	private final static transient Logger logger = LogManager.getLogger(GetOperationActivity.class);

	@Override
	public void execute(AppContext appContext, Operation operation, Map<String, Object> operOutput)
			throws ActivityException {
		Operation gotOperation = Objects.requireNonNull(appContext.getOperationCache()
				.get(operation.getContext().get("operationId")), "No such operation");
		operOutput.put("operation", gotOperation);
		logger.trace("Got operation with id {}", gotOperation.getId());
	}
	
}
