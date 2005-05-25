package mikespike3.views;


import mikespike3.model.IModelListener;
import mikespike3.model.Model;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

public class ObjectListView extends ViewPart {
	
	TableViewer viewer = null;
	
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
		
		// add right-click menu
        final MenuManager mgr = new MenuManager();
		mgr.setRemoveAllWhenShown( true );
        mgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
				mgr.add( openAction );
				mgr.add( new OpenWithAction( viewer ) );
            }
        });
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
}
