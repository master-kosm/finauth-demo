package ru.kosm.finauth.core;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import ru.kosm.finauth.domain.Account;
import ru.kosm.finauth.domain.User;

/**
 * Application context encapsulates internal references to application resources 
 * 
 * @author kosm
 */
public class AppContext {

	private final UserManager userManager = new UserManager(this);
	private final EmbeddedCacheManager cacheManager = new DefaultCacheManager();
	private final Cache<String, User> userCache = cacheManager.getCache();
	private final Cache<String, Account> accountCache = cacheManager.getCache();
	
	public AppContext() {
		
	}
	
	/** Dispose application context resources before the shutdown
	 */
	public void dispose() {
		cacheManager.stop();
	}

	/** Get {@link UserManager}
	 * 
	 * @return Instance of UserManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	public Cache<String, User> getUserCache() {
		return userCache;
	}

	public Cache<String, Account> getAccountCache() {
		return accountCache;
	}
	
}
