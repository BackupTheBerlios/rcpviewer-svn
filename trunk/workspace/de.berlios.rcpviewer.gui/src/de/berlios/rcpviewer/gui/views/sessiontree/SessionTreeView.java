package de.berlios.rcpviewer.gui.views.sessiontree;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.jobs.JobAction;
import de.berlios.rcpviewer.gui.jobs.NewDomainObjectJob;
import de.berlios.rcpviewer.gui.jobs.OpenDomainObjectJob;
import de.berlios.rcpviewer.gui.jobs.SearchJob;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISessionManager;

/**
 * Displays all objects attached to the session started by
 * <code>GuiPlugin start()</code>. <br>
 * Eventually there will be an instance of this view for each session opened.
 * 
 * @author Mike
 */
public class SessionTreeView extends ViewPart {

	public static final String ID = SessionTreeView.class.getName();

	private TreeViewer _viewer = null;

	private SessionListener _sessionListener = null;

	private SessionTreePartListener _partListener = null;

	/**
	 * Creates viewer and link it to current session via listeners.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		if (parent == null)
			throw new IllegalArgumentException();
		parent.setLayout(new FillLayout());

		// viewer
		_viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		_viewer.setLabelProvider(new SessionTreeLabelProvider());
		_viewer.setContentProvider(new SessionTreeContentProvider());

		// tie viewer to current session
		try {
			ISessionManager mgr = RuntimePlugin.getDefault().getSessionManager();
			String sessionId = mgr.getCurrentSessionId();
			_sessionListener = new SessionListener(mgr, sessionId, _viewer);
			_viewer.setInput(mgr.get(sessionId));
		} catch (CoreException ce) {
			GuiPlugin.getDefault().getLog().log(ce.getStatus());
			_viewer.setInput(new ErrorInput());
		}

		// tie viewer to active editors
		_partListener = new SessionTreePartListener(_viewer);

		// add popup menu
		MenuManager mgr = new MenuManager();
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				populateContextMenu(manager);
			}
		});
		Menu menu = mgr.createContextMenu(_viewer.getControl());
		_viewer.getControl().setMenu(menu);
		
		// add open listener
		_viewer.addOpenListener( new IOpenListener(){
		    public void open(OpenEvent event) {
				// can only be one item
				Object selected 
					= ((StructuredSelection)event.getSelection()).getFirstElement();
				if ( selected instanceof IRuntimeDomainClass ) {
					new NewDomainObjectJob( (IRuntimeDomainClass)selected ).schedule();
				}
				else if ( selected instanceof IDomainObject ) {
					new OpenDomainObjectJob( (IDomainObject)selected ).schedule();
				}
		    }
			
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (_viewer != null) {
			_viewer.getTree().setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		if (_sessionListener != null ) {
			_sessionListener.dispose();
			_sessionListener = null;
		}
		if (_partListener != null) {
			_partListener.dispose();
			_partListener = null;
		}
		super.dispose();
	}

	/* non-platform public methods */
	
	
	/**
	 * Refrehses the display of the passed object (if  displayed).
	 * @param object
	 */
	public void refresh( IDomainObject object ) {
		if ( object == null ) throw new IllegalArgumentException();
		_viewer.refresh( object );
	}

	/* private methods */

	// as it says - dependent on current viewer selection
	private void populateContextMenu( IMenuManager mgr ) {
		assert mgr != null;
		StructuredSelection selection
			= (StructuredSelection)_viewer.getSelection();
		if ( selection.isEmpty() ) return;
		// single-selection mode
		Object selected = selection.getFirstElement();
		if ( selected instanceof IRuntimeDomainClass ) {
			IRuntimeDomainClass clazz = (IRuntimeDomainClass)selected;
			mgr.add( new JobAction( new NewDomainObjectJob( clazz ) ) );
			mgr.add( new JobAction( new SearchJob( clazz ) ) );
		}
		else if ( selected instanceof IDomainObject ) {
			IDomainObject object = (IDomainObject)selected;
			mgr.add( new JobAction( new OpenDomainObjectJob( object  ) ) );
		}
	}}
