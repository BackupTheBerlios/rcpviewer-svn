package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Runs the passed operation on the passed object with the passed arguements.
 * <br>This job validates the passed arguments.
 * @author Mike
 *
 */
public class RunOperationJob extends AbstractDomainObjectJob {

	private final EOperation _op;
	private final Object[] _args;
	
	/**
	 * Constructor where op has no args.
	 * @param object
	 * @param operation
	 */
	public RunOperationJob( IDomainObject object,
							EOperation operation ) {
		this( object, operation, new Object[0] );
	}
	
	/**
	 * Constructor where op has a single arg.
	 * @param object
	 * @param operation
	 */
	public RunOperationJob( IDomainObject object,
							EOperation operation,
							Object arg ) {
		this( object, operation, new Object[]{ arg } );
	}
	
	/**
	 * Constructor where op has multiple args.
	 * @param object
	 * @param operation
	 */
	public RunOperationJob( IDomainObject object,
							EOperation operation,
							Object[] args ) {
		super( RunOperationJob.class.getName(), object );
		if ( operation == null ) throw new IllegalArgumentException();
		if ( args == null ) throw new IllegalArgumentException();
		if ( args.length != operation.getEParameters().size() ) {
			 throw new IllegalArgumentException();
		}
		_op = operation;
		_args = args;
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		getDomainObject().invokeOperation( _op, _args );
		return Status.OK_STATUS;
	}

}
