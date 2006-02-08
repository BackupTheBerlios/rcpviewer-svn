/**
 * 
 */
package org.essentialplatform.louis;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IBinding;

/**
 * Superclass for all bootstrap jobs - ensures they do not run
 * in parallel
 * @author Mike
 */
abstract class AbstractBootstrapJob extends Job {
	
	// straight out of help file!
	private static final ISchedulingRule MUTEX = new ISchedulingRule(){
      public boolean isConflicting(ISchedulingRule rule) {
          return rule == this;
       }
       public boolean contains(ISchedulingRule rule) {
          return rule == this;
       }
	};

	/**
	 * @param name
	 */
	protected AbstractBootstrapJob(String name) {
		super(name);
		setRule( MUTEX );
		_binding = Binding.getBinding();
	}

	/**
	 * Job family linked by class object.
	 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
	 */
	@Override
	public boolean belongsTo(Object family) {
		return AbstractBootstrapJob.class == family;
	}
	
	
	/**
	 * Sets the {@link IBinding} for this thread and then delegates to
	 * {@link #doRun(IProgressMonitor)} to actually do the work.
	 * 
	 * <p>
	 * Template pattern.
	 */
	@Override
	protected final IStatus run(IProgressMonitor monitor) {
		Binding.setBinding(getBinding());
		return doRun(monitor);
	}

	
	protected abstract IStatus doRun(IProgressMonitor monitor);


	private IBinding _binding;
	/**
	 * Binding of the instantiating thread.
	 * 
	 * @return
	 */
	protected IBinding getBinding() {
		return _binding;
	}


}
