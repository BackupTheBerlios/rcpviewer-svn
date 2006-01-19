package org.essentialplatform.runtime.shared.domain.handle;

import java.util.LinkedHashMap;
import java.util.Map;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;

/**
 * Implements a {@link IHandleMap} that is never empty.
 * 
 * <p>
 * If a request is made to get an object from the map that does not exist
 * (ie either {@link #getDomainObject(Handle)} or {@link #getHandle(IDomainObject)})
 * then it will automatically populate the Map, creating the domain object if
 * required.
 * 
 * <p>
 * Any objects that have been added are available using {@link #getAdditions()}.
 * These objects will have a resolve state of {@link ResolveState#UNRESOLVED}.
 * 
 * <p>
 * Note that requests to remove objects from the map (ie via either
 * {@link IHandleMap#remove(Handle)} or {@link IHandleMap#remove(IDomainObject)})
 * are <i>not</i> tracked.
 *   
 * 
 * @author Dan Haywood
 */
public final class AutoAddingHandleMap extends DelegatingHandleMap {

	public AutoAddingHandleMap(IHandleMap delegateHandleMap, IDomainObjectFactory domainObjectFactory) {
		super(delegateHandleMap);
		_domainObjectFactory = domainObjectFactory;
		_deltaHandleMap = new HandleMap(getDelegateHandleMap().getSessionBinding());
	}

	private final IHandleMap _deltaHandleMap;
	/**
	 * All (handle, IDomainObject) tuples that have been added to the map since
	 * it was instantiated.
	 *   
	 * @return
	 */
	public IHandleMap getAdditions() {
		return _deltaHandleMap;
	}
	
	private IDomainObjectFactory _domainObjectFactory;


	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getDomainObject(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public IDomainObject getDomainObject(Handle handle) {
		IDomainObject domainObject = super.getDomainObject(handle);
		if (domainObject == null) {
			domainObject = _domainObjectFactory.createDomainObject(handle);
			add(domainObject);
		}
		return domainObject;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getHandle(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public Handle getHandle(IDomainObject domainObject) {
		Handle handle = super.getHandle(domainObject);
		if (handle == null) {
			add(domainObject); // adds to both super and to our delta
			handle = domainObject.getHandle();
		}
		return handle;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#add(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public boolean add(IDomainObject domainObject) throws IllegalStateException {
		boolean whetherAdded = super.add(domainObject);
		if (whetherAdded) {
			_deltaHandleMap.add(domainObject);
		}
		return whetherAdded;
	}
}

