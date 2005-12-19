package org.essentialplatform.server.persistence;

import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * Encapsulates the generation of persistence Ids for domain objects.
 * 
 * <p>
 * This interface is closely coupled to the semantics of the 
 * <tt>@IdAssignmentType</tt> annotation.  It is factored out for testing
 * purposes. 
 * 
 * @author Dan Haywood
 *
 */
public interface IPersistenceIdAssigner {
	
	/**
	 * Creates and assigns a persistence Id to the supplied domain object.
	 * 
	 * <p>
	 * Depending on the implementation, the state of the domain object may or
	 * may not be used.
	 * 
	 * <p>
	 * If the implementation is not able to assign a persistence Id, it should
	 * delegate to the next in the chain (as returned by 
	 * {@link #nextAssigner()}).
	 *  
	 * @param <T>
	 * @param domainObject
	 * @return persistence Id assigned by this (or next in chain).
	 */
	<T> PersistenceId assignPersistenceIdFor(IDomainObject<T> domainObject);
	
	/**
	 * Chain of responsibility pattern; initialized through implementation's
	 * constructor.  
	 * 
	 * <p>
	 * May be null if at end of chain.
	 * 
	 * @return
	 */
	IPersistenceIdAssigner nextAssigner();
}
