/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.jface.viewers.LabelProvider;

import de.berlios.rcpviewer.gui.widgets.ErrorInput;

/**
 * @author Mike
 *
 */
public class ActionsViewLabelProvider extends LabelProvider {

	/**
	 * 
	 */
	ActionsViewLabelProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( element instanceof ErrorInput ) {
			return ((ErrorInput)element).getMessage();
		}
		else if ( element instanceof EOperation ) {
			return buildName( (EOperation)element );
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	
	private String buildName( EOperation op ) {
		assert op != null;
		StringBuffer sb = new StringBuffer();
		sb.append( op.getName() );
		sb.append( "(" ); //$NON-NLS-1$
		boolean firstParam = true;
		for ( Object param : op.getEParameters() ) {
			if ( firstParam ) {
				firstParam = false;
			}
			else {
				sb.append( ", "); //$NON-NLS-1$
			}
			sb.append( ((EParameter)param).getName() );
		}	
		sb.append( ")" ); //$NON-NLS-1$
		return sb.toString();
	}
	
	

}
