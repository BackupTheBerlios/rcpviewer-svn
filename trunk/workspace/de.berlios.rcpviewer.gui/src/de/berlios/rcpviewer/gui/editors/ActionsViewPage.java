/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.Page;

import de.berlios.rcpviewer.gui.views.actions.IActionsViewPage;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Page supplied by <code>DefaultEditor.getAdapter(IActionsViewPage.class)</code>
 * <br>Each editor instance holds its own instance of this class.
 * @author Mike
 */
class ActionsViewPage extends Page implements IActionsViewPage {

	private final IDomainObject _domainObject; 
	
	// temporary
	private Text _text = null;

	/**
	 * Constructor passed parent domain object.
	 * @param object
	 */
	ActionsViewPage( IDomainObject object ) {
		super();
		assert object != null;
		_domainObject = object;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		_text = new Text(parent, SWT.NONE );
		StringBuffer sb = new StringBuffer();
		sb.append( _domainObject.getPojo().getClass().getSimpleName() );
		sb.append( ":" );
		sb.append( _domainObject.getPojo().hashCode() );
		_text.setText( sb.toString() );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#getControl()
	 */
	@Override
	public Control getControl() {
		return _text;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#setFocus()
	 */
	@Override
	public void setFocus() {
		_text.setFocus();
	}

}
