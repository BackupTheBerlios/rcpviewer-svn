package mikespike3;

import mikespike3.editors.DefaultEditor;
import mikespike3.editors.DefaultEditorInput;
import mikespike3.model.IModelListener;
import mikespike3.model.Model;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ObjectListView extends ViewPart {
	
	private TableViewer viewer = null;
	
	public ObjectListView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider( new ViewContentProvider() );
		viewer.setLabelProvider( new ViewLabelProvider());
		viewer.setInput( getViewSite() );
		
		// add model listening (see test 6)
		final IModelListener listener = new IModelListener() {
			public void modifiedEvent() {
				viewer.refresh();
			}
		};
		Model.getInstance().add( listener );
		viewer.getControl().addDisposeListener( new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				Model.getInstance().remove( listener );
			}
		});
		
		// add mouse listening
		viewer.getControl().addMouseListener( new MouseAdapter(){
		    public void mouseDoubleClick(MouseEvent event) {				
				if ( !viewer.getSelection().isEmpty() ) {
					try {
						Object obj = ((StructuredSelection)
								viewer.getSelection()).getFirstElement();
						PlatformUI.getWorkbench()
						          .getActiveWorkbenchWindow()
						          .getActivePage()
						          .openEditor( new DefaultEditorInput( obj ), 
										       DefaultEditor.class.getName() );
					}
					catch ( PartInitException pie ) {
						// no visible error msg yet
						Plugin.getDefault().getLog().log( pie.getStatus() );
					}
				}
		    }
		});

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * @author Mike
	 */
	private class ViewContentProvider implements IStructuredContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return Model.getInstance().getObjects();
		}
	}
	
	/**
	 * @author Mike
	 */
	private class ViewLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			return element.getClass().getName();
		}
		
		
	}
}
