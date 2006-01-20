package org.essentialplatform.runtime.server.session.noop;

import org.essentialplatform.runtime.server.session.AbstractServerSessionFactory;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public final class NoopServerSessionFactory 
	extends AbstractServerSessionFactory<NoopServerSession> {

	public NoopServerSessionFactory(final SessionBinding sessionBinding) {
		super(sessionBinding);
	}
	

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSessionFactory#open()
	 */
	public NoopServerSession open() {
		return new NoopServerSession(getSessionBinding());
	}


	/*
	 * @see org.essentialplatform.runtime.shared.session.IObjectStoreHandle#reset()
	 */
	public void reset() {
		// nothing to do
	}

}
