package ru.kosm.finauth.core;

import java.util.Arrays;
import java.util.List;

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
			(app, ctx, out) -> ctx.put("amount", "-" + ctx.get("amount")),
			new AdjustAccountActivity("sourceAccountId")
		);
	}

}
