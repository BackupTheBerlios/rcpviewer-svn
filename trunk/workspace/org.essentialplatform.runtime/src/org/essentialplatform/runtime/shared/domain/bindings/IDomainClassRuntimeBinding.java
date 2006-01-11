package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

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
	
	/**
	 * For the domain class to which <i>this</i> binding is associated, returns
	 * a binding for an instance of said domain class, in the same runtime
	 * context (eg client or server).
	 * @param domainObject TODO
	 * 
	 * @return
	 */
	public IDomainObjectRuntimeBinding<T> getObjectBinding(IDomainObject domainObject);
	
}