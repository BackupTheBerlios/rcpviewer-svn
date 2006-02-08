package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
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
	

	@Override
	public void doRegisterClasses() throws DomainBootstrapException {
		
		List<Class<?>> classes = new ArrayList<Class<?>>();

		// check each className represents a no-arg instantiable class ...
		for (String className: _classNames) {
			Class<?> javaClass;
			try {
				javaClass = getBundle().loadClass(className);
				classes.add(javaClass);
			} catch (ClassNotFoundException ex) {
				String msg = String.format(
						"Bundle#loadClass(\"%s\") failed", className);   //$NON-NLS-1$
				getLogger().error(msg);
				throw new DomainRegistryException(msg, ex);
			}
		}
		
		// ... then add to domain
		for (Class<?> javaClass: classes) {
			registerClass(javaClass);
		}
	}
	
}
