package org.essentialplatform.runtime.server.session.noop;

import org.essentialplatform.runtime.server.session.AbstractServerSessionFactory;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public final class NoopServerSessionFactory 
	extends AbstractServerSessionFactory<NoopServerSession> {


	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSessionFactory#open()
	 */
	public NoopServerSession open() {
		return new NoopServerSession(getSessionBinding());
	}


	/*
	 * @see org.essentialplatform.runtime.shared.session.IObjectStoreRef#reset()
	 */
	public void reset() {
		// nothing to do
	}


	public <V> void addClass(Class<V> javaClass) {
		// TODO Auto-generated method stub
		
	}

}
