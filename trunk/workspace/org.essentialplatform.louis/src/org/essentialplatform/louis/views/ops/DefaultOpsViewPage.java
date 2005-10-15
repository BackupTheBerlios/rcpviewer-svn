/**
 * 
 */
package org.essentialplatform.louis.views.ops;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.Page;
import org.essentialplatform.louis.LouisPlugin;


/**
 * Default option when activated <code>IEditorPart</code> has no
 * <code>IOpsViewPage.class</code> adapter.  Simply displays a 
 * default bit of text.
 * @author Mike
 */
class DefaultOpsViewPage extends Page implements IOpsViewPage {

	private Text _text = null;
	
	/**
	 * 
	 */
	DefaultOpsViewPage() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		_text = new Text(parent, SWT.NONE );
		_text.setText( LouisPlugin.getResourceString( "DefaultOpsViewPage.Text") ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() {
		return _text;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() {
		_text.setFocus();
	}
	
	


}
