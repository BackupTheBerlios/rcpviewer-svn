package org.essentialplatform.runtime.server.session;

import org.essentialplatform.runtime.server.database.IDatabaseServer;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.essentialplatform.runtime.shared.session.IObjectStoreRef;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public interface IServerSessionFactory<V extends IServerSession> extends IObjectStoreRef {

	/**
	 * Initialize the referenced {@link IDatabaseServer} with the domain
	 * represented by {@link IDomainDefinition}.
	 * 
	 * @return a {@link SessionBinding} computed from the domain and database.  
	 *         Note that the database knows the object store name. 
	 */
	public SessionBinding init();
	public SessionBinding getSessionBinding();
	
	public IDomainDefinition getDomainDefinition();
	
	public IDatabaseServer getDatabaseServer();

	/**
	 * Register class.
	 * 
	 * @param <V>
	 * @param javaClass
	 */
	public <V> void addClass(Class<V> javaClass);
		
	/**
	 * Creates a new {@link IServerSession} to process a request (eg apply
	 * a set of changes, or perform a repository query and return its
	 * results).
	 * 
	 * @return
	 */
	public V open();

}
