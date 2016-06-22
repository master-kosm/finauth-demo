package ru.kosm.finauth.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kosm.finauth.core.activity.Activity;
import ru.kosm.finauth.core.activity.ActivityException;
import ru.kosm.finauth.core.activity.AddUserFlow;
import ru.kosm.finauth.core.activity.AdjustAccountActivity;
import ru.kosm.finauth.core.activity.GetAccountActivity;
import ru.kosm.finauth.core.activity.GetOperationActivity;
import ru.kosm.finauth.core.activity.GetUserActivity;
import ru.kosm.finauth.core.activity.TransferFlow;
import ru.kosm.finauth.domain.Operation;

/**
 * Operation processor executes the sequence of actions for a particular operation
 * 
 * @author kosm
 */
public class Processor {
	
	private final AppContext appContext;
	/** Lock acquired on the application of cache actions at the end of transaction */
	private final ReentrantLock cacheActionLock = new ReentrantLock(); 
	/** Flows executed to handle operations */
	private Map<String, Activity> flows = new HashMap<String, Activity>() {
		private static final long serialVersionUID = 3391398508868773848L;
		
		{
			put("Add user", new AddUserFlow());
			put("Get user", new GetUserActivity());
			put("Get account", new GetAccountActivity());
			put("Adjust", new AdjustAccountActivity("accountId"));
			put("Transfer", new TransferFlow());
			put("Get operation", new GetOperationActivity());
		}
	};
	
	private final static transient Logger logger = LogManager.getLogger(Processor.class);
	
	public Processor(AppContext appContext) {
		this.appContext = appContext;
	}
	
	/**
	 * Construct objects for output parameters of a particular operation
	 * within a response batch 
	 * 
	 * @param operationName Name of operation
	 * @param responseOutput Output of the response batch 
	 * @return Output parameters
	 */
	static Map<String, Object> constructOperOutput(String operationName,
			Map<String, Object> responseOutput) {
		Map<String, Object> outputParams = new LinkedHashMap<>();
		responseOutput.put(operationName, outputParams);
		return outputParams;
	}
	
	/** 
	 * Final preparations of the operations output: set status and operation Id
	 * 
	 * @param operation Operation to finalize
	 * @param operOutput Operation output
	 */
	static void finalizeOperOutput(Operation operation, Map<String, Object> operOutput) {
		operOutput.put("status", operation.getContext().get("status"));
		operOutput.put("operationId", operation.getId());
	}
	
	/**
	 * Executed cache actions stashed in the operations
	 * 
	 * @param operation Operation containing the cache actions to execute
	 */
	void executeCacheActions(Operation operation) {
		cacheActionLock.lock();
		try {
			operation.getCacheActions().forEach(a -> a.apply(appContext, operation));
			// Implicitly storing the operation in the operation cache
			appContext.getOperationCache().put(operation.getId(), operation);
			// Locking the caches for write forever in case of failure in one of the cache actions.
			// The caches are still accessible for read.
			// See CacheAction.apply() for more details.
			cacheActionLock.unlock();
		}
		catch (Exception e) {
			logger.fatal("Error ocurred during the application of cache actions!!! " +
					"Further cache writes are prohibited.", e);
		}
	}

	/**
	 * Go through the processing sequence for the operation
	 * 
	 * @param input Input operations
	 * @return Output results
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> process(Map<String, Object> input) {
		Map<String, Object> output = new LinkedHashMap<>();
		// Traversing the request batch
		input.forEach((k, v) -> {
			logger.trace("Processing request: {}", k);
			Map<String, Object> operOutput = constructOperOutput(k, output);
			// Constructing new operation
			Operation operation = null;
			try {
				if (!(v instanceof Map)) {
					throw new IllegalArgumentException("Malformed parameters of the request: " + k);
				}
				operation = new Operation(k, (Map<String, Object>)v);
				Activity flow = flows.get(k);
				if (flow == null) throw new ActivityException("No appropriate flow for the request: " + k);
				flow.execute(appContext, operation, operOutput);
				// "Committing" the transaction
				executeCacheActions(operation);
				operation.setCompletedStatus();
			}
			catch (Exception e) {
				if (operation != null) operation.setErrorStatus();
				operOutput.put("errorDescription", e.getMessage());
				logger.error(e);
			}
			finally {
				if (operation != null) finalizeOperOutput(operation, operOutput);
			}
		});
		return output;
	}
	
}
