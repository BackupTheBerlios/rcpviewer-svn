package org.essentialplatform.runtime.session;

import java.util.Collection;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.session.event.ISessionManagerListener;
import org.essentialplatform.runtime.session.SessionBinding;

/**
 * A (client-side) singleton that keeps track of all {@link ISession}s that 
 * have been instantiated, tracking the current session for each {@link IDomain}.
 * 
 * <p>
 * A {@link ISession} is a binding between an {@link IDomain} and an
 * {@link IObjectStore} that is capable of storing objects from that domain.
 * Since {@link IObjectStore}s are server-side, the session actually binds 
 * the {@link IDomain} object and the Id of the {@link IObjectStore}.  This binding
 * is encapsulated in the (serializable) {@link SessionBinding} that allows
 * the server-side equivalent of {@link ISessionManager} to recreate the
 * binding, but this time to the actual {@link IObjectStore} instance.
 *  
 * <p>
 * Typically there will be just one {@link ISession} per {@link IDomain} (and
 * even, just one {@link IDomain}), meaning that there is never any ambiguity
 * into which {@link IObjectStore} a given newly created domain object should
 * be persisted into.  However, the architecture does allow for multiple
 * {@link ISession}s for a given {@link IDomain}.  For example, the client
 * application might have instantiated objects from the <tt>Shop</tt> domain
 * for several retail outlets each with their own dedicated object store 
 * (Edinburgh, London, Oxford etc).  It is the responsibility of <t>this</i>
 * object to keep track of which is the current session for any given
 * domain.
 *  
 *  
 * @author Dan Haywood
 */
public interface ISessionManager {
	
	/**
	 * Return the next identifier.
	 * 
	 * @return
	 */
	public String nextId();

	/**
	 * Returns the {@link ISession} with the specified id.
	 * 
	 * <p>
	 * If no such session has been created, then returns <code>null</code>.
	 * 
	 * @param id
	 * @return session with given identifier.
	 */
	public ISession get(String id);

	/**
	 * Set the current session for the {@link IDomain} referenced by the 
	 * {@link ISession} corresponding to the supplied sessionId.
	 */
	public void switchSessionTo(String sessionId);
	
	
	/**
	 * @return The current session for the specified domain.  
	 *         May return <tt>null</tt> if there is no current session.
	 */
	public ISession getCurrentSession(final IDomain domain);
	
	
	/**
	 * Creates an {@link ISession} that effectively binds the specified 
	 * {@link IDomain} with the specified {@link IObjectStore} (through the
	 * latter's own Id)
	 * 
	 * <p>
	 * A unique session Id is automatically allocated, and the created session 
	 * is added to the collection of sessions maintained by this session manager
	 * and is moreover made the current session for the domain.
	 * 
	 * @return The newly created {@link ISession} 
	 */
	public ISession defineSession(final IDomain domain, final String objectStoreId);
	
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
	public Collection<ISession> getAllSessions();
	
}
