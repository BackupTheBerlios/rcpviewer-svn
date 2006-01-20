package org.essentialplatform.runtime.server.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.remoting.packaging.IPackager;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public abstract class AbstractServerSession implements IServerSession {

	public AbstractServerSession(SessionBinding sessionBinding) {
		_sessionBinding = sessionBinding;
	}

	private final SessionBinding _sessionBinding;
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getSessionBinding()
	 */
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}

	////////////////////////////////////////////////////////////////
	// just a sketch
	////////////////////////////////////////////////////////////////
	

	/**
	 * This is just a sketch of how the server-side will accept and deal with
	 * an pojo coming across the wire from the client.
	 */
	public void accept(IPojoPackage unmarshalledPackedPojo) {
		SessionBinding sessionBinding = _packager.unpackSessionBinding(unmarshalledPackedPojo);
		if (!this.getSessionBinding().equals(sessionBinding)) {
			throw new RuntimeException();
		}
		Handle unpackedHandle = _packager.unpackHandle(unmarshalledPackedPojo);
		Class<?> javaClass = unpackedHandle.getJavaClass();
		IDomainClass dc = Domain.lookupAny(javaClass);
		IDomainObject domainObject = getDomainObject(unpackedHandle);
		
		if (domainObject == null) {
			IDomainClassRuntimeBinding dcBinding = (IDomainClassRuntimeBinding)dc.getBinding();
			IPojo newPojo = (IPojo)dcBinding.newInstance(sessionBinding, PersistState.TRANSIENT, ResolveState.RESOLVED);
			domainObject = newPojo.domainObject();
		} else {
			// TODO: if there is a transaction in progress (client-side) then
			// need to do the merge but also indicate (somehow) that the current
			// xactn has been aborted.
		}
		
		// more stuff here...
	}

	////////////////////////////////////////////////////////////////
	// Packager
	////////////////////////////////////////////////////////////////
	
	private IPackager _packager;
	public IPackager getPackager() {
		return _packager;
	}
	public void setPackager(IPackager packager) {
		_packager = packager;
	}


}
