package org.essentialplatform.louis;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.louis.util.ConfigElementSorter;

import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreBootStrap;

/**
 * Initialises the passed object store by looking for implementors
 * of <code>IObjectStoreBootstrap</code> and running their
 * <code>populate()</code> methods.
 * @author Mike
 */
class ObjectStoreBootstrapJob extends AbstractBootstrapJob {
	
	private final IObjectStore _store;
	
	/**
	 * Requires a valid reference
	 * @param store
	 */
	ObjectStoreBootstrapJob( IObjectStore store ) {
		super( ObjectStoreBootstrapJob.class.getSimpleName() );
		if ( store == null ) throw new IllegalArgumentException();
		_store = store;
	}
	
	/**
	 * 
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {  
		try {
	        IConfigurationElement[] elems
        		= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( IObjectStoreBootStrap.EXTENSION_POINT_ID );
			Arrays.sort( elems, new ConfigElementSorter() );
			for ( IConfigurationElement element : elems ) {
				Object obj = element.createExecutableExtension( "class" ); //$NON-NLS-1$
				assert obj instanceof IObjectStoreBootStrap;
				((IObjectStoreBootStrap)obj).populate( _store );
				// use ISafeRunnable ..?  Nah - want to know about errors
			}
		}
		catch ( CoreException ce ) {
			return ce.getStatus();	
		}	
		return Status.OK_STATUS;	
	}
}
