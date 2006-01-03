package org.essentialplatform.runtime.server.session;

public abstract class AbstractServerSessionFactory<V> implements IServerSessionFactory<V> {

	public AbstractServerSessionFactory() {
	}

	public AbstractServerSessionFactory(final String objectStoreId) {
		_objectStoreId = objectStoreId;
	}
	
	private String _objectStoreId;
	public String getObjectStoreId() {
		return _objectStoreId;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param objectStoreId
	 */
	public void setObjectStoreId(String objectStoreId) {
		_objectStoreId = objectStoreId;
	}


}
