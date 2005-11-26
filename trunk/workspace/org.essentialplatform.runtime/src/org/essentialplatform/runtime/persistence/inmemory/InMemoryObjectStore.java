package org.essentialplatform.runtime.persistence.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.RuntimeDeployment;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.persistence.ConcurrencyException;
import org.essentialplatform.runtime.persistence.DuplicateObjectException;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.persistence.PersistenceId;

/**
 * 
 * TODO: this design doesn't deal with polymorphism.
 * 
 * @author Dan Haywood
 */
public final class InMemoryObjectStore implements IObjectStore {
	
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
	public <T> void save(IDomainObject<T> domainObject) throws DuplicateObjectException {
		IDomainClass domainClass = domainObject.getDomainClass();

		// get the runtime binding to assign an appropriate persistence Id.
		RuntimeDeployment.RuntimeClassBinding<T> binding = 
			(RuntimeDeployment.RuntimeClassBinding)domainClass.getBinding();
		binding.assignPersistenceIdFor(domainObject);
		
		persist(domainObject.getPersistenceId(), domainObject.getPojo());
	}

	/*
	 * @see org.essentialplatform.persistence.IObjectStore#update(org.essentialplatform.session.IDomainObject)
	 */
	public <T> void update(IDomainObject<T> domainObject) throws ConcurrencyException, DuplicateObjectException {
		T pojo = (T)pojoByPersistenceIdFor(domainObject.getPojo().getClass());
		// make sure that the pojo that this domain Object is referencing is
		// the one in our hash.
		if (pojo != domainObject.getPojo()) {
			throw new IllegalArgumentException("Pojo is incorrect");
		}
		// nothing else to do, though.
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
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#isPersistent(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	public <T> boolean isPersistent(IDomainObject<T> domainObject) {
		return false;
	}

	/**
	 * 
	 * @param title
	 * @param pojo
	 */
	private void persist(PersistenceId persistenceId, Object pojo) {
		pojoByPersistenceIdFor(pojo.getClass()).put(persistenceId, pojo);
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
