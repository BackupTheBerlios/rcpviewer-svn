package de.berlios.rcpviewer.persistence.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import de.berlios.rcpviewer.persistence.IObjectStore;

public final class InMemoryObjectStore implements IObjectStore {
	
	private Map<Class<?>, Map<String, Object>> pojoByTitleByType = 
		new HashMap<Class<?>, Map<String, Object>>();
	
	private Map<String, Object> pojoByTitleFor(Class javaClass) {
		Map<String, Object> pojoByTitle = pojoByTitleByType.get(javaClass);
		if (pojoByTitle == null) {
			pojoByTitle = new HashMap<String, Object>();
			pojoByTitleByType.put(javaClass, pojoByTitle);
		}
		return pojoByTitle;
	}		


	/**
	 * TODO: this design doesn't deal with polymorphism.
	 * 
	 * @param title
	 * @param pojo
	 */
	public void persist(String title, Object pojo) {
		pojoByTitleFor(pojo.getClass()).put(title, pojo);
	}
	public Object findByTitle(Class<?> javaClass, String title) {
		return pojoByTitleFor(javaClass).get(title);
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
