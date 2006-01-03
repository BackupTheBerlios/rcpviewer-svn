package org.essentialplatform.runtime.server.session;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;

public abstract class AbstractServerSession implements IServerSession {

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
