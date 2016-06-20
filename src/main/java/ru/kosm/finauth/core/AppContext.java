package ru.kosm.finauth.core;

import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.TransactionMode;

import ru.kosm.finauth.domain.Account;
import ru.kosm.finauth.domain.User;

/**
 * Application context encapsulates internal references to application resources 
 * 
 * @author kosm
 */
public class AppContext {

	private final EmbeddedCacheManager cacheManager;
	private final Cache<String, User> userCache;
	private final Cache<String, Account> accountCache;
	private final Processor processor = new Processor(this);
	private final TransactionManager transactionManager;
	
	public AppContext() {
		//Configuration config = new ConfigurationBuilder().invocationBatching().enable().transaction().build();
		/*Configuration config = new ConfigurationBuilder().
				transaction()
				.transactionMode(TransactionMode.TRANSACTIONAL).autoCommit(false)
				.useSynchronization(false).lockingMode(LockingMode.PESSIMISTIC)
				.invocationBatching().enable()
				.build();*/
		Configuration config = new ConfigurationBuilder().transaction()
				.transactionMode(TransactionMode.TRANSACTIONAL).build();
		cacheManager = new DefaultCacheManager(config);
		userCache = cacheManager.getCache();
		accountCache = cacheManager.getCache();
		transactionManager = accountCache.getAdvancedCache().getTransactionManager();
	}
	
	/**
	 * Dispose application context resources before the shutdown
	 */
	public void dispose() {
		cacheManager.stop();
	}

	public Cache<String, User> getUserCache() {
		return userCache;
	}

	public Cache<String, Account> getAccountCache() {
		return accountCache;
	}

	public Processor getProcessor() {
		return processor;
	}

	public TransactionManager getTransactionManager() {
		return transactionManager;
	}
	
}
