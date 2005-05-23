package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.persistence.IObjectStore;

/**
 * A factory for {@link ISession}s.
 * 
 * <p>
 * Typically there will be just one {@link ISession} per {@link Domain}, and
 * it will be wired up to persist to a particular {@link IObjectStore} (though
 * the {@link IObjectStore} can be overridden at creation time.  However, we 
 * don't want to mandate when this session might be created, nor that there be 
 * precisely one.  (For example, at the time of writing it isn't known how 
 * <i>rcpviewer</i> will support "what-if" analysis.  One possible design  
 * would be to have a separate session to perform such analysis).
 *
 * <p>
 * Hence we use this factory interface to hold the defining configuration for 
 * building sessions.  There would typically be a single instance of the 
 * factory, and its implementation might well be initialized using dependency
 * injection via Spring or equivalent. 
 * 
 * <p>
 * As the factory creates each {@link ISession}, a session identifier is also
 * created.  The value of this identifier is propagated to all 
 * {@link IDomainObject}s that the session then subsequently creates.  This
 * allows the domain objects themselves to locate the session if they are later
 * detached with {@link ISession#detach(IDomainObject)}. 
 * 
 * 
 * @author Dan Haywood
 *
 */
public interface ISessionFactory {
	
	/**
	 * Create an {@link ISession} for the factory's {@link IDomain}, and using 
	 * the {@link IObjectStore} referenced by the factory.
	 * 
	 * @return
	 */
	ISession createSession();
	
	/**
	 * Create an {@link ISession} for the factory's {@link IDomain}, but using 
	 * a supplied {@link IObjectStore} rather than that referenced by the 
	 * factory.
	 * 
	 * <p>
	 * This might be of use to allow support of "what-if" changes, eg persisting
	 * to an in-memory objectstore.  Or, it could be used to migrate data
	 * between two objectstores?
	 * 
	 * @return
	 */
	ISession createSession(IObjectStore objectStore);
	
	/**
	 * The {@link IObjectStore} that will be used for {@link ISession}s built
	 * using the {@link #createSession()} factory method.
	 *  
	 * @return
	 */
	IObjectStore getObjectStore();
	
	/**
	 * The name of the {@link IDomain} that all {@link ISession}s built using 
	 * this factory will reference.
	 * 
	 * <p>
	 * The name, rather than the {@link Domain} is held, because Domains are
	 * instantiated indirectly through {@link Domain#instance(String)}
	 *  
	 * @return
	 */
	String getDomainName();

	/**
	 * Convenience accessor of the {@link Domain} with this session factory's
	 * {@link #getDomainName()}.
	 * 
	 * <p>
	 * Implementations should (must) use {@link Domain#instance(String)} to 
	 * obtain.
	 * 
	 * @return
	 */
	Domain getDomain();
	
}
