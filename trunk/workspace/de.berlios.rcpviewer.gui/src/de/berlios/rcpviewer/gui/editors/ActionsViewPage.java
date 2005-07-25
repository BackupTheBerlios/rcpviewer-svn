/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
	private TreeViewer _viewer = null;

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
		
		// create viewer
		_viewer = new TreeViewer( 
				parent, 
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
		_viewer.setContentProvider( new ActionsViewContentProvider() );
		_viewer.setLabelProvider( new ActionsViewLabelProvider() );
		
		// dbl click logic
		_viewer.addOpenListener( new IOpenListener(){
		    public void open(OpenEvent event) {
		    	Object selected = getSelected( ActionsViewActionProxy.class );
		    	if ( selected != null ) {
					((ActionsViewActionProxy)selected).schedule();
				}
		    }
		});
		
		// DnD logic delegated
		new ActionsViewDnDController( _viewer );
		
		_viewer.setInput( _domainObject );
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#getControl()
	 */
	@Override
	public Control getControl() {
		return _viewer.getControl();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#setFocus()
	 */
	@Override
	public void setFocus() {
		_viewer.getControl().setFocus();
	}

	// commonly used - predicated on SWT.SINGLE viewer
	private Object getSelected( Class expectedClass ) {
		assert expectedClass != null;
		assert _viewer != null;
    	if ( _viewer.getSelection().isEmpty() ) return null;
    	Object obj = ((StructuredSelection)
    			_viewer.getSelection()).getFirstElement();
    	if ( expectedClass.equals( obj.getClass() ) ){
    		return obj;
    	}
    	return null;
	}

}
