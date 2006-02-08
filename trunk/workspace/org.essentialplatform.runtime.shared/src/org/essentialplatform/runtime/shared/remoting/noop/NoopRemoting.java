package org.essentialplatform.runtime.shared.remoting.noop;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.remoting.AbstractRemoting;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;

/**
 * This is a mess and needs sorting out - just going for a clean compile at
 * the moment.
 * 
 * @author Dan Haywood
 *
 */
public final class NoopRemoting extends AbstractRemoting {

	private static Logger LOG = Logger.getLogger(NoopRemoting.class);
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
	}

	@Override
	public void send(Object object) {
	}
}


