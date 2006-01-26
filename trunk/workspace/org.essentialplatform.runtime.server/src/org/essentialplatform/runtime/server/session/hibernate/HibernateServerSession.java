package org.essentialplatform.runtime.server.session.hibernate;

import java.util.Set;

import org.essentialplatform.runtime.server.session.AbstractServerSession;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.hibernate.Session;

public class HibernateServerSession extends AbstractServerSession {

	private Session _session;
	
	public HibernateServerSession(final SessionBinding sessionBinding, final Session session) {
		super(sessionBinding);
		_session = session; 
	}
	
	public void attach(IDomainObject<?> domainObject) {
		_session.merge(domainObject.getPojo());
	}

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

	public void close() {
		_session.close();
	}

	public IDomainObject getDomainObject(Handle handle) {
		// TODO Auto-generated method stub
		return null;
	}

	public Handle getHandle(IDomainObject domainObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean add(IDomainObject domainObject) throws IllegalStateException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean remove(IDomainObject domainObject) throws IllegalStateException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean remove(Handle handle) throws IllegalStateException {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<Handle> handles() {
		// TODO Auto-generated method stub
		return null;
	}


}
