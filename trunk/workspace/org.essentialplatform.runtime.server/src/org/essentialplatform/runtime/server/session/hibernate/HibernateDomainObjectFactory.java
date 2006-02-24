package org.essentialplatform.runtime.server.session.hibernate;

import org.essentialplatform.core.deployment.IDomainBinding;
import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.runtime.server.domain.bindings.RuntimeServerBinding.RuntimeServerDomainBinding;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.hibernate.Session;
import org.osgi.framework.Bundle;

public class HibernateDomainObjectFactory implements IDomainObjectFactory {

	public HibernateDomainObjectFactory(final SessionBinding sessionBinding, final Session hibernateSession) {
		_sessionBinding = sessionBinding;
		_hibernateSession = hibernateSession;
	}

	
	private final Session _hibernateSession;
	public Session getHibernateSession() {
		return _hibernateSession;
	}

	private final SessionBinding _sessionBinding;
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getSessionBinding()
	 */
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}


	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#createDomainObject(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public <T> IDomainObject<T> createDomainObject(Handle handle) {
		Class javaClass = handle.getJavaClass();
		Domain domain = Domain.instance(_sessionBinding.getDomainName());
		IDomainClass domainClass = domain.lookup(javaClass);
		IDomainObject<T> domainObject = createDomainObject(domainClass);
		domainObject.assignHandle(handle);
		return domainObject;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#createDomainObject(org.essentialplatform.core.domain.IDomainClass)
	 */
	public <T> IDomainObject<T> createDomainObject(IDomainClass domainClass) {
		IDomainClassRuntimeBinding<T> binding = (IDomainClassRuntimeBinding)domainClass.getBinding();
		IPojo pojo = (IPojo)binding.newInstance(getSessionBinding(), getInitialPersistState(), getInitialResolveState());
		return (IDomainObject<T>)pojo.domainObject();
	}


	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getHandleAssigner()
	 */
	public IHandleAssigner getHandleAssigner() {
		// TODO:
		throw new UnsupportedOperationException("should this be in the IDomainObjectFactory interface???");
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getInitialPersistState()
	 */
	public PersistState getInitialPersistState() {
		return PersistState.PERSISTED;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getInitialResolveState()
	 */
	public ResolveState getInitialResolveState() {
		return ResolveState.MUTATING;
	}

}
