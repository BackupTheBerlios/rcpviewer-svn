package org.essentialplatform.runtime.shared;

import org.eclipse.core.runtime.CoreException;

/**
 * Registers (via {@link org.essentialplatform.core.domain.Domain#lookup(Class)})
 * one or several domain classes in order to create a 
 * {@link org.essentialplatform.core.domain.IDomain}domain to hold those classes.
 * 
 * <p>
 * Typically, when an {@link org.essentialplatform.core.domain.IDomainClass} is
 * created for a class, it will implicitly register other classes as a result
 * of references and collections.  By carefully choosing which class to 
 * register, it may only be necessary to register a single class.
 * 
 * @author Dan Haywood
 *
 */
public interface IDomainBootstrap {
	
	/**
	 * 
	 * @throws CoreException if fail to register.
	 */
	void registerClasses() throws CoreException;

}
