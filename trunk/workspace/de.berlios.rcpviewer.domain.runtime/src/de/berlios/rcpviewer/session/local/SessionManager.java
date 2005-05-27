package de.berlios.rcpviewer.session.local;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;

public final class SessionManager implements ISessionManager {

	private static SessionManager instance = new SessionManager();
	
	/**
	 * Singleton access.
	 * 
	 * @return
	 */
	public static SessionManager instance() {
		return instance;
	}
	
	/**
	 * Returns a GUID guaranteed to be unique across all VMs.
	 * 
	 * <p>
	 * Uses {@link java.rmi.dgc.VMID}.
	 */
	public String nextId() {
		return new VMID().toString();
	}

	private Map<String, ISession> sessionById = new HashMap<String, ISession>();
	public ISession get(String id) {
		return sessionById.get(id);
	}


	/**
	 * Primarily for testing purposes; resets all sessions (using
	 * {@link ISession#reset()}) and then clears hash of sessions held by the
	 * manager itself.
	 * 
	 */
	public void reset() {
		for(ISession session: sessionById.values()) {
			session.reset();
		}
		this.sessionById.clear();
	}
	

}
