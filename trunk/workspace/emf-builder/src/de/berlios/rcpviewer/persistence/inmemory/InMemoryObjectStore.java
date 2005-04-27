package de.berlios.rcpviewer.persistence.inmemory;

import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainAspect;

public final class InMemoryObjectStore implements IObjectStore {

	/**
	 * TODO: this design doesn't deal with polymorphism.
	 * 
	 * @param title
	 * @param pojo
	 */
	public void persist(String title, Object pojo) {
		pojoByTitleFor(pojo.getClass()).put(title, pojo);
	}

	
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
	public Object findByTitle(Class<?> javaClass, String title) {
		return pojoByTitleFor(javaClass).get(title);
	}

	public void reset() {
		pojoByTitleByType.clear();
	}
	


}
