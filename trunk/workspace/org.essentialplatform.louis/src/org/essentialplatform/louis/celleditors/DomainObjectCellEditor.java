/**
 * 
 */
package org.essentialplatform.louis.celleditors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.essentialplatform.louis.LouisPlugin;

import org.essentialplatform.domain.IDomainClass;

/**
 * @author Mike
 */
class DomainObjectCellEditor extends DialogCellEditor {

	private final IDomainClass _class;
	
	// internal state controlling focus lost actions
	private boolean _dialogOpen = false;
	
	/**
	 * Requires domain class.
	 * @param
	 */
	DomainObjectCellEditor( IDomainClass clazz ) {
		super();
		_class = clazz;
	}
	
	/**
	 * Fixes deficiency in super's implementation - now automatically deactivates
	 * if focus lost (as other CellEditor subclasses do).  
	 * <br>Implementtation should be robust enough to handle changes to 
	 * super's internals.
	 * @see org.eclipse.jface.viewers.DialogCellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		FocusAdapter listener  = new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	if ( !_dialogOpen ) {
            		DomainObjectCellEditor.this.focusLost();
            	}
            }
		};
		if ( control instanceof Composite ) {
			for ( Control child : ((Composite)control).getChildren() ) {
				child.addFocusListener( listener );
			}
		}
		else {
			control.addFocusListener( listener );
		}
		return control;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	@Override
	protected void updateContents(Object value) {
		String display;
		if ( value == null ) {
			display = ""; //$NON-NLS-1$
		}
		else {
			display = LouisPlugin.getText( value );
			
		}
		getDefaultLabel().setText( display );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		try {
			_dialogOpen = true;
			MessageDialog.openWarning(
					null,
					null,
					LouisPlugin.getResourceString( "NotImplementedJob.Msg" ) ); //$NON-NLS-1$
			return null;
		}
		finally {
			_dialogOpen = false;
		}
		
	}
	
	

}
