/**
 * 
 */
package org.essentialplatform.louis.editors.opsview;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EParameter;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.louis.jobs.RunOperationJob;

/**
 * Wraps an op on the ops view
 * @author Mike
 */
class OpsViewActionProxy extends RunOperationJob  {
	
	// lazily instantiated
	private String _displayText = null;
	private OpsViewParameterProxy[] _params = null;

	/**
	 * Constructor takes the operation to wrap and the instance this applies to
	 * @param op
	 */
	<T> OpsViewActionProxy( IDomainObject<T> obj, IDomainClass.IOperation op ) {
		super( obj, op);
	}
	
	/**
	 * Sets args based on proxy params
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		for ( int i=0; i < _params.length ; i++ ) {
			getArgs()[i] = _params[i].getValue();
		}
		return super.runInUIThread( monitor );
	}
	
	/**
	 * Display text for label provider
	 * @return
	 */
	String getText() {
		if ( _displayText == null ) {
			StringBuffer sb = new StringBuffer();
			sb.append( getOp().getName() );
			sb.append( "(" ); //$NON-NLS-1$
			boolean firstParam = true;
			for ( Object param : getOp().getEOperation().getEParameters() ) {
				if ( firstParam ) {
					firstParam = false;
				}
				else {
					sb.append( ", "); //$NON-NLS-1$
				}
				sb.append( ((EParameter)param).getName() );
			}	
			sb.append( ")" ); //$NON-NLS-1$
			_displayText = sb.toString();
		}
		return _displayText;
	}

	/**
	 * @return Returns the params.
	 */
	OpsViewParameterProxy[] getParams() {
		if ( _params == null ) {
			EList list = getOp().getEOperation().getEParameters();
			_params = new OpsViewParameterProxy[ list.size() ];
			for ( int i=0 ; i < _params.length ; i++ ) {
				_params[i] = new OpsViewParameterProxy( 
						this, (EParameter)list.get( i ) );
			}	
		}
		return _params;
	}
	
	/**
	 * Whether the operation can be run.
	 * <br>Currenly simply asks all parameters if they are valid.
	 * <br>Later will add prerequisite checks.
	 * @return
	 */
	boolean isValid() {
		for ( OpsViewParameterProxy proxy : getParams() ) {
			if ( !proxy.isValid() ) return false;
		}
		return true;
	}

}
