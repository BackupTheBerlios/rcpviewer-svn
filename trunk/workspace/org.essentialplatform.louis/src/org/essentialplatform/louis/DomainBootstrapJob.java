package org.essentialplatform.louis;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

/**
 * Wraps the passed <code>IDomainRegistrar</code>.
 * 
 * @author Mike
 */
public class DomainBootstrapJob extends AbstractBootstrapJob {
	
	private final IDomainRegistry _registry;
	
	public DomainBootstrapJob( IDomainRegistry registry ) {
		super( DomainBootstrapJob.class.getSimpleName() );
		assert registry != null;
		_registry = registry;
	}



	/**
	 * Runs the wrapped <code>IDomainRegistrar</code> and adds other necessary 
	 * builders.
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {  
		_registry.registerClassesInDomains();
		return Status.OK_STATUS;	
	}
}
