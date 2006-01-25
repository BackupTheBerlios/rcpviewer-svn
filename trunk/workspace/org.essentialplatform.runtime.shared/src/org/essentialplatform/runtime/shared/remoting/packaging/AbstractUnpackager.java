package org.essentialplatform.runtime.shared.remoting.packaging;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Adapter for implementations of {@link IPackager}.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractUnpackager implements IUnpackager {

	protected abstract Logger getLogger(); 
	
	
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#unpackSessionBinding(org.essentialplatform.runtime.shared.remoting.packaging.ISessionBindingPackage)
	 */
	public abstract SessionBinding unpackSessionBinding(ISessionBindingPackage packagedSessionBinding);


	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#unpackHandle(org.essentialplatform.runtime.shared.remoting.packaging.IHandlePackage)
	 */
	public abstract Handle unpackHandle(IHandlePackage handlePackage);


	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#merge(org.essentialplatform.runtime.shared.domain.IDomainObject, org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage, org.essentialplatform.runtime.shared.domain.handle.IHandleMap)
	 */
	public abstract void merge(IDomainObject domainObject, IPojoPackage packagedPojo, IHandleMap handleMap);

	
	/*
	 * Default implementation does nothing.
	 *  
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#optimize(org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling)
	 */
	public void optimize(IMarshalling marshalling) {
		// does nothing.
	}
}
