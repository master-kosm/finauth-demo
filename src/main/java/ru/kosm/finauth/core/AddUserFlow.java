package ru.kosm.finauth.core;

import java.util.Arrays;
import java.util.List;

/**
 * Flow for "Add user" operation
 * 
 * @author kosm
 *
 */
public class AddUserFlow extends Flow {
	
	@Override
	protected List<? extends Activity> getSteps() {
		return Arrays.asList(
			new AddUserActivity(),
			new AddAccountActivity()
		);
	}

}
