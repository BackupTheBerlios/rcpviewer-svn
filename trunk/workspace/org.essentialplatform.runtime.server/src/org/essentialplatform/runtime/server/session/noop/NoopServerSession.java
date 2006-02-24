package org.essentialplatform.runtime.server.session.noop;

import java.util.Collections;
import java.util.Set;

import org.essentialplatform.runtime.server.session.AbstractServerSession;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * For testing, mostly.
 * 
 * @author Dan Haywood
 */
public final class NoopServerSession extends AbstractServerSession {

	
	public NoopServerSession(SessionBinding sessionBinding) {
		super(sessionBinding);
	}
	
	public void attach(IDomainObject<?> domainObject) {
		// TODO Auto-generated method stub
		
	}

	public <T> void save(IDomainObject<T> domainObject) throws DuplicateObjectException {
		// TODO Auto-generated method stub
		
	}

	public <T> void update(IDomainObject<T> domainObject) throws ConcurrencyException, DuplicateObjectException {
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
		// TODO Auto-generated method stub
		
	}

	public SessionBinding getSessionBinding() {
		// TODO Auto-generated method stub
		return null;
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
		return Collections.EMPTY_SET;
	}

	public void nowPersisting() {
		// TODO Auto-generated method stub
		
	}


}
