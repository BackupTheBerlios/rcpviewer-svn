package org.essentialplatform.runtime.server.persistence.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.server.domain.bindings.RuntimeServerBinding;
import org.essentialplatform.runtime.server.persistence.AbstractObjectStore;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;
import org.essentialplatform.runtime.shared.persistence.PersistenceId;

/**
 * 
 * TODO: this design doesn't deal with polymorphism.
 * 
 * @author Dan Haywood
 */
public final class InMemoryObjectStore extends AbstractObjectStore {
	
	public InMemoryObjectStore(String id) {
		super(id);
	}

	private Map<Class<?>, Map<PersistenceId, Object>> _pojoByPersistenceIdByType = 
		new HashMap<Class<?>, Map<PersistenceId, Object>>();
	
	private <V> Map<PersistenceId, Object> pojoByPersistenceIdFor(Class<V> javaClass) {
		Map<PersistenceId, Object> pojoByPersistenceId = (Map<PersistenceId, Object>)_pojoByPersistenceIdByType.get(javaClass);
		if (pojoByPersistenceId == null) {
			pojoByPersistenceId = new HashMap<PersistenceId, Object>();
			_pojoByPersistenceIdByType.put(javaClass, pojoByPersistenceId);
		}
		return pojoByPersistenceId;
	}		

	/*
	 * @see org.essentialplatform.persistence.IObjectStore#save(org.essentialplatform.session.IDomainObject)
	 */
	public synchronized <T> void save(IDomainObject<T> domainObject) throws DuplicateObjectException {
		IDomainClass domainClass = domainObject.getDomainClass();

		// get the runtime binding to assign an appropriate persistence Id.
		RuntimeServerBinding.RuntimeServerClassBinding<T> binding = 
			(RuntimeServerBinding.RuntimeServerClassBinding)domainClass.getBinding();
		binding.assignPersistenceIdFor(domainObject);
		Object pojo = domainObject.getPojo();
		PersistenceId persistenceId = domainObject.getPersistenceId();

		// get the hash for this class and make sure this pojo doesn't already exist
		Map<PersistenceId, Object> pojoByPersistenceId = 
			pojoByPersistenceIdFor(pojo.getClass());
		if (pojoByPersistenceId.get(persistenceId) != null) {
			throw new DuplicateObjectException("Pojo '" + persistenceId + "' already exists", pojo);
		}
		
		// put the pojo into the hash.
		pojoByPersistenceId.put(persistenceId, pojo);
	}

	/*
	 * @see org.essentialplatform.persistence.IObjectStore#update(org.essentialplatform.session.IDomainObject)
	 */
	public synchronized <T> void update(IDomainObject<T> domainObject) throws ConcurrencyException, DuplicateObjectException {
		Object pojo = domainObject.getPojo();

		// get the hash for this class
		Map<PersistenceId, Object> pojoByPersistenceId = 
			pojoByPersistenceIdFor(domainObject.getPojo().getClass());
		
		// look up the stored pojo, to make sure its there
		Object storedPojo = pojoForPersistenceId(domainObject, pojoByPersistenceId);
		
		// nothing else to do, though (relying on identity comparison).
	}


	public synchronized void delete(IDomainObject<?> domainObject) throws ConcurrencyException {
		Object pojo = domainObject.getPojo();

		// get the hash for this class
		Map<PersistenceId, Object> pojoByPersistenceId = 
			pojoByPersistenceIdFor(domainObject.getPojo().getClass());
		
		// look up the stored pojo, just to make sure its there.
		Object storedPojo = pojoForPersistenceId(domainObject, pojoByPersistenceId);

		// delete the pojo from the hash
		_pojoByPersistenceIdByType.remove(domainObject.getPersistenceId());
	}

	
	/*
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#saveOrUpdate(org.essentialplatform.session.IDomainObject)
	 */
	public <T> void saveOrUpdate(IDomainObject<T> pojo) {
		if (!isPersistent(pojo)) {
			save(pojo);
		} else {
			update(pojo);
		}
	}


	/*
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#isPersistent(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public <T> boolean isPersistent(IDomainObject<T> domainObject) {
		return false;
	}


	/**
	 * INCLUDES A CHECK WHICH RELIES UPON IDENTITY COMPARISON...
	 * ... SO IS INCOMPATIBLE WITH THE REMOTING DESIGN.
	 */
	private <T> Object pojoForPersistenceId( 
			IDomainObject<T> domainObject, Map<PersistenceId, Object> pojoByPersistenceId) throws ConcurrencyException {
		PersistenceId persistenceId = domainObject.getPersistenceId();
		Object pojo = pojoByPersistenceId.get(persistenceId);
		if (pojo == null) {
			throw new ConcurrencyException("Pojo does not exist (deleted?)", null);
		}
		if (pojo != domainObject.getPojo()) {
			throw new IllegalArgumentException("Pojo in the map is different");
		}
		return pojo;
	}
	

	/**
	 * For testing purposes.
	 * 
	 * <p>
	 * Not part of the {@link IObjectStore} interface.
	 */
	public void reset() {
		_pojoByPersistenceIdByType.clear();
	}
	
	
	/**
	 * @return All pojos of the specified type persisted in this store.
	 */
	public <V> Collection<V> allInstances(final Class<V> pojoClass) {
		List<V> allInstances= new ArrayList<V>();
		appendInstances(pojoClass, allInstances);
		return allInstances;
	}

	/**
	 * @return All pojos of any type persisted in this store.
	 */
	public Collection<?> allInstances() {
		List<Object> allInstances = new ArrayList<Object>();
		for(Class type: _pojoByPersistenceIdByType.keySet()) {
			appendInstances(type, allInstances);
		}
		return allInstances;
	}

	/**
	 * JAVA_5_FIXME
	 */
	private <V> Collection<V> appendInstances(final Class<V> pojoClass, Collection<V> instances) {
		Map<PersistenceId, Object> pojoByPersistenceId = pojoByPersistenceIdFor(pojoClass);
		if (pojoByPersistenceId != null) {
			instances.addAll((Collection<V>)pojoByPersistenceId.values());
		}
		return instances;
	}


}
