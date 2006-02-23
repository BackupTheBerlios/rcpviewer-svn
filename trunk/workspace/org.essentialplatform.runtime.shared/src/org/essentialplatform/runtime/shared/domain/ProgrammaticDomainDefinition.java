package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.builders.IClassLoader;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.runtime.shared.domain.builders.StandardClassLoader;
import org.osgi.framework.Bundle;

/**
 * Initialises the <code>Domain</code> by registering one or several
 * domain classes.
 * 
 * <p>
 * Designed to be set up programmatically, eg for testing purposes.
 * 
 * @author Dan Haywood
 */
public class ProgrammaticDomainDefinition extends AbstractDomainDefinition {


	@Override
	protected Logger getLogger() {
		return Logger.getLogger(ProgrammaticDomainDefinition.class);
	}

	public ProgrammaticDomainDefinition(final String name, final IDomainBuilder domainBuilder, final String... classes) {
		setName(name);
		setDomainBuilder(domainBuilder);
		setClasses(Arrays.asList(classes));
	}
	
	
	
	///////////////////////////////////////////////////////////////////////
	// Classes
	// (or rather, the names of classes).
	///////////////////////////////////////////////////////////////////////
	
	private List<String> _classNames = new ArrayList<String>();
	/**
	 * The (fully qualified names of) classes to register.
	 */
	public List<String> getClasses() {
		return _classNames;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Mandatory; supply a list of String values, each being the fully qualified
	 * name of a class to be registered.
	 * 
	 * <p>
	 * (In fact, if not specified then will not fail, but equally won't 
	 * do anything either).
	 * 
	 * @param classes - referenced directly rather than copied, since intended to be injected. 
	 */
	public void setClasses(List<String> classes) {
		_classNames = classes;
	}


	///////////////////////////////////////////////////////////////////////
	// Registration
	// (or rather, the names of classes).
	///////////////////////////////////////////////////////////////////////
	
	private StandardClassLoader _classLoader = new StandardClassLoader();
	
	/**
	 * Uses the bundle if provided, else falls back to {@link Class#forName(java.lang.String)}.
	 */
	@Override
	public void doRegisterClasses() throws DomainBootstrapException {
		
		List<Class<?>> classes = new ArrayList<Class<?>>();

		// check each className represents a no-arg instantiable class ...
		for (String className: _classNames) {
			Class<?> javaClass = _classLoader.loadClass(className);
			classes.add(javaClass);
		}
		
		// ... then add to domain
		for (Class<?> javaClass: classes) {
			registerClass(javaClass);
		}
	}
	
}
