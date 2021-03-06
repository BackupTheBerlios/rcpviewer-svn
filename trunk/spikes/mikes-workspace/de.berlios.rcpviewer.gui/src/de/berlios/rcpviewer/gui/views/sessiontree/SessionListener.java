/**
 * 
 */
package de.berlios.rcpviewer.gui.views.sessiontree;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PartInitException;

import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.util.PlatformUtil;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionListener;
import de.berlios.rcpviewer.session.ISessionManager;
import de.berlios.rcpviewer.session.ISessionManagerListener;
import de.berlios.rcpviewer.session.SessionManagerEvent;
import de.berlios.rcpviewer.session.SessionObjectEvent;

/**
 * Implements both <code>ISessionListener</code> and 
 * <code>ISessionManagerListener</code>.  The former for adding/removing
 * items from the parent view as they are added/removed from the
 * parent session.  The latter if for cleanly de-referencing when either 
 * parent session or view is closed. 
 * @author Mike
 */
class SessionListener implements ISessionListener, ISessionManagerListener {
	
	private final String _sessionId;
	private final TreeViewer _viewer;
	
	/**
	 * Constructor passed the parent session id and viewer.
	 * <br>Adds itself as a listener to the passed session manager and the
	 * session idenetified by the passed id.
	 * @param session
	 */
	SessionListener( ISessionManager mgr, String sessionId, TreeViewer viewer ) {
		assert sessionId != null;
		assert viewer != null;
		_sessionId= sessionId;
		_viewer = viewer;
		mgr.addSessionManagerListener( this );
		mgr.get( sessionId ).addSessionListener( this );
	}
	
	/* ISessionListener contract */

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.session.ISessionListener#domainObjectAttached(de.berlios.rcpviewer.session.SessionObjectEvent)
	 */
	public void domainObjectAttached(SessionObjectEvent event) {
		if ( event == null ) throw new IllegalArgumentException();
		if ( _sessionId.equals( event.getSession().getId() ) ) {
			_viewer.refresh();
		}
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.session.ISessionListener#domainObjectDetached(de.berlios.rcpviewer.session.SessionObjectEvent)
	 */
	public void domainObjectDetached(SessionObjectEvent event) {
		if ( event == null ) throw new IllegalArgumentException();
		if ( _sessionId.equals( event.getSession().getId() ) ) {
			_viewer.refresh();
		}
	}
	
	/* ISessionManagerListener contract */
	
	/**
	 * Null method
	 * @see de.berlios.rcpviewer.session.ISessionManagerListener#sessionCreated(de.berlios.rcpviewer.session.SessionManagerEvent)
	 */
	public void sessionCreated(SessionManagerEvent event) {
		// null method
	}
	
	/**
	 * Brings this view to the fore
	 * @see de.berlios.rcpviewer.session.ISessionManagerListener#sessionNowCurrent(de.berlios.rcpviewer.session.SessionManagerEvent)
	 */
	public void sessionNowCurrent(SessionManagerEvent event) {
		try {
			PlatformUtil.getActivePage().showView( SessionTreeView.ID );
		}
		catch ( PartInitException pie ) {
			// should never occur as this view has already been succesfully
			// instantiated
			GuiPlugin.getDefault().getLog().log( pie.getStatus() );
		}
		
	}

	/**
	 * Dispose this and the session listener of the removed session is the
	 * parent session.
	 * @see de.berlios.rcpviewer.session.ISessionManagerListener#sessionRemoved(de.berlios.rcpviewer.session.SessionManagerEvent)
	 */
	public void sessionRemoved(SessionManagerEvent event) {
		if ( event == null ) throw new IllegalArgumentException();
		if ( _sessionId.equals( event.getSession().getId() ) ) {
			dispose();
		}
	}
	
	/**
	 * Dereferences everything
	 */
	void dispose() {
		try {
			ISessionManager mgr = RuntimePlugin.getDefault().getSessionManager();
			ISession session =  mgr.get( _sessionId );
			mgr.removeSessionManagerListener( this );
			session.removeSessionListener( this );
		}
		catch ( CoreException ce ) {
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
		}
	}
	

	/**
	 * Accessor to parent session's id
	 * @return
	 */
	String getSessionId() {
		return _sessionId ;
	}

}