package ru.kosm.finauth.core.activity;

import java.util.List;
import java.util.Map;

import ru.kosm.finauth.core.AppContext;
import ru.kosm.finauth.domain.Operation;

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
	public void execute(AppContext appContext, Operation operation,
			Map<String, Object> operOutput) throws ActivityException {
		for (Activity step : steps) step.execute(appContext, operation, operOutput);
	}

}
