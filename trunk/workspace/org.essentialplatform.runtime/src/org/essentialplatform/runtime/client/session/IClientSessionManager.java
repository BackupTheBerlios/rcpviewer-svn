package org.essentialplatform.runtime.client.session;

import java.util.Collection;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.IObjectStore;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.session.event.ISessionManagerListener;

/**
 * A (client-side) singleton that keeps track of all {@link IClientSession}s that 
 * have been instantiated, tracking the current session for each {@link IDomain}.
 * 
 * <p>
 * A {@link IClientSession} is a binding between an {@link IDomain} and an
 * {@link IObjectStore} that is capable of storing objects from that domain.
 * Since {@link IObjectStore}s are server-side, the session actually binds 
 * the {@link IDomain} object and the Id of the {@link IObjectStore}.  This binding
 * is encapsulated in the (serializable) {@link SessionBinding} that allows
 * the server-side equivalent of {@link IClientSessionManager} to recreate the
 * binding, but this time to the actual {@link IObjectStore} instance.
 *  
 * <p>
 * Typically there will be just one {@link IClientSession} per {@link IDomain} (and
 * even, just one {@link IDomain}), meaning that there is never any ambiguity
 * into which {@link IObjectStore} a given newly created domain object should
 * be persisted into.  However, the architecture does allow for multiple
 * {@link IClientSession}s for a given {@link IDomain}.  For example, the client
 * application might have instantiated objects from the <tt>Shop</tt> domain
 * for several retail outlets each with their own dedicated object store 
 * (Edinburgh, London, Oxford etc).  It is the responsibility of <t>this</i>
 * object to keep track of which is the current session for any given
 * domain.
 *  
 *  
 * @author Dan Haywood
 */
public interface IClientSessionManager {

	/**
	 * Set the current session to that represented by the {@link IDomain} and
	 * the supplied object store Id. 
	 */
	public IClientSession switchSessionTo(final Domain domain, String objectStoreId);
	
	
	/**
	 * @return The current session for the specified domain.  
	 *         May return <tt>null</tt> if there is no current session.
	 */
	public IClientSession getCurrentSession(final IDomain domain);
	
	
	/**
	 * Creates an {@link IClientSession} that effectively binds the specified 
	 * {@link IDomain} with the specified {@link IObjectStore} (through the
	 * latter's own Id)
	 * 
	 * <p>
	 * A unique session Id is automatically allocated, and the created session 
	 * is added to the collection of sessions maintained by this session manager
	 * and is moreover made the current session for the domain.
	 * 
	 * @return The newly created {@link IClientSession} 
	 */
	public IClientSession defineSession(final SessionBinding sessionBinding);
	
	/**
	 * Add a listener that will be notified of session manager changes
	 */
	public void addSessionManagerListener(ISessionManagerListener listener);
	
	/**
	 * Remove a previously added listener
	 */
	public void removeSessionManagerListener(ISessionManagerListener listener);

	/**
	 * Remove a session from the collection of managed sessions.
	 */
	public void removeSession(String sessionId);

	/**
	 * Returns all sessions currently managed by this session manager.
	 * 
	 * @return
	 */
	public Collection<IClientSession> getAllSessions();
	
}
