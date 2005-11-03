package org.essentialplatform.louis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.runtime.domain.runtime.IDomainBootstrap;
import org.essentialplatform.runtime.progmodel.rcpviewer.RcpViewerProgModelDomainBuilder;

/**
 * Wraps the passed <code>IDomainBootstrap</code>.
 * @author Mike
 */
class DomainBootstrapJob extends AbstractBootstrapJob {
	
	private final IDomainBootstrap _bootstrap;
	
	DomainBootstrapJob( IDomainBootstrap bootstrap ) {
		super( DomainBootstrapJob.class.getSimpleName() );
		assert bootstrap != null;
		this._bootstrap = bootstrap;
	}



	/**
	 * Runs the wrapped <code>IDomainBootstrap</code> and adds other necessary 
	 * builders.
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {  
		try {
			_bootstrap.registerClasses();
			Domain.instance().addBuilder( 
					new RcpViewerProgModelDomainBuilder() );
		}
		catch ( CoreException ce ) {
			return ce.getStatus();	
		}
		Domain.instance().done();	
		return Status.OK_STATUS;	
	}
}
