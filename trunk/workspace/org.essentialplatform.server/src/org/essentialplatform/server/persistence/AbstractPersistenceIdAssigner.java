package org.essentialplatform.server.persistence;

import org.essentialplatform.runtime.domain.IDomainObject;

/**
 * Partial implementation of {@link IPersistenceIdAssigner} that implements the
 * expected chain of responsibility pattern.
 * 
 * <p>
 * The actual implementation is factored out using the template pattern.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractPersistenceIdAssigner implements IPersistenceIdAssigner {

	/**
	 * next assigner; may be null.
	 */
	private final IPersistenceIdAssigner _nextAssigner;

	/**
	 * To create as last in chain.
	 *
	 */
	public AbstractPersistenceIdAssigner() {
		this(null);
	}

	/**
	 * To create as a link in chain.
	 *
	 */
	public AbstractPersistenceIdAssigner(IPersistenceIdAssigner nextAssigner) {
		_nextAssigner = nextAssigner;
	}

	/*
	 * @see org.essentialplatform.runtime.persistence.IPersistenceIdAssigner#assignPersistenceIdFor(org.essentialplatform.runtime.domain.IDomainObject)
	 */
	public final <T> PersistenceId assignPersistenceIdFor(
			IDomainObject<T> domainObject) {
		PersistenceId persistenceId = doGeneratePersistenceIdFor(domainObject);
		if (persistenceId != null) {
			domainObject.assignPersistenceId(persistenceId);
			return persistenceId;
		}
		if (_nextAssigner != null) {
			return _nextAssigner.assignPersistenceIdFor(domainObject);
		}
		return null;
	}

	/**
	 * Mandatory hook method; there is no need for the implementor to assign
	 * the returned PersistenceId to the object since this will be done by
	 * the template.
	 * 
	 * @param <T>
	 * @param domainObject
	 * @return
	 */
	protected abstract <T> PersistenceId doGeneratePersistenceIdFor(IDomainObject<T> domainObject);
		
	
	/*
	 * @see org.essentialplatform.runtime.persistence.IPersistenceIdAssigner#nextAssigner()
	 */
	public final IPersistenceIdAssigner nextAssigner() {
		return _nextAssigner;
	}

}
