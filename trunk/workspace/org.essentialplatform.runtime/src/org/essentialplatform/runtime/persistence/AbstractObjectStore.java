package org.essentialplatform.runtime.persistence;

import org.essentialplatform.runtime.domain.IDomainObject;

public abstract class AbstractObjectStore implements IObjectStore {
	
	public AbstractObjectStore(final String id) {
		_id = id;
	}
	
	private final String _id;
	public final String getId() {
		return _id;
	}

}
