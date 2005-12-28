/**
 * 
 */
package org.essentialplatform.louis.views.sessiontree;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PartInitException;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.PlatformUtil;

import org.essentialplatform.runtime.shared.session.ISession;
import org.essentialplatform.runtime.shared.session.ISessionManager;
import org.essentialplatform.runtime.shared.session.event.ISessionListener;
import org.essentialplatform.runtime.shared.session.event.ISessionManagerListener;
import org.essentialplatform.runtime.shared.session.event.SessionManagerEvent;
import org.essentialplatform.runtime.shared.session.event.SessionObjectEvent;
import org.essentialplatform.runtime.shared.RuntimePlugin;

/**
 * Implements both <code>ISessionListener</code> and 
 * <code>ISessionManagerListener</code>.  The former for adding/removing
 * items from the parent view as they are added/removed from the
 * parent session.  The latter if for cleanly de-referencing when either 
 * parent session or view is closed. 
 * @author Mike
 */
class SessionListener implements ISessionListener, ISessionManagerListener {
	
	private final ISessionManager _sessionManager;
	private final ISession _session;
	private final TreeViewer _viewer;
	
	/**
	 * Constructor passed the parent session id and viewer.
	 * <br>Adds itself as a listener to the passed session manager and the
	 * session idenetified by the passed id.
	 * @param session
	 */
	SessionListener( ISessionManager sessionManager, ISession session, TreeViewer viewer ) {
		assert sessionManager != null;
		assert session != null;
		assert viewer != null;
		_sessionManager = sessionManager;
		_session = session;
		_viewer = viewer;
		_sessionManager.addSessionManagerListener( this );
		_session.addSessionListener( this );
	}
	
	/* ISessionListener contract */

	/* (non-Javadoc)
	 * @see org.essentialplatform.session.ISessionListener#domainObjectAttached(org.essentialplatform.session.SessionObjectEvent)
	 */
	public void domainObjectAttached(SessionObjectEvent event) {
		if ( event == null ) throw new IllegalArgumentException();
		if ( _session == event.getSession() ) {
			_viewer.refresh();
		}
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.session.ISessionListener#domainObjectDetached(org.essentialplatform.session.SessionObjectEvent)
	 */
	public void domainObjectDetached(SessionObjectEvent event) {
		if ( event == null ) throw new IllegalArgumentException();
		if ( _session == event.getSession() ) {
			_viewer.refresh();
		}
	}
	
	/* ISessionManagerListener contract */
	
	/**
	 * Null method
	 * @see org.essentialplatform.runtime.shared.session.event.ISessionManagerListener#sessionCreated(org.essentialplatform.runtime.shared.session.event.SessionManagerEvent)
	 */
	public void sessionCreated(SessionManagerEvent event) {
		// null method
	}
	
	/**
	 * Brings this view to the fore
	 * @see org.essentialplatform.runtime.shared.session.event.ISessionManagerListener#sessionNowCurrent(org.essentialplatform.runtime.shared.session.event.SessionManagerEvent)
	 */
	public void sessionNowCurrent(SessionManagerEvent event) {
		try {
			PlatformUtil.getActivePage().showView( SessionTreeView.ID );
		}
		catch ( PartInitException pie ) {
			// should never occur as this view has already been succesfully
			// instantiated
			LouisPlugin.getDefault().getLog().log( pie.getStatus() );
		}
		
	}

	/**
	 * Dispose this and the session listener of the removed session is the
	 * parent session.
	 * @see org.essentialplatform.runtime.shared.session.event.ISessionManagerListener#sessionRemoved(org.essentialplatform.runtime.shared.session.event.SessionManagerEvent)
	 */
	public void sessionRemoved(SessionManagerEvent event) {
		if ( event == null ) throw new IllegalArgumentException();
		if ( _session == event.getSession() ) {
			dispose();
		}
	}
	
	/**
	 * Dereferences everything
	 */
	void dispose() {
		_sessionManager.removeSessionManagerListener(this);
		_session.removeSessionListener(this);
	}
	

	/**
	 * Accessor to parent session's id
	 * @return
	 */
	String getSessionId() {
		return _session.getId() ;
	}

}