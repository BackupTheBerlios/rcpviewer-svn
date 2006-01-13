package org.essentialplatform.runtime.shared.domain.handle;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Partial implementation of {@link IHandleAssigner} that implements the
 * expected chain of responsibility pattern.
 * 
 * <p>
 * The actual implementation is factored out using the template pattern.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractHandleAssigner implements IHandleAssigner {

	/**
	 * next assigner; may be null.
	 */
	private final IHandleAssigner _nextAssigner;

	/**
	 * To create as last in chain.
	 *
	 */
	public AbstractHandleAssigner() {
		this(null);
	}

	/**
	 * To create as a link in chain.
	 *
	 */
	public AbstractHandleAssigner(IHandleAssigner nextAssigner) {
		_nextAssigner = nextAssigner;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner#assignHandleFor(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public final <T> Handle assignHandleFor(
			IDomainObject<T> domainObject) {
		Handle persistenceId = doGenerateHandleFor(domainObject);
		if (persistenceId != null) {
			domainObject.assignHandle(persistenceId);
			return persistenceId;
		}
		if (_nextAssigner != null) {
			return _nextAssigner.assignHandleFor(domainObject);
		}
		return null;
	}

	/**
	 * Mandatory hook method; there is no need for the implementor to assign
	 * the returned {@link Handle} to the object since this will be done by
	 * the template.
	 * 
	 * @param <T>
	 * @param domainObject
	 * @return
	 */
	protected abstract <T> Handle doGenerateHandleFor(IDomainObject<T> domainObject);
		

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner#nextAssigner()
	 */
	public final IHandleAssigner nextAssigner() {
		return _nextAssigner;
	}

}
