package de.berlios.rcpviewer.session.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.IWrapperAware;

public class Session implements ISession, IWrapperAware, IObjectStoreAware {

	// TODO: make into an aspect
	private static ThreadLocal<Session> sessionForThisThread = 
			new ThreadLocal<Session>() {
		protected synchronized Session initialValue() {
			return new Session();
		}
	};
	public static ISession instance() {
		return sessionForThisThread.get();
	}

	private Map<IDomainClass<?>, List<IDomainObject<?>>> 
		domainObjectsByDomainClass = new HashMap<IDomainClass<?>, List<IDomainObject<?>>>();

	private List<IDomainObject<?>> getDomainObjectsFor(IDomainClass<?> domainClass) {
		List<IDomainObject<?>> domainObjects = 
			domainObjectsByDomainClass.get(domainClass);
		if (domainObjects == null) {
			domainObjects = new ArrayList<IDomainObject<?>>();
			domainObjectsByDomainClass.put(domainClass, domainObjects);
		}
		return domainObjects;
	}
	private List<IDomainObject<?>> getDomainObjectsFor(IDomainObject<?> domainObject) {
		return getDomainObjectsFor(domainObject.getDomainClass());
	}

	public IDomainObject<?> createTransient(IDomainClass<?> domainClass) {
		IDomainObject domainObject = domainClass.createTransient();
		attach(domainObject);
		return domainObject;
	}

	public boolean isAttached(Object pojo) {
		IDomainObject<?> domainObject = getWrapper().wrapped(pojo);
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		return domainObjects.contains(domainObject);
	}
	
	public boolean isAttached(IDomainObject<?> domainObject) {
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		return domainObjects.contains(domainObject);
	}
	
	public void detach(IDomainObject<?> domainObject) {
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		if (!domainObjects.contains(domainObject)) {
			throw new IllegalArgumentException("pojo not attached to session.");
		}
		domainObjects.remove(domainObject);
	}
	public void detach(Object pojo) {
		IDomainObject<?> domainObject = getWrapper().wrapped(pojo);
		detach(domainObject);
	}

	public void attach(IDomainObject<?> domainObject) {
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		if (domainObjects.contains(domainObject)) {
			throw new IllegalArgumentException("pojo already attached to session.");
		}
		domainObjects.add(domainObject);
	}
	public void attach(Department pojo) {
		IDomainObject<?> domainObject = getWrapper().wrapped(pojo);
		attach(domainObject);
	}

	public List<IDomainObject<?>> footprintFor(IDomainClass<?> domainClass) {
		return Collections.unmodifiableList(getDomainObjectsFor(domainClass));
	}

	public void persist(IDomainObject<?> domainObject) {
		if (!isAttached(domainObject)) {
			throw new IllegalArgumentException("pojo not attached to session");
		}
		getObjectStore().persist(domainObject.title(), domainObject.getPojo());
	}

	public void reset() {
		domainObjectsByDomainClass.clear();
	}

	// DEPENDENCY INJECTION START //

	private IWrapper wrapper;
	public IWrapper getWrapper() {
		return wrapper;
	}
	public void setWrapper(IWrapper wrapper) {
		this.wrapper = wrapper;
	}
	
	private IObjectStore objectStore;
	public IObjectStore getObjectStore() {
		return objectStore;
	}
	public void setObjectStore(IObjectStore objectStore) {
		this.objectStore = objectStore;
	}
	
	// DEPENDENCY INJECTION END //


}
