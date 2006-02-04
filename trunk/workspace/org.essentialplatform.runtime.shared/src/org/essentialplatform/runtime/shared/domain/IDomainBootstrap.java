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
public interface IDomainBootstrap {

	/**
	 * TODO: the domainName is temporary until such time that merge with IDomainDefinition
	 * 
	 * @param domainName
	 * @throws DomainBootstrapException
	 */
	void registerClasses(String domainName) throws DomainBootstrapException;

	/**
	 * Propagated by owning IDomainDefinition.
	 * 
	 * <p>
	 * TODO: will disappear once merge IDomainBootstrap with DomainDefinition.
	 * @param domainBundle
	 */
	void setBundle(Bundle domainBundle);

	List<IDomainBuilder> getSecondaryBuilders();
	
	/**
	 * Adds the {@link IDomainBuilder} to those returned from {@link #getSecondaryBuilders()}.
	 * 
	 * <p>
	 * If using an implementation that is designed for dependency injection, 
	 * then this method would <i>not</i> be used.  Instead, the implementation
	 * would provide a setter for the entire <tt>List</tt>.
	 * 
	 * @param domainBuilder
	 */
	void addSecondaryBuilder(IDomainBuilder domainBuilder);

	
}
