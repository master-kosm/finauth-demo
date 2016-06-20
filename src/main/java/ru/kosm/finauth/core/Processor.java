package ru.kosm.finauth.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Operation processor executes the sequence of actions for a particular operation
 * 
 * @author kosm
 */
public class Processor {
	
	private final AppContext appContext;
	/** Flows executed to handle operations */
	private Map<String, Activity> flows = new HashMap<String, Activity>() {
		private static final long serialVersionUID = 3391398508868773848L;
		
		{
			put("Add user", new AddUserFlow());
			put("Get user", new GetUserActivity());
			put("Get account", new GetAccountActivity());
			put("Adjust", new AdjustAccountActivity("accountId"));
			put("Transfer", new TransferFlow());
		}
	};
	
	private final static transient Logger logger = LogManager.getLogger(Processor.class);
	
	public Processor(AppContext appContext) {
		this.appContext = appContext;
	}
	
	/**
	 * Initialize the operation context: set unique operation number and
	 * default operation status
	 * 
	 * @param operContext Operation context to init
	 */
	static void initOperContext(Map<String, Object> operContext) {
		operContext.put("operationId", UUID.randomUUID());
		operContext.put("status", "OK");
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
	 * @param operContext Operation context
	 * @param operOutput Operation output
	 */
	static void finalizeOperOutput(Map<String, Object> operContext, Map<String, Object> operOutput) {
		operOutput.put("status", operContext.get("status"));
		operOutput.put("operationId", operContext.get("operationId"));
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
			Map<String, Object> operContext = null;
			//Transaction txn = null;
			try {
				appContext.getTransactionManager().begin();
				//txn = appContext.getTransactionManager().getTransaction();
				//appContext.getAccountCache().startBatch();
				if (!(v instanceof Map)) {
					throw new IllegalArgumentException("Malformed parameters of the request: " + k);
				}
				operContext = (Map<String, Object>)v;
				
				initOperContext(operContext);
				Activity flow = flows.get(k);
				if (flow == null) throw new ActivityException("No appropriate flow for the request: " + k);
				flow.execute(appContext, operContext, operOutput);
				//txn.commit();
				appContext.getTransactionManager().commit();
				//appContext.getAccountCache().endBatch(true);
			}
			catch (Exception e) {
				//appContext.getAccountCache().endBatch(false);
				try {
					appContext.getTransactionManager().rollback();
					//if (txn != null) txn.rollback();
				}
				catch (Exception e1) {
					logger.error(e);
				}
				if (operContext != null) operContext.put("status", "ERROR");
				operOutput.put("errorDescription", e.getMessage());
				logger.error(e);
			}
			finally {
				if (operContext != null) finalizeOperOutput(operContext, operOutput);
			}
		});
		return output;
	}
	
}
