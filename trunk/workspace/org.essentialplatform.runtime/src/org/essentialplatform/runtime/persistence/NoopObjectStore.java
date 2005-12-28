package org.essentialplatform.runtime.persistence;

import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * Does, erm, nothing.
 * 
 * <p>
 * Provided only for testing those pieces of the architecture that require a
 * non-null reference to an {@link IObjectStore} implementation.  Don't expect
 * it to store anything.
 * 
 * @author Dan Haywood
 */
public final class NoopObjectStore extends AbstractObjectStore {

	public NoopObjectStore(String id) {
		super(id);
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#save(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	public <T> void save(IDomainObject<T> domainObject)
			throws DuplicateObjectException {
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#update(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	public <T> void update(IDomainObject<T> domainObject)
			throws ConcurrencyException, DuplicateObjectException {
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#saveOrUpdate(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	public <T> void saveOrUpdate(IDomainObject<T> domainObject) {
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#delete(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	public void delete(IDomainObject<?> domainObject) {
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.IObjectStore#isPersistent(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	public <T> boolean isPersistent(IDomainObject<T> domainObject) {
		return false;
	}

	public void reset() {
	}

}
