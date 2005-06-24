package de.berlios.rcpviewer.gui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.berlios.rcpviewer.domain.Domain;

/**
 * Creates <code>Job</code> that will initialise the <code>Domain</code>.
 * @see org.eclipse.core.runtime.jobs.Job
 * @see de.berlios.rcpviewer.domain.Domain;
 * @author Mike
 */
class DomainInitialiserFactory {
	
	public static final String DOMAIN_CLASS_EXTENSION_POINT
		= "de.berlios.rcpviewer.gui.domainclass"; 
	public static final String CLASS_PROPERTY = "class";
	
	/**
	 * Currently always returns an instance of <code>DefaultInitialiserJob</code>.
	 * @return
	 */
	static Job getInitialiserJob() {
		return new DefaultInitialiserJob();
	}
	

	// prevent instantiation
	private DomainInitialiserFactory() {
		super();
	}
	
	/**
	 * Initialises the <code>Domain</code> by looking for implementors of
	 * DOMAIN_CLASS_EXTENSION_POINT.  
	 * <br>Currently these are restricted to POJOS's with a no-arg constructor.
	 */
	private static class DefaultInitialiserJob extends Job {
		
		DefaultInitialiserJob() {
			super( DefaultInitialiserJob.class.getSimpleName() );
		}

		/**
		 * See class description.
		 * @see org.eclipse.core.internal.jobs.InternalJob#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) {
	        
			// fetch config elements
			IConfigurationElement[] elems
	        	= Platform.getExtensionRegistry()
	                  .getConfigurationElementsFor( DOMAIN_CLASS_EXTENSION_POINT );
			int num = elems.length;
			
			// get classes (checking they can be instantiated)
			Class<?>[] classes = new Class[ num ];
			try {
				for ( int i=0 ; i < num ; i++ ) {
					// nb: this demands a no-arg constructor
					Object obj = elems[i].createExecutableExtension(
							CLASS_PROPERTY );
					classes[i] = obj.getClass();
				}
			}
			catch ( CoreException ce ) {
				return ce.getStatus();	
			}
			
			// add to domain
			for ( int i=0 ; i < num ; i++ ) {
				Domain.instance().lookup( classes[i] );
			}
			Domain.instance().done();
			
				
			return Status.OK_STATUS;
		}
		
		
		
	}

}
