package org.essentialplatform.runtime.server.persistence;


public abstract class AbstractObjectStore implements IObjectStore {
	
	public AbstractObjectStore(final String id) {
		_id = id;
	}
	
	private final String _id;
	public final String getId() {
		return _id;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.session.IObjectStoreRef#getObjectStoreId()
	 */
	public String getObjectStoreId() {
		return getId();
	}


}
