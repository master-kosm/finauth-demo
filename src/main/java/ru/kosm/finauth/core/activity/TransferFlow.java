package ru.kosm.finauth.core.activity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Flow for "Transfer" operation. Transfers funds between two accounts 
 * 
 * @author kosm
 */
public class TransferFlow extends Flow {

	@Override
	protected List<? extends Activity> getSteps() {
		return Arrays.asList(
			new AdjustAccountActivity("targetAccountId"),
			new ApplyFeeActivity(),
			(app, opr, out) -> opr.getContext().put("amount", "-" +
					Objects.requireNonNull(opr.getContext().get("amount"), "No amount")),
			new AdjustAccountActivity("sourceAccountId")
		);
	}

}
