package org.essentialplatform.transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class TranMgmtConstants {

	/**
	 * Cannot be instantiated.
	 */
	private TranMgmtConstants() {
	}

	/**
	 * Used in constructing identifiers of {@link ITransaction}s.
	 * 
	 * <p>
	 * Format is designed to be easily human readable, and unique within a
	 * typical session. eg:
	 * <p><code>15 Aug, 1:35.25 pm </code>
	 * <br><code>1 Sep, 10:03.12 pm</code>
	 */
	public static final DateFormat TRANSACTION_ID_FORMAT = new SimpleDateFormat("dd MMM, KK:mm.ss a");

}
