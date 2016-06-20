package ru.kosm.finauth.core;

import java.util.Map;

/** Activity is a flow element executed during the operation processing
 * 
 * @author kosm
 */
@FunctionalInterface
public interface Activity {

	/**
	 * Execute the activity
	 * 
	 * @param appContext Application context
	 * @param operContext Input operation contest
	 * @param operOutput Output operation parameters
	 */
	public void execute(AppContext appContext, Map<String, Object> operContext,
			Map<String, Object> operOutput) throws ActivityException;
	
}