package ru.kosm.finauth.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.kosm.finauth.domain.Account;
import ru.kosm.finauth.domain.Operation;
import ru.kosm.finauth.domain.User;

/**
 * Application context encapsulates internal references to application resources 
 * 
 * @author kosm
 */
public class AppContext {

	private final Map<String, User> userCache = new ConcurrentHashMap<>();
	private final Map<String, Account> accountCache = new ConcurrentHashMap<>();
	private final Map<String, Operation> operationCache = new ConcurrentHashMap<>();
	private final Processor processor = new Processor(this);
	
	public AppContext() {
		
	}
	
	/**
	 * Dispose application context resources before the shutdown
	 */
	public void dispose() {
		// Nothing to do for now
	}

	public Map<String, User> getUserCache() {
		return userCache;
	}

	public Map<String, Account> getAccountCache() {
		return accountCache;
	}

	public Map<String, Operation> getOperationCache() {
		return operationCache;
	}

	public Processor getProcessor() {
		return processor;
	}

}
