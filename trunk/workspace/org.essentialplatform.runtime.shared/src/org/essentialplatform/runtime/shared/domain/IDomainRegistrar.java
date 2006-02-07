package org.essentialplatform.runtime.shared.domain;

import java.util.List;

import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.osgi.framework.Bundle;


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
public interface IDomainRegistrar  {

	
	void registerClass(Class<?> javaClass);
	
	/**
	 * Propagated by owning IDomainDefinition.
	 * 
	 * <p>
	 * TODO: will disappear once merge IDomainRegistrar with DomainDefinition.
	 * @param domainBundle
	 */
	void setBundle(Bundle domainBundle);

	
}
