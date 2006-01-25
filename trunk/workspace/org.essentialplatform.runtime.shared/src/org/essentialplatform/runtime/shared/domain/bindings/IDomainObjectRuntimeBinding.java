package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IObservedFeature;


/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> a {@link IDomainClass} (that is, an {@link IDomainObject}).
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectRuntimeBinding<T> extends IObservedFeature {

	/**
	 * So that the domain object can delegate to its runtime binding to 
	 * determine whether its binding to a session can be cleared or not. 
	 *
	 */
	void assertCanClearSessionBinding() throws IllegalStateException;


	/**
	 * Whether the domain object is attached to (the binding-specific notion of)
	 * a session.
	 * 
	 * @return
	 */
	boolean isAttached();

}