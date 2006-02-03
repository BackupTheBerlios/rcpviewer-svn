package org.essentialplatform.louis;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.louis.runtime.LouisProgModelRuntimeBuilder;
import org.essentialplatform.runtime.shared.domain.IDomainBootstrap;

/**
 * Wraps the passed <code>IDomainBootstrap</code>.
 * 
 * @author Mike
 */
public class DomainBootstrapJob extends AbstractBootstrapJob {
	
	private final IDomainBootstrap _bootstrap;
	
	public DomainBootstrapJob( IDomainBootstrap bootstrap ) {
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
		_bootstrap.registerClasses();
		Domain.instance().addBuilder( 
				new LouisProgModelRuntimeBuilder() );
		Domain.instance().done();	
		return Status.OK_STATUS;	
	}
}
