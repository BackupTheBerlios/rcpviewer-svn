package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.essentialplatform.core.domain.Domain;
import org.osgi.framework.Bundle;

/**
 * An implementation of {@link IDomainDefinition} whose properties are defaulted
 * to the implementations that read from various extension points.
 * 
 * <p>
 * Developers who are familiar and comfortable with Eclipse extension points
 * can register this class in Spring as the definition of <tt>domain</tt> bean,
 * and thereafter configure their application using the appropriate
 * extension points documented elsewhere.
 * 
 * <p>
 * Note that the name of the domain must still be provided via Spring.  
 * 
 * <p>
 * Initialises the {@link Domain} by looking for implementors of 
 * DOMAIN_CLASS_EXTENSION_POINT.
 * 
 * <p>
 * Depending on the implementation chosen, it may or may not be necessary to
 * register every domain class.  For example, on the client-side it isn't 
 * because Essential will "walk the graph", registering all related domain 
 * classes automatically.  However, on the server-side using Hibernate it 
 * will be, because every annotated class must be added to Hibernate's
 * configuration.
 * 
 * <p>
 * Note that the classes registered are not necessarily the classes that
 * appear in the classbar view in the UI; that depends upon their annotations
 * specific to the programming model (eg <tt>Lifecycle(instantiable=true)</tt>).
 * 
 * <p>
 * Example usage:
 * <pre>
 * &lt;bean id="domain" class="org.essentialplatform.louis.app.ExtensionPointReadingDomainDefinition">
 *     &lt;property name="name" value="marketing"/>
 * &lt;/bean>
 * </pre>
 * 
 * @author Dan Haywood
 */
public final class ExtensionPointReadingDomainDefinition extends AbstractDomainDefinition {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(ExtensionPointReadingDomainDefinition.class);
	}

	public static final String DOMAIN_CLASS_EXTENSION_POINT = "org.essentialplatform.runtime.shared.domainclass";  //$NON-NLS-1$
	public static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$

	

	//////////////////////////////////////////////////////////////////////
	// Initialization
	// Bundle
	////////////////////////////////////////////////////////////////////

	/**
	 * Invoked by {@link org.essentialplatform.louis.Bootstrap}.
	 */
	public void setBundle(Bundle bundle) {
		_bundle = bundle;
	}

	private Bundle _bundle;
	/**
	 * The (Eclipse) bundle representing the domain plugin.
	 *
	 * <p>
	 * As per {@link #setBundle(Bundle)}.
	 *
	 * <p>
	 * Set by Essential itself (rather than through Spring, say), primarily
	 * to assist in the verification of domain classes (so that it can use 
	 * the appropriate <tt>ClassLoader</tt>).
	 */
	public Bundle getBundle() {
		return _bundle;
	}

	
	

	@Override
	public void doRegisterClasses() throws DomainBootstrapException {
		
		// fetch config elements
		IConfigurationElement[] elems =
        	Platform.getExtensionRegistry().getConfigurationElementsFor( DOMAIN_CLASS_EXTENSION_POINT );

		// get classes (checking they can be instantiated)
		List<Class<?>> javaClasses = new ArrayList<Class<?>>();
		for ( int i=0 ; i < elems.length ; i++ ) {
			try {
				// nb: this demands a no-arg constructor
				Object obj = elems[i].createExecutableExtension(CLASS_PROPERTY);
				javaClasses.add(obj.getClass());
			} catch (CoreException ex) {
				String msg = String.format(
					"IConfigurationElement#createExecutableExtension(\"%s\") failed for '%s'", CLASS_PROPERTY, elems[i].getValue());   //$NON-NLS-1$
				getLogger().error(msg);
				throw new DomainBootstrapException(msg, ex);
			}
		}
		
		// add to domain
		for (Class<?> javaClass: javaClasses) {
			registerClass(javaClass);
		}
	}


}
