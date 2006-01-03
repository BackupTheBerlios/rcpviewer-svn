package org.essentialplatform.runtime.shared.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.server.persistence.IObjectStore;

/**
 * Maintains a set of handles, and the current handle, all for a given domain.
 * 
 * @author Dan Haywood
 */
public final class ObjectStoreHandleList<V extends IObjectStoreHandle> implements Iterable<V> {
	
	private Map<String,V> _handleByObjectStoreId = new HashMap<String,V>();
	
	/////////////////////////////////////////////////////////////////////
	// add, remove
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a session and makes it the default.
	 * @param handle
	 */
	public void add(V handle) {
		_handleByObjectStoreId.put(handle.getObjectStoreId(), handle);
		_current = handle;
	}
	
	/**
	 * Removes the session; if it was the current session then that is set to 
	 * <tt>null</tt>.
	 * 
	 * @param handle
	 * @return <tt> iff the session was contained in the list.
	 */
	public boolean remove(V handle) {
		if (_current == handle) {
			_current = null;
		}
		return _handleByObjectStoreId.remove(handle.getObjectStoreId()) != null;
	}

	/**
	 * Looks up the handle by objectStoreId and removes, returning the
	 * handle (or <tt>null</tt> if could not be located).
	 * 
	 * @param objectStoreId
	 * @return
	 */
	public V remove(String objectStoreId) {
		V handle = get(objectStoreId);
		if (handle != null) {
			remove(handle);
		}
		return handle;
	}

	/////////////////////////////////////////////////////////////////////
	// iterator, get, getAll
	/////////////////////////////////////////////////////////////////////

	public Iterator<V> iterator() {
		return _handleByObjectStoreId.values().iterator();
	}


	public V get(String objectStoreId) {
		return _handleByObjectStoreId.get(objectStoreId);
	}

	public Collection<V> getAll() {
		return _handleByObjectStoreId.values();
	}

	
	/////////////////////////////////////////////////////////////////////
	// current
	/////////////////////////////////////////////////////////////////////
		
	private V _current = null;
	public V getCurrent() {
		return _current;
	}
	public V setCurrent(String objectStoreId) {
		V session = _handleByObjectStoreId.get(objectStoreId); 
		if (session == null) {
			throw new IllegalStateException("Could not locate session '" + session + "'");
		}
		_current = session;
		return session;
	}

	
	/////////////////////////////////////////////////////////////////////
	// reset
	/////////////////////////////////////////////////////////////////////
		
	/**
	 * Primarily for testing purposes; resets all sessions (using
	 * {@link IClientSession#reset()}) and then clears hash of sessions held by the
	 * list itself.
	 * 
	 */
	public void reset() {
		for(V session: _handleByObjectStoreId.values()) {
			session.reset();
		}
		_handleByObjectStoreId.clear();
	}


}
