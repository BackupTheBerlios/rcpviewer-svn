/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

/**
 * Generic form part for single references to <code>IDomainObject</code>'s.
 * @author Mike
 */
class InvalidReferencePart implements IFormPart {
	

	/**
	 * @param ref - cannot be <code>null</code> and must represent a single reference
	 * @param reason - display test for the user why the reference is invalid
	 * @param parent - cannot be <code>null</code>
	 * @param columnwidths - can be <code>null</code>
	 */
	InvalidReferencePart( EReference ref,
				   		  String reason,
				   		  Composite parent, 
				   		  int[] columnWidths ) {
		assert parent != null;
		assert ref != null;
		assert reason != null;
		// column widths can be null;

		// gui generation
		parent.setLayout( new GridLayout( 2, false ) );
		
		// label for reference
		GridData labelData = new GridData();
		if ( columnWidths != null 
				&& columnWidths.length == 2 
					&& columnWidths[0] != 0 ) {
			labelData.widthHint = columnWidths[0];
		}
		Label label = new Label( parent, SWT.RIGHT );
		label.setLayoutData( labelData );
		label.setBackground( parent.getBackground() );
		label.setText( ref.getName() + ":" ); //$NON-NLS-1$
		
		// reason why invalid
		GridData reasonData = new GridData();
		if ( columnWidths != null 
				&& columnWidths.length == 2 
					&& columnWidths[1] != 0 ) {
			reasonData.widthHint = columnWidths[1];
		}
		Label reasonLabel = new Label( parent, SWT.LEFT );
		reasonLabel.setLayoutData( reasonData );
		label.setBackground( parent.getBackground() );
		reasonLabel.setText( reason );
		reasonLabel.setForeground( parent.getDisplay().getSystemColor( 
				SWT.COLOR_DARK_RED ) );
	}
	
	
	/* IFormPart contract */

	/**
	 * Creates GUI.
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		// does nowt
	}

}
