package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.domain.AbstractDomainClassAdapter;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * All adapters of {@link IDomainClass} (as obtained using 
 * {@link de.berlios.rcpviewer.domain.IDomainClass#getAdapter(Class.class)})
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
