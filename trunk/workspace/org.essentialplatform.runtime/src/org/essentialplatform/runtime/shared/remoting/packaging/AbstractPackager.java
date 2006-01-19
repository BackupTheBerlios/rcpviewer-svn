package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Adapter for implementations of {@link IPackager}.
 * 
 * @author Dan Haywood
 */
public abstract  class AbstractPackager implements IPackager {

	
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#pack(org.essentialplatform.runtime.shared.domain.IPojo)
	 */
	public abstract Object pack(IPojo pojo);


	public abstract SessionBinding unpackSessionBinding(Object packagedPojo);


	public abstract Handle unpackHandle(Object packagedPojo);


	public abstract void merge(IDomainObject domainObject, IHandleMap handleMap, Object packagedPojo);


}
