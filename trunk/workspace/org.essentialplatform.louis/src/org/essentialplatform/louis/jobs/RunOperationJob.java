package org.essentialplatform.louis.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.essentialplatform.louis.LouisPlugin;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.domain.bindings.IObjectOperationClientBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOperation;

/**
 * Runs the passed operation on the passed object with the passed arguements.
 * <br>If the operation requires more args than are passed, opens a gui widget
 * to collect these.
 * @author Mike
 *
 */
public class RunOperationJob extends AbstractDomainObjectJob {

	private final IDomainClass.IOperation _iOperation;
	private final Object[] _args;
	
	/**
	 * Constructor where no arguments are passed
	 * @param object
	 * @param iOperation
	 */
	public RunOperationJob( IDomainObject object,
							IDomainClass.IOperation iOperation ) {
		this(object, iOperation, new Object[ iOperation.getEOperation().getEParameters().size() ]);
	}
	
	
	/**
	 * Constructor where args are passed - the number of argument must match
	 * the operation though some may be <code>null.</code>
	 * @param object
	 * @param operation
	 */
	public RunOperationJob( IDomainObject object,
							IDomainClass.IOperation iOperation,
							Object[] args ) {
		super( RunOperationJob.class.getName(), object );
		if ( iOperation == null ) throw new IllegalArgumentException();
		if ( args == null ) throw new IllegalArgumentException();
		if ( args.length != iOperation.getEOperation().getEParameters().size() ) {
			throw new IllegalArgumentException();
		}
		_iOperation = iOperation;
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
			int num = _args.length;
			final IObjectOperation op = getDomainObject().getOperation( _iOperation );
			IObjectOperationClientBinding opBinding = (IObjectOperationClientBinding)op;
			for ( int i=0 ; i < num ; i++ ) {
				Object arg;
				if ( _args[i] instanceof IDomainObject ) {
					arg = ((IDomainObject<?>)_args[i]).getPojo();
				}
				else {
					arg = _args[i];
				}
				opBinding.setArg(0, arg);
			}
			opBinding.invokeOperation();
			ReportJob report = new ReportJob(
					LouisPlugin.getResourceString( "RunOperationJob.Ok"), //$NON-NLS-1$
					ReportJob.INFO,
					_iOperation.getName() );
			report.schedule();
		}
		else {
			assert false;
		}
		return Status.OK_STATUS;
	}

	
	/* protected methods */

	/**
	 * @return Returns the args.
	 */
	protected Object[] getArgs() {
		return _args;
	}


	/**
	 * @return Returns the op.
	 */
	protected IDomainClass.IOperation getOp() {
		return _iOperation;
	}

	
	

}
