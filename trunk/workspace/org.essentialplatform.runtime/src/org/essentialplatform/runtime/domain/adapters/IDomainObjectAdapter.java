package org.essentialplatform.runtime.domain.adapters;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.adapters.AbstractDomainClassAdapter;
import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * All adapters of {@link IDomainClass} (as obtained using 
 * {@link org.essentialplatform.domain.IDomainClass#getAdapter(Class.class)})
 * should implement this interface but more importantly should extend from
 * {@link AbstractDomainClassAdapter} in order that they can be automatically
 * initialized (through their constructor) with an instance of the 
 * {@link IDomainClass} that they extend.
 * 
 * @author Dan Haywood
 */
public interface IDomainObjectAdapter<T> {
	
	/**
	 * The {@link IDomainClass} that this is an adapter for.
	 *  
	 * @return
	 */
	IDomainObject<T> adapts();
	
	

}
