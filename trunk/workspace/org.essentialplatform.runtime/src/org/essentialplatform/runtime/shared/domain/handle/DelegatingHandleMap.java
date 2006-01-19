package org.essentialplatform.runtime.shared.domain.handle;

import java.util.Iterator;
import java.util.Set;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public abstract class DelegatingHandleMap implements IHandleMap {

	
	public DelegatingHandleMap(final IHandleMap delegateHandleMap) {
		super();
		_delegateHandleMap = delegateHandleMap;
	}
	
	private final IHandleMap _delegateHandleMap;
	protected final IHandleMap getDelegateHandleMap() {
		return _delegateHandleMap;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getSessionBinding()
	 */
	public SessionBinding getSessionBinding() {
		return _delegateHandleMap.getSessionBinding();
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getDomainObject(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public IDomainObject getDomainObject(Handle handle) {
		return _delegateHandleMap.getDomainObject(handle);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getHandle(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public Handle getHandle(IDomainObject domainObject) {
		return _delegateHandleMap.getHandle(domainObject);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#add(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public boolean add(IDomainObject domainObject) throws IllegalStateException {
		return _delegateHandleMap.add(domainObject);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#remove(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public boolean remove(IDomainObject domainObject) throws IllegalStateException {
		return _delegateHandleMap.remove(domainObject);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#remove(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public boolean remove(Handle handle) throws IllegalStateException {
		return _delegateHandleMap.remove(handle);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getHandles()
	 */
	public Set<Handle> handles() {
		return _delegateHandleMap.handles();
	}

}
