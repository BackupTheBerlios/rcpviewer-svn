/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;

import de.berlios.rcpviewer.gui.jobs.NotImplementedJob;
import de.berlios.rcpviewer.gui.jobs.RunOperationJob;
import de.berlios.rcpviewer.gui.views.actions.IActionsViewPage;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Page supplied by <code>DefaultEditor.getAdapter(IActionsViewPage.class)</code>
 * <br>Each editor instance holds its own instance of this class.
 * @author Mike
 */
class ActionsViewPage extends Page implements IActionsViewPage {

	private final IDomainObject _domainObject; 
	
	// temporary
	private ListViewer _viewer = null;

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
		_viewer = new ListViewer( 
				parent, 
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
		_viewer.setContentProvider( new ActionsViewContentProvider() );
		_viewer.setLabelProvider( new ActionsViewLabelProvider() );
		_viewer.addOpenListener( new IOpenListener(){
		    public void open(OpenEvent event) {
		    	runOp();
		    }
		});
		
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
	
	// run the selected operation
	private void runOp() {
    	// get op
		if ( _viewer.getSelection().isEmpty() ) return;
    	Object selected
    		= ((StructuredSelection)_viewer.getSelection()).getFirstElement();
    	if ( selected instanceof ErrorInput ) return;
    	assert selected instanceof EOperation;
    	EOperation op = (EOperation)selected;
    	
    	// choose job
    	Job job;
    	if ( op.getEParameters().isEmpty() ) {
    		// no-arg op can be run immediately
    		job = new RunOperationJob( _domainObject, op );
    	}
    	else {
    		job = new NotImplementedJob();
    	}
    	
    	// run job
    	job.schedule();
    	
	}

}
