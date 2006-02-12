package org.essentialplatform.runtime.shared.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Maintains a set of {@link IObjectStoreRef}s, and the current ref, all for a 
 * given domain.
 * 
 * <p> 
 * Although this class is used both client- and server-side, note that the 
 * "current" ref functionality is only needed for the client-side.
 * 
 * @author Dan Haywood
 */
public final class ObjectStoreRefList<V extends IObjectStoreRef> implements Iterable<V> {
	
	private Map<String,V> _refsByObjectStoreId = new HashMap<String,V>();
	
	/////////////////////////////////////////////////////////////////////
	// add, remove
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a ref and makes it the default.
	 * 
	 * @param ref
	 */
	public void add(V ref) {
		_refsByObjectStoreId.put(ref.getObjectStoreId(), ref);
		_current = ref;
	}
	
	/**
	 * Removes the ref; if it was the current ref then that is set to 
	 * <tt>null</tt>.
	 * 
	 * @param ref
	 * @return <tt> iff the session was contained in the list.
	 */
	public boolean remove(V ref) {
		if (_current == ref) {
			_current = null;
		}
		return _refsByObjectStoreId.remove(ref.getObjectStoreId()) != null;
	}

	/**
	 * Looks up the ref by objectStoreId and removes, returning the
	 * handle (or <tt>null</tt> if could not be located).
	 * 
	 * @param objectStoreId
	 * @return
	 */
	public V remove(String objectStoreId) {
		V ref = get(objectStoreId);
		if (ref != null) {
			remove(ref);
		}
		return ref;
	}

	/////////////////////////////////////////////////////////////////////
	// iterator, get, getAll
	/////////////////////////////////////////////////////////////////////

	public Iterator<V> iterator() {
		return _refsByObjectStoreId.values().iterator();
	}


	public V get(String objectStoreId) {
		return _refsByObjectStoreId.get(objectStoreId);
	}

	public Collection<V> getAll() {
		return _refsByObjectStoreId.values();
	}

	
	/////////////////////////////////////////////////////////////////////
	// current
	/////////////////////////////////////////////////////////////////////
		
	private V _current = null;
	public V getCurrent() {
		return _current;
	}
	public V setCurrent(String objectStoreId) {
		V session = _refsByObjectStoreId.get(objectStoreId); 
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
	 * Primarily for testing purposes; resets all sessions and then clears 
	 * hash of sessions held by the list itself.
	 * 
	 */
	public void reset() {
		for(V session: _refsByObjectStoreId.values()) {
			session.reset();
		}
		_refsByObjectStoreId.clear();
	}


}
