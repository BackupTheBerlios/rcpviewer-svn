package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;

/**
 * Wraps the passed <code>IDomainBootstrap</code>.
 * @author Mike
 */
public class DomainBootstrapJob extends Job {
	
	private final IDomainBootstrap bootstrap;
	
	public DomainBootstrapJob( IDomainBootstrap bootstrap ) {
		super( DomainBootstrapJob.class.getSimpleName() );
		assert bootstrap != null;
		this.bootstrap = bootstrap;
	}


	/**
	 * Runs the wrapped <code>IDomainBootstrap</code>.
	 * @see org.eclipse.core.internal.jobs.InternalJob#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {  
		try {
			bootstrap.registerClasses();
		}
		catch ( CoreException ce ) {
			return ce.getStatus();	
		}
		Domain.instance().done();	
		return Status.OK_STATUS;	
	}
}
