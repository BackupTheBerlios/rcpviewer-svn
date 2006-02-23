package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.builders.IClassLoader;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.runtime.shared.domain.builders.BundleElseStandardClassLoader;
import org.osgi.framework.Bundle;

/**
 * Initialises the <code>Domain</code> by registering one or several
 * domain classes.
 * 
 * <p>
 * Designed to be set up with Spring configuration files (as opposed to being
 * set up using extension points, for example).
 * 
 * <p>
 * Typical setup would be something like:
 * <pre>
 * &lt;bean id="domain" 
 *     class="org.essentialplatform.runtime.shared.domain.SpringConfiguredDomainDefinition">
 *     &lt;property name="classes">
 *         &lt;list>
 *             &lt;value>com.example.Customer"&lt;/value>
 *             &lt;value>com.example.Product"&lt;/value>
 *             &lt;value>com.example.Employee"&lt;/value>
 *         &lt;/list>
 *     &lt;/property>
 * &lt;/bean>
 * </pre> 
 * <p>
 * Where the <tt>com.example.Customer</tt> etc is the fully qualified name of
 * the domain class being registered.
 * 
 * <p>
 * Note that it isn't necessary to register every domain class; often only one
 * is necessary.  That's because Essential will "walk the graph", registering
 * all related domain classes automatically.
 * 
 * <p>
 * Also, note that the classes registered are not necessarily the classes that
 * appear in the classbar view in the UI; that depends upon their annotations
 * specific to the programming model (eg <tt>Lifecycle(instantiable=true)</tt>).
 * 
 * @author Dan Haywood
 */
public class SpringConfiguredDomainDefinition extends AbstractDomainDefinition {


	@Override
	protected Logger getLogger() {
		return Logger.getLogger(SpringConfiguredDomainDefinition.class);
	}

	
	//////////////////////////////////////////////////////////////////////
	// Initialization
	// Bundle
	////////////////////////////////////////////////////////////////////

	private Bundle _bundle;
	/**
	 * The (Eclipse) bundle representing the domain plugin.
	 *
	 * <p>
	 * To assist in the loading of domain classes (so that it can use 
	 * the appropriate <tt>ClassLoader</tt>).
	 */
	public Bundle getBundle() {
		return _bundle;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional, but if not specified then class loading will be attempted 
	 * using {@link Class#forName(java.lang.String)} rather than
	 * {@link Bundle#loadClass(java.lang.String)}.
	 * 
	 * <p>
	 * Implementation notes: propagates to a reference to 
	 * {@link BundleElseStandardClassLoader} that actually does the loading.
	 */
	public void setBundle(Bundle bundle) {
		_bundle = bundle;
		_classLoader.setBundle(bundle);
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
	

	private BundleElseStandardClassLoader _classLoader = new BundleElseStandardClassLoader();
	
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
