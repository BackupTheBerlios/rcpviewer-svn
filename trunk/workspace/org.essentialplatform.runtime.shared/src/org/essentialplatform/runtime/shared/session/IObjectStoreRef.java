package org.essentialplatform.runtime.shared.session;

import org.essentialplatform.core.domain.IDomain;

/**
 * Represents a context-specific handle to an ObjectStore.
 * 
 * <p>
 * On the client-side, is implemented by IClientSession.  On the server-side, 
 * is implemented by IServerSessionFactory.  It is a factory for sessions on
 * the server-side because on the server-side, sessions are created/destroyed 
 * for each transaction, whereas for the client-side the session is long-lived.
 *   
 * @author Dan Haywood
 *
 */
public interface IObjectStoreRef {
	
	/**
	 * The id of the object store into which the domain objects managed by 
	 * handle to an object store will be stored.
	 * 
	 * <p>
	 * Typically there will be precisely one session per {@link IDomain}.
	 * However, the design allows multiple such handles (= sessions on the
	 * client-side). 
	 * 
	 * @return
	 */
	public String getObjectStoreId();
	

	
	/**
	 * Reset this handle.
	 * 
	 * <p>
	 * On the client-side (for a session), it means detach all instances.  
	 * Note that <i>no</i> {@link ITransactionListener}s will be notified. 
	 * 
	 * <p>
	 * Primarily of use for testing.
	 */
	void reset();

}
