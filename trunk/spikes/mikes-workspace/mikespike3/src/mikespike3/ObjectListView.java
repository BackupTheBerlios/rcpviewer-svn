package mikespike3;

import mikespike3.editors.DefaultEditor;
import mikespike3.editors.DefaultEditorInput;
import mikespike3.model.IModelListener;
import mikespike3.model.Model;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
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
		
		// add model listening
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
		
		// create actions
		final OpenAction openAction = new OpenAction( viewer );
		final OpenWithAction openWithAction = new OpenWithAction( viewer );
		
		// add right-click menu
        MenuManager mgr = new MenuManager();
		mgr.add( openAction );
		mgr.add( openWithAction );
        Menu menu = mgr.createContextMenu(  viewer.getControl() );
		viewer.getControl().setMenu( menu );
		
		// dbl-click equivalent to open action
		viewer.getControl().addMouseListener( new MouseAdapter(){
		    public void mouseDoubleClick(MouseEvent event) {				
				openAction.run();
		    }
		});

	}

	@Override
	public void setFocus() {
		assert viewer != null;
		viewer.getControl().setFocus();
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
	
	/**
	 * 
	 * @author Mike
	 */
	private class OpenAction extends Action {

		public OpenAction( Viewer viewer ) {
			super();
			assert viewer != null;
			setText( "Open" );
		}
		
		public void run() {
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
	}
	
	/**
	 * 
	 * @author Mike
	 */
	private class OpenWithAction extends Action implements IMenuCreator {
		
		public OpenWithAction( Viewer viewer ) {
			super( "Open With", IAction.AS_DROP_DOWN_MENU ) ;
			setMenuCreator( this );
		}
		
		public Menu getMenu(Control parent) {
			return null;
		}
		
		public Menu getMenu(Menu parent) {
			Menu menu = new Menu( parent );
			new ActionContributionItem( new Action("Action 1"){} ).fill( menu, 0 );
			new ActionContributionItem( new Action("Action 2"){} ).fill( menu, 1 );
			new ActionContributionItem( new Action("Action 3"){} ).fill( menu, 2 );
			return menu;
		}
		
		public void dispose() {
		}
	}
}
