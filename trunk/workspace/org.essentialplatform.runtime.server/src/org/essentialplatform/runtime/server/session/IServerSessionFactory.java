package org.essentialplatform.runtime.server.session;

import org.essentialplatform.runtime.shared.session.IObjectStoreHandle;

public interface IServerSessionFactory<V extends IServerSession> extends IObjectStoreHandle {
	
	public V open();

}
