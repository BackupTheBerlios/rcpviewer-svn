package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Runs the passed operation on the passed object with the passed arguements.
 * <br>If the operation requires more args than are passed, opens a gui widget
 * to collect these.
 * @author Mike
 *
 */
public class RunOperationJob extends AbstractDomainObjectJob {

	private final EOperation _op;
	private final Object[] _args;
	
	/**
	 * Constructor where no arguments are passed
	 * @param object
	 * @param operation
	 */
	public RunOperationJob( IDomainObject object,
							EOperation operation ) {
		super( RunOperationJob.class.getName(), object );
		if ( operation == null ) throw new IllegalArgumentException();
		_op = operation;
		_args = new Object[ operation.getEParameters().size() ];
	}
	
	
	/**
	 * Constructor where args are passed - the number of argument must match
	 * the operation though some may be <code>null.</code>
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
		
		// do we have all the args we need?
		// later will have to deal with nulls
		boolean argsRequired = false;
		for ( Object arg : _args ) {
			if ( arg == null ) {
				argsRequired = true;
				break;
			}
		}
		if ( !argsRequired ) {
			getDomainObject().invokeOperation( _op, _args );
		}
		else {
			ActionArgsDisplay display = new ActionArgsDisplay(
					getDomainObject(),
					_op,
					_args );
			display.open();
		}
		return Status.OK_STATUS;
	}

}
