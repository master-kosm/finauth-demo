package ru.kosm.finauth.core;

import java.util.LinkedHashMap;
import java.util.Map;

/** Operation processor executes the sequence of actions for a particular operation
 * 
 * @author kosm
 */
public abstract class Processor {
	
	private Map<String, String> flows

	/**
	 * Go through the processing sequence for the operation
	 * 
	 * @param input Input parameters
	 * @return Output parameters
	 */
	public Map<String, Object> process(Map<String, Object> input) {
		Map<String, Object> output = new LinkedHashMap<>();
		return output;
	}
	
}
