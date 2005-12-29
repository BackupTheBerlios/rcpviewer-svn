package org.essentialplatform.runtime.shared.persistence;


public abstract class AbstractObjectStore implements IObjectStore {
	
	public AbstractObjectStore(final String id) {
		_id = id;
	}
	
	private final String _id;
	public final String getId() {
		return _id;
	}

}
