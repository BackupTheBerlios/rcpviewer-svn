package org.essentialplatform.server.persistence.hibernate;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.AbstractObjectStore;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;
import org.essentialplatform.runtime.shared.persistence.IObjectStore;

public final class HibernateObjectStore extends AbstractObjectStore {

	public HibernateObjectStore(final String id) {
		super(id);
	}

	///////////////////////////////////////////////////////////////

	public <T> void save(IDomainObject<T> domainObject)
			throws DuplicateObjectException {
		// TODO Auto-generated method stub

	}

	public <T> void update(IDomainObject<T> domainObject)
			throws ConcurrencyException, DuplicateObjectException {
		// TODO Auto-generated method stub

	}

	public <T> void saveOrUpdate(IDomainObject<T> domainObject) {
		// TODO Auto-generated method stub

	}

	public void delete(IDomainObject<?> domainObject) {
		// TODO Auto-generated method stub

	}

	public <T> boolean isPersistent(IDomainObject<T> domainObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
