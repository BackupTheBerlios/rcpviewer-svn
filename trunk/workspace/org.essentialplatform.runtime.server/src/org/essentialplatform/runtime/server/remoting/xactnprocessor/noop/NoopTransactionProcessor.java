package org.essentialplatform.runtime.server.remoting.xactnprocessor.noop;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.AbstractTransactionProcessor;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;

public final class NoopTransactionProcessor extends AbstractTransactionProcessor {

	protected Logger getLogger() {
		return Logger.getLogger(NoopTransactionProcessor.class);
	}

	public void process(ITransactionPackage transactionPackage) {
		getLogger().info("process(transactionPackage=" + transactionPackage + ")");
	}


}
