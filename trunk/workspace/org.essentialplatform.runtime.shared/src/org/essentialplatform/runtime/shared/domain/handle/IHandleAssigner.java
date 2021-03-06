package org.essentialplatform.runtime.shared.domain.handle;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Encapsulates the generation of handles for domain objects.
 * 
 * <p>
 * This interface is closely coupled to the semantics of the 
 * <tt>@IdAssignmentType</tt> annotation.  It is factored out for testing
 * purposes. 
 * 
 * @author Dan Haywood
 *
 */
public interface IHandleAssigner {
	
	
	/**
	 * Creates and assigns a handle to the supplied domain object.
	 * 
	 * <p>
	 * Depending on the implementation, the state of the domain object may or
	 * may not be used.
	 * 
	 * <p>
	 * If the implementation is not able to assign a handle, it should
	 * delegate to the next in the chain (as returned by 
	 * {@link #nextAssigner()}).
	 *  
	 * @param <T>
	 * @param domainObject
	 * @return handle assigned by this (or next in chain).
	 */
	<T> Handle assignHandleFor(IDomainObject<T> domainObject);
	
	/**
	 * Chain of responsibility pattern; initialized through implementation's
	 * constructor.  
	 * 
	 * <p>
	 * May be null if at end of chain.
	 * 
	 * @return
	 */
	IHandleAssigner nextAssigner();
}
