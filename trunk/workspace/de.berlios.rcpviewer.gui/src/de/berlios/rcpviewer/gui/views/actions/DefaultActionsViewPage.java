/**
 * 
 */
package de.berlios.rcpviewer.gui.views.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.Page;

import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Default option when activated <code>IEditorPart</code> has no
 * <code>IActionsViewPage.class</code> adapter.  Simply displays a 
 * default bit of text.
 * @author Mike
 */
class DefaultActionsViewPage extends Page implements IActionsViewPage {

	private Text _text = null;
	
	/**
	 * 
	 */
	DefaultActionsViewPage() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		_text = new Text(parent, SWT.NONE );
		_text.setText( GuiPlugin.getResourceString( "DefaultActionsViewPage.Text") );
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
