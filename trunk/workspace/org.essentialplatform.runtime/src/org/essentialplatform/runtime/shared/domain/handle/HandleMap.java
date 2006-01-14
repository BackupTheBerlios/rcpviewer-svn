package org.essentialplatform.runtime.shared.domain.handle;

import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Simple implementation of {@link IHandleMap} that uses hashes.
 * 
 * <p>
 * TODO: need to use weak references? 
 * 
 * @author Dan Haywood
 */
public final class HandleMap implements IHandleMap {
	
	/**
	 * Mapping of pojo by its {@link Handle} (as managed by uts wrapping 
	 * {@link IDomainObject}).
	 * 
	 * <p>
	 * Reciprocal of {@link #_handleByPojo}
	 *  
	 * @see #_domainObjectByPojo
	 */
	private Map<Handle, IDomainObject> _domainObjectsByHandle = new HashMap<Handle, IDomainObject>();

	/**
	 * Mapping of pojo by its wrapping {@link IDomainObject}.
	 * 
	 * <p>
	 * Reciprocal of {@link #__pojoByHandle}
	 *  
	 * @see #_domainObjectByPojo
	 */
	private Map<IDomainObject, Handle> _handlesByDomainObject = new HashMap<IDomainObject, Handle>();

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IHandleMap#getDomainObject(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public IDomainObject getDomainObject(Handle handle) {
		// attempt to lookup domain object by handle
		IDomainObject domainObject = _domainObjectsByHandle.get(handle);
		if (domainObject != null) {
			return domainObject;
		}
		// not found.  If there is no previous, then give up
		if (!handle.hasPrevious()) {
			return null;
		}
		// the handle has previous, so is the domain object keyed by the previuos
		Handle previousHandle = handle.getPrevious();
		domainObject = _domainObjectsByHandle.get(previousHandle);
		if (domainObject == null) {
			return null;
		}
		// the domain object is keyed by previous.  Update the hashes
		// first, remove from both hashes
		_domainObjectsByHandle.remove(previousHandle);
		_handlesByDomainObject.remove(domainObject);
		// second, update the domain object's own handle
		domainObject.assignHandle(handle);
		// lastly, add back in with the new handle
		_domainObjectsByHandle.put(handle, domainObject);
		_handlesByDomainObject.put(domainObject, handle);
		return domainObject;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IHandleMap#getHandle(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public Handle getHandle(IDomainObject domainObject) {
		return _handlesByDomainObject.get(domainObject);
	}

	public synchronized boolean add(IDomainObject domainObject) throws IllegalStateException {
		Handle handleFromDomainObject = domainObject.getHandle();
		if (handleFromDomainObject == null) {
			throw new IllegalArgumentException("domain object has null handle");
		}
		Handle handleInMap = _handlesByDomainObject.get(domainObject);
		IDomainObject domainObjectUsingHandleFromDomainObject = _domainObjectsByHandle.get(handleFromDomainObject);
		if (handleInMap == null) {
			if (domainObjectUsingHandleFromDomainObject == null) {
				// not in the maps, so add
				_domainObjectsByHandle.put(handleFromDomainObject, domainObject);
				_handlesByDomainObject.put(domainObject, handleFromDomainObject);
				return true;
			} else {
				throw new IllegalStateException("Located (handle, domainObject but not (domainObject, handle); " +
						"domainObject = '" + domainObject + "', handle = '" + handleFromDomainObject + "'");
			}
		} else {
			if (domainObjectUsingHandleFromDomainObject == null) {
				throw new IllegalStateException("Located (domainObject, handle) but not (handle, domainObject); " +
						"domainObject = '" + domainObject + "', looked up handle = '" + handleInMap + "'");
			} else {
				// found both
				if (!handleInMap.equals(handleFromDomainObject)) {
					throw new IllegalStateException("Found both pairs, but handle in map different from domain object's; " + 
						"domainObject = '" + domainObject + "', looked up handle = '" + handleInMap + "', domainObject's handle = '" + handleFromDomainObject + "'");
				}
				if (domainObjectUsingHandleFromDomainObject != domainObject) {
					throw new IllegalStateException("Found both pairs, but reverse lookup of domainObject was different; " + 
						"domainObject = '" + domainObject + "', handle = '" + handleInMap + "', reverse domainObject = '" + domainObjectUsingHandleFromDomainObject + "'");
				}
				return false;
			}
		}
	}

	/**
	 * Doesn't delegate to {@link #remove(Handle)} so that the error messages are
	 * as clear as possible.
	 */
	public synchronized boolean remove(IDomainObject domainObject) throws IllegalStateException {
		Handle handleFromDomainObject = domainObject.getHandle();
		if (handleFromDomainObject == null) {
			throw new IllegalArgumentException("domain object has null handle");
		}
		Handle handleInMap = _handlesByDomainObject.get(domainObject);
		IDomainObject domainObjectUsingHandleFromDomainObject = _domainObjectsByHandle.get(handleFromDomainObject);
		if (handleInMap == null) {
			if (domainObjectUsingHandleFromDomainObject == null) {
				// wasn't in either map
				return false;
			} else {
				throw new IllegalStateException("Located (handle, domainObject) but not (domainObject, handle); " +
						"domainObject = '" + domainObject + "', handle = '" + handleFromDomainObject + "'");
			}
		} else {
			if (domainObjectUsingHandleFromDomainObject == null) {
				throw new IllegalStateException("Located (domainObject, handle) but not (handle, domainObject); " +
						"domainObject = '" + domainObject + "', looked up handle = '" + handleInMap + "'");
			} else {
				// found both
				if (!handleInMap.equals(handleFromDomainObject)) {
					throw new IllegalStateException("Found both pairs, but handle in map different from domain object's; " + 
						"domainObject = '" + domainObject + "', looked up handle = '" + handleInMap + "', domainObject's handle = '" + handleFromDomainObject + "'");
				}
				if (domainObjectUsingHandleFromDomainObject != domainObject) {
					throw new IllegalStateException("Found both pairs, but reverse lookup of domainObject was different; " + 
						"domainObject = '" + domainObject + "', handle = '" + handleInMap + "', reverse domainObject = '" + domainObjectUsingHandleFromDomainObject + "'");
				}
				// all ok:
				// handleInMap  == handleFromDomainObject   AND 
				// domainObject == domainObjectUsingHandleFromDomainObject
				// now remove from the two hashes.
				_handlesByDomainObject.remove(domainObject);
				_domainObjectsByHandle.remove(handleFromDomainObject);
				return true;
			}
		}
	}

	/**
	 * Doesn't delegate to {@link #remove(Handle)} so that the error messages are
	 * as clear as possible.
	 */
	public synchronized boolean remove(Handle handle) throws IllegalStateException {
		if (handle.hasPrevious()) {
			throw new IllegalArgumentException("Handle cannot have previous; handle='" + handle + "'"); 
		}
		
		IDomainObject lookedUpDomainObject = _domainObjectsByHandle.get(handle);
		Handle handleUsingLookedUpDomainObject = _handlesByDomainObject.get(lookedUpDomainObject);
		if (lookedUpDomainObject == null) {
			if (handleUsingLookedUpDomainObject == null) {
				// wasn't in either map
				return false;
			} else {
				throw new IllegalStateException("Located (domainObject, handle) but not (handle, domainObject); " +
						"handle = '" + handle + "', domainObject = '" + lookedUpDomainObject + "'");
			}
		} else {
			if (handleUsingLookedUpDomainObject == null) {
				throw new IllegalStateException("Located (handle, domainObject) but not (domainObject, handle); " +
						"handle = '" + handle + "', looked up domainObject = '" + lookedUpDomainObject + "'");
			} else {
				// found both
				if (!handleUsingLookedUpDomainObject.equals(handle)) {
					throw new IllegalStateException("Found both pairs, but reverse lookup handle different from orginal; " + 
							"handle = '" + handle + "', looked up domainObject = '" + lookedUpDomainObject + "', reverse handle = '" + handleUsingLookedUpDomainObject + "'");
				}
				// all ok:
				// handle == handleUsingLookedUpDomainObject 
				// now remove from the two hashes.
				_domainObjectsByHandle.remove(handle);
				_handlesByDomainObject.remove(lookedUpDomainObject);
				return true;
			}
		}
	}

	
}
