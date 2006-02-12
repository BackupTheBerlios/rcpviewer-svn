package org.essentialplatform.louis;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

/**
 * Wraps the passed {@link IDomainRegistry}.
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
	 * Registers classes using the wrapped <tt>IDomainRegistry</tt>.
	 */
	@Override
	protected IStatus doRun(IProgressMonitor monitor) {  
		_registry.registerClassesInDomains();
		return Status.OK_STATUS;	
	}
}
