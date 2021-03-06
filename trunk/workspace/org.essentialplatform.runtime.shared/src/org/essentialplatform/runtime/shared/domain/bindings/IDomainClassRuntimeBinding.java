package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Represents runtime-specific functionality for a 
 * {@link IDomainClass} (common to both client- and server).
 * 
 * <p>
 * @see IDomainObjectRuntimeBinding. 
 * 
 * @author Dan Haywood
 */
public interface IDomainClassRuntimeBinding<T> extends IDomainClassBinding<T> {
	
	public Class<T> getJavaClass();

	/**
	 * For the domain class to which <i>this</i> binding is associated, returns
	 * a binding for an instance of said domain class, in the same runtime
	 * context (eg client or server).
	 * @param domainObject TODO
	 * 
	 * @return
	 */
	public IDomainObjectRuntimeBinding<T> getObjectBinding(IDomainObject domainObject);

	/**
	 * Convenience method that creates a new instance of the java class
	 * (as represented by {@link #getJavaClass()}).
	 * 
	 * <p>
	 * Uses the no-arg constructor.
	 * 
	 * @return
	 */
	public T newInstance(final SessionBinding sessionBinding, PersistState persistState, ResolveState resolveState);
	
}