package ru.kosm.finauth.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ru.kosm.finauth.core.CacheAction;

/**
 * The class containing full operation context 
 * 
 * @author kosm
 */
public class Operation {

	private final String type;
	private final String id;
	private final Map<String, Object> context;
	private transient final List<CacheAction> cacheActions = new ArrayList<>();
	
	/**
	 * Constructor. On the creation the operation takes a new unique id
	 * 
	 * @param type Operation type
	 * @param context Operation context
	 */
	public Operation(String type, Map<String, Object> context) {
		this.type = type;
		this.context = context;
		this.id = UUID.randomUUID().toString();
		// Setting context defaults
		context.put("status", "INCOMPLETE");
	}
	
	/**
	 * Set the ERROR status to the operation 
	 */
	public void setErrorStatus() {
		context.put("status", "ERROR");
	}
	
	/**
	 * Set the status to the operation 
	 */
	public void setCompletedStatus() {
		context.put("status", "OK");
	}

	/**
	 * Get the list of actions to be performed on the cache at the end of transaction processing
	 * 
	 * @return List of cache actions
	 */
	public List<CacheAction> getCacheActions() {
		return cacheActions;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public Map<String, Object> getContext() {
		return context;
	}

}
