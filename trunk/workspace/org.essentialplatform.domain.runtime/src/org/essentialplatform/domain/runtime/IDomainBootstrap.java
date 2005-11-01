package org.essentialplatform.domain.runtime;

import org.eclipse.core.runtime.CoreException;

/**
 * Registers (via {@link org.essentialplatform.domain.Domain#lookup(Class)})
 * one or several domain classes in order to create a 
 * {@link org.essentialplatform.domain.IDomain}domain to hold those classes.
 * 
 * <p>
 * Typically, when an {@link org.essentialplatform.domain.IDomainClass} is
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
