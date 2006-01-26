package org.essentialplatform.runtime.client.domain;

import org.essentialplatform.progmodel.essential.app.InDomain;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.DomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;

/**
 * Defines IPojo (based on {@link InDomain} annotation) and introduces
 * {@link IDomainObject} member.
 */
public aspect PojoAspect {
	
	/**
	 * All pojos that are have an {@link InDomain} annotation should implement 
	 * {@link IPojo}. 
	 */
	declare parents: (@InDomain *) implements IPojo;

	private IDomainObject IPojo._domainObject;

	/**
	 * Lazily creates the {@link IDomainObject} wrapper for a given
	 * {@link IPojo}.
	 * 
	 * <p>
	 * The wrapper is created lazily because the field itself is not
	 * transmitted across the wire.
	 */
	public synchronized IDomainObject IPojo.domainObject() {
		if (_domainObject == null) {
			_domainObject = new DomainObject(this);
		}
		return _domainObject;
	}

}
