package org.essentialplatform.runtime.server.persistence.hibernate;

import org.essentialplatform.runtime.shared.domain.handle.AbstractHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Updates the {@link Handle} for a {@link IDomainObject} once the pojo that
 * it wraps has been persisted.
 * 
 * <p>
 * Most pojos will be mapped using an <tt>@Id</tt> assigned by the RDBMS
 * (eg using an <tt>identity</tt> column or <tt>SEQUENCE</tt>).  This assigner
 * reads the value and copies it into the {@link Handle}.
 * 
 * 
 * @author Dan Haywood
 *
 */
public final class HibernateHandleAssigner extends AbstractHandleAssigner {

	@Override
	protected <T> Handle doGenerateHandleFor(IDomainObject<T> domainObject) {
		throw new RuntimeException("Not yet implemented.");
	}


}
