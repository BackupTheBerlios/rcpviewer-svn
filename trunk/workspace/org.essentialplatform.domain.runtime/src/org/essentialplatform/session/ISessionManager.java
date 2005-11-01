package org.essentialplatform.session;

import java.util.Collection;

import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomain;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.persistence.IObjectStore;

/**
 * A singleton that, with the co-operation of {@link ISessionFactory}, keeps 
 * track of all {@link ISession}s that have been instantiated.
 * 
 * <p>
 * Typically there will be precisely one {@link ISessionManager}, one
 * {@link ISessionFactory} and one {@link ISession}.  The 
 * {@link ISessionFactory} refers to a specific {@link Domain} and a specific
 * (compatible) {@link IObjectStore} and passes these onto the {@link ISession}
 * that it creates.  The {@link ISession}'s identifier is also passed to it by 
 * the @link ISessionFactory}, but the factory obtains the identifier from
 * the session manager (that is, an instance of this interface).
 * 
 * <p>
 * However, the design of a manager, a factory and sessions allows for more 
 * involved setups.  For example, we could have two {@link ISession}s built
 * from the same {@link ISessionFactory} (therefore relating to the same
 * {@link Domain}), but with different {@link IObjectStore}s.  The  
 * {@link IDomainObject}s of each {@link ISession} would be distinct (eg a
 * development vs a production environment, or London vs Hong Kong).
 * 
 * <p>
 * Alternatively, there might be two {@link ISessionFactory}s, each configured
 * with a different {@link Domain}.  Each would be used to create instances of
 * {@link IDomainObject}s referring to {@link IDomainClass}es of their
 * corresponding {@link Domain}s.  For example, one domain might be the 
 * <i>default</i>  domain (holding classes modelling an order management
 * system, for example), and one domain might be an <i>email</i> domain 
 * (holding classes that represent emails and contacts), with an 
 * {@link IObjectStore} that persists to the underlying mail system.
 * 
 *  
 * @author Dan Haywood
 *
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
	 * Set the system's current session
	 */
	public void switchSessionTo(String id);
	
	
	/**
	 * @return The ID of the current session.  May return null if there is no current session.
	 */
	public String getCurrentSessionId();
	
	
	/**
	 * Creates an {@link ISession} that effectively binds the specified 
	 * {@link IDomain} with the specified {@link IObjectStore}.
	 * 
	 * <p>
	 * A unique session Id is automatically allocated, and the created session 
	 * is added to the collection of sessions maintained by this session manager
	 * and is moreover made the current session.
	 * 
	 * @return The newly created {@link ISession} 
	 */
	public ISession createSession(final IDomain domain, final IObjectStore objectStore);
	
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
