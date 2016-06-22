package ru.kosm.finauth.core;

import ru.kosm.finauth.domain.Operation;

/**
 * Action performed on the cache. The batch of cache actions is atomically executed
 * upon the end of transaction.
 * 
 * @author kosm
 */
@FunctionalInterface
public interface CacheAction {
	
	/**
	 * Apply the cache changes
	 * CAUTION!!! Throwing an Exception by this method shall cause the ever write lock
	 * of the caches, since the caches are not allowed to be in an inconsistent state
	 * and they do not support rollback. 
	 * 
	 * @param appContext Application context
	 * @param operation Operation
	 */
	void apply(AppContext appContext, Operation operation);

}
