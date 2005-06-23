package de.berlios.rcpviewer.persistence.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.session.IDomainObject;

public final class InMemoryObjectStore implements IObjectStore {
	
	private Map<Class<?>, Map<String, Object>> pojoByTitleByType = 
		new HashMap<Class<?>, Map<String, Object>>();
	
	private <V> Map<String, Object> pojoByTitleFor(Class<V> javaClass) {
		Map<String, Object> pojoByTitle = (Map<String, Object>)pojoByTitleByType.get(javaClass);
		if (pojoByTitle == null) {
			pojoByTitle = new HashMap<String, Object>();
			pojoByTitleByType.put(javaClass, pojoByTitle);
		}
		return pojoByTitle;
	}		

	public <T> void persist(IDomainObject<T> domainObject) {
		persist(domainObject.title(), domainObject.getPojo());
	}


	public <T> void save(IDomainObject<T> domainObject) {
		Object pojo = pojoByTitleFor(domainObject.getPojo().getClass());
		// make sure that the pojo that this domain Object is referencing is
		// the one in our hash.
		if (pojo != domainObject.getPojo()) {
			throw new IllegalArgumentException("Pojo is incorrect");
		}
		// nothing else to do, though.
	}
	
	/**
	 * TODO: this design doesn't deal with polymorphism.
	 * 
	 * @param title
	 * @param pojo
	 */
	private void persist(String title, Object pojo) {
		pojoByTitleFor(pojo.getClass()).put(title, pojo);
	}
	public <V> V findByTitle(Class<V> javaClass, String title) {
		return (V)pojoByTitleFor(javaClass).get(title);
	}

	public void reset() {
		pojoByTitleByType.clear();
	}
	
	
	/**
	 * @return All objects saved in this store.
	 */
	public Collection<Object> getAllStoredObjects() {
		ArrayList<Object> all= new ArrayList<Object>(); 
		for (Map<String, Object> pojoByTitle: pojoByTitleByType.values()) {
			all.addAll(pojoByTitle.values());
		}
		return all;
	}



}
