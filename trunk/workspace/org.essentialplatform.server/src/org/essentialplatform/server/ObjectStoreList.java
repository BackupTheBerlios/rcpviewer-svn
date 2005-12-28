package org.essentialplatform.server;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.essentialplatform.runtime.persistence.IObjectStore;

/**
 * cf SessionList on the client-side.
 * 
 *
 * @author Dan Haywood
 */
public class ObjectStoreList implements Iterable<IObjectStore> {
	private List<IObjectStore> objectStoreList = new ArrayList<IObjectStore>();
	
	public void add(IObjectStore objectStore) {
		objectStoreList.add(objectStore);
	}

	public void remove(IObjectStore objectStore) {
		objectStoreList.remove(objectStore);
	}

	public Iterator<IObjectStore> iterator() {
		return objectStoreList.iterator();
	}

}
