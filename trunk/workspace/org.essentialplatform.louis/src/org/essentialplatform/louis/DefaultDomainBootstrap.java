package org.essentialplatform.louis;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;

/**
 * Initialises the <code>Domain</code> by looking for implementors of 
 * DOMAIN_CLASS_EXTENSION_POINT.  
 * <br>Currently these are restricted to POJOS's with a no-arg constructor.
 * @author Mike
 */
class DefaultDomainBootstrap implements IDomainBootstrap {
	
	private final static Logger LOG = Logger.getLogger(DefaultDomainBootstrap.class);

	public static final String DOMAIN_CLASS_EXTENSION_POINT
		= "org.essentialplatform.louis.domainclass";  //$NON-NLS-1$
	public static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.domain.runtime.IDomainBootstrap#registerClasses()
	 * 
	 */
	public void registerClasses() throws CoreException {
		// fetch config elements
		IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( DOMAIN_CLASS_EXTENSION_POINT );
		int num = elems.length;
		
		// get classes (checking they can be instantiated)
		Class<?>[] classes = new Class[ num ];
		for ( int i=0 ; i < num ; i++ ) {
			// nb: this demands a no-arg constructor
			Object obj = elems[i].createExecutableExtension(
					CLASS_PROPERTY );

			LOG.info("Registering " + obj.getClass().getName()); //$NON-NLS-1$

			classes[i] = obj.getClass();
		}		
		// add to domain
		for ( int i=0 ; i < num ; i++ ) {
			Domain.instance().lookup( classes[i] );
		}
	}
	

}
