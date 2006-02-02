package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;

/**
 * Initialises the {@link Domain} by looking for implementors of 
 * DOMAIN_CLASS_EXTENSION_POINT.
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
 * @author Mike
 */
public final class ExtensionPointReadingDomainBootstrap 
	extends AbstractDomainBootstrap {
	
	protected Logger getLogger() { return Logger.getLogger(ExtensionPointReadingDomainBootstrap.class); } 

	public static final String DOMAIN_CLASS_EXTENSION_POINT = "org.essentialplatform.louis.domainclass";  //$NON-NLS-1$
	public static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$

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
