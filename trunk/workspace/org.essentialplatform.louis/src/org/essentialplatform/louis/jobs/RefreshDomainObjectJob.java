package org.essentialplatform.louis.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IViewPart;
import org.essentialplatform.louis.editors.DefaultEditor;
import org.essentialplatform.louis.util.PlatformUtil;
import org.essentialplatform.louis.views.sessiontree.SessionTreeView;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Dunno what this might do eventually ... 
 * <br>Currently very un-encapsulated and ugly - but should provoke thought:
 * <ul>
 * <li>looks for a DefaultEditor displaying the object and if found refreshes it
 * <li>request the session tree to refresh for the object
 * </ul>
 * @author Mike
 *
 */
public class RefreshDomainObjectJob extends AbstractDomainObjectJob {
	
	/**
	 * Constructor requires operation
	 * @param object
	 */
	public RefreshDomainObjectJob( IDomainObject object ) {
		super( RefreshDomainObjectJob.class.getName(), object );
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		
		// editor
		DefaultEditor editor = PlatformUtil.getActiveEditor( getDomainObject() );
		if ( editor != null ) {
			editor.refresh();
		}
		
		// session tree
		IViewPart view = PlatformUtil.getActivePage().findView( SessionTreeView.ID );
		if ( view != null ) {
			((SessionTreeView)view).refresh( getDomainObject() );
		}
		
		
		return Status.OK_STATUS;
	}

}
