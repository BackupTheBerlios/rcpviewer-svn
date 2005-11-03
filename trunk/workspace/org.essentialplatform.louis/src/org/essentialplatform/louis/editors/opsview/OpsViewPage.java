/**
 * 
 */
package org.essentialplatform.louis.editors.opsview;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.Page;
import org.essentialplatform.louis.jobs.AbstractUserJob;
import org.essentialplatform.louis.util.SWTUtil;
import org.essentialplatform.louis.views.ops.IOpsViewPage;

import org.essentialplatform.runtime.session.IDomainObject;

/**
 * Page supplied by <code>DefaultEditor.getAdapter(IOpsViewPage.class)</code>
 * <br>Each editor instance holds its own instance of this class.
 * @author Mike
 */
public class OpsViewPage<T> extends Page implements IOpsViewPage {
	
	private final IDomainObject<T> _domainObject; 
	
	// temporary
	private TreeViewer _viewer = null;

	/**
	 * Constructor passed parent domain object.
	 * @param object
	 */
	public OpsViewPage( IDomainObject<T> object ) {
		super();
		assert object != null;
		_domainObject = object;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
	 */
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		// create viewer:
		// note : cell editors for the tree must be set on every change of 
		// selection BEFORE anything else happens hence a listener must be 
		// added before any other listeners 
		int style = SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER 
						| SWT.FULL_SELECTION;
		_viewer = new TreeViewer( parent, style){
			@Override
			protected void hookControl( final Control control) {
				final OpsViewCellEditorFactory factory
					= new OpsViewCellEditorFactory();
				control.addMouseListener( new MouseAdapter(){
					public void mouseDown(MouseEvent e) {
	                    TreeItem item = ((Tree)control).getItem(
	                    		new Point(e.x, e.y) );
	                    if ( item != null ) {
	                    	setCellEditors( factory.getCellEditors(
	                    			(Tree)control, item.getData() ) );
	                    }
					}
				});
				super.hookControl( control );
			}
		};	
		
		_viewer.setContentProvider( new OpsViewContentProvider() );
		// label provider also a colour provider
		_viewer.setLabelProvider( new OpsViewLabelProvider() );
		_viewer.getTree().setLinesVisible( true );
		
		// columns
		new TreeColumn( _viewer.getTree(), SWT.RIGHT );
		new TreeColumn( _viewer.getTree(), SWT.LEFT );
		
		// cell modification - note cell editors set on viewer construction
		_viewer.setColumnProperties( new String[] { 
				OpsViewCellModifier.LABEL_COLUMN, 
				OpsViewCellModifier.VALUE_COLUMN } );
		_viewer.setCellModifier( new OpsViewCellModifier(_viewer  ) );
		
		// dbl click logic
		_viewer.addOpenListener( new IOpenListener(){
		    public void open(OpenEvent event) {
		    	ISelection selection = event.getSelection();
		    	if ( selection.isEmpty() ) return;
		    	Object obj = ((StructuredSelection)selection).getFirstElement();
		    	if ( obj instanceof OpsViewActionProxy ) {
		    		OpsViewActionProxy proxy = (OpsViewActionProxy)obj;
		    		if ( proxy.isValid() ) {
		    			proxy.schedule();
		    		}
		    		// TODO - if not error message?
				}
		    }
		});
		
		// tooltip logic delegated
		new OpsViewToolTipController( _viewer );
		
		// DnD logic delegated
		new OpsViewDnDController( _viewer );
		
		// set input so can then pack columns (expand & contract so that
		// branch width can be taken into account)
		_viewer.setInput( _domainObject );
		_viewer.expandAll();
		for ( TreeColumn column : _viewer.getTree().getColumns() ) {
			column.pack();
		}
		_viewer.collapseAll();
		
		// cannot set last column until tree is visible
		Job resize = new AbstractUserJob( "TreeColumnResize") { //$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor) {
				SWTUtil.autosizeLastColumn( _viewer.getTree() );
				return Status.OK_STATUS;
			}
		};
		resize.schedule();
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

}
