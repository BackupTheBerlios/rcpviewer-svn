/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;

import de.berlios.rcpviewer.gui.jobs.RunOperationJob;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Wraps an action on the actions view
 * @author Mike
 */
class ActionsViewActionProxy extends RunOperationJob {
	
	// lazily instantiated
	private String _displayText = null;
	private ActionsViewParameterProxy[] _params = null;

	/**
	 * Constructor takes the operation to wrap and the instance this applies to
	 * @param op
	 */
	ActionsViewActionProxy( IDomainObject obj, EOperation op ) {
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
			for ( Object param : getOp().getEParameters() ) {
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
	ActionsViewParameterProxy[] getParams() {
		if ( _params == null ) {
			EList list = getOp().getEParameters();
			_params = new ActionsViewParameterProxy[ list.size() ];
			for ( int i=0 ; i < _params.length ; i++ ) {
				_params[i] = new ActionsViewParameterProxy( 
						this, (EParameter)list.get( i ) );
			}	
		}
		return _params;
	}
	
	

}
