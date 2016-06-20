package ru.kosm.finauth.core;

import java.util.List;
import java.util.Map;

/**
 * Action flow executed to handle a specific request
 * 
 * @author kosm
 */
public abstract class Flow implements Activity {
	
	private final List<? extends Activity> steps;
	
	/**
	 * Get step types to be executed within the current flow
	 * 
	 * @return List of steps
	 */
	protected abstract List<? extends Activity> getSteps();
	
	/**
	 * Construct the flow
	 * 
	 * @param steps Steps to execute during the flow execution
	 */
	public Flow() {
		steps = getSteps();
	}

	@Override
	public void execute(AppContext appContext, Map<String, Object> operContext,
			Map<String, Object> operOutput) throws ActivityException {
		for (Activity step : steps) step.execute(appContext, operContext, operOutput);
	}

}
