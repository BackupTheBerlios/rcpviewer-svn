package org.essentialplatform.domain;

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
public interface IDomainClassAdapter {
	
	/**
	 * The {@link IDomainClass} that this is an adapter for.
	 *  
	 * @return
	 */
	IDomainClass adapts();
	
}
