package ru.kosm.finauth.core.activity;

import java.util.Map;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.Operation;

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
	 * @param operation Executed operation
	 * @param operOutput Output operation parameters
	 */
	public void execute(AppContext appContext, Operation operation,
			Map<String, Object> operOutput) throws ActivityException;
	
}