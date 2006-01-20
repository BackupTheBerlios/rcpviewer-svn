package org.essentialplatform.runtime.shared.domain.handle;

import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public final class DefaultDomainObjectFactory implements IDomainObjectFactory {
	
	/**
	 * 
	 * @param sessionBinding      - the session binding of any domain objects that are created
	 * @param initialPersistState - the initial persistence state for any domain objects that are created
	 * @param initialResolveState - the initial persistence state for any domain objects that are created
	 */
	public DefaultDomainObjectFactory(SessionBinding sessionBinding, PersistState initialPersistState, ResolveState initialResolveState, IHandleAssigner handleAssigner) {
		_sessionBinding = sessionBinding;
		_initialPersistState = initialPersistState;
		_initialResolveState = initialResolveState;
		_handleAssigner = handleAssigner;
	}

	private final SessionBinding _sessionBinding;
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getSessionBinding()
	 */
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}

	private final IHandleAssigner _handleAssigner;
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getHandleAssigner()
	 */
	public IHandleAssigner getHandleAssigner() {
		return _handleAssigner;
	}
	

	private final PersistState _initialPersistState;
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getInitialPersistState()
	 */
	public PersistState getInitialPersistState() {
		return _initialPersistState;
	}

	private final ResolveState _initialResolveState;
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#getInitialResolveState()
	 */
	public ResolveState getInitialResolveState() {
		return _initialResolveState;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#createDomainObject(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public <T> IDomainObject<T> createDomainObject(IDomainClass domainClass) {
		final IDomainObject domainObject = createDomainObjectInternal(domainClass);
		_handleAssigner.assignHandleFor(domainObject);
		return domainObject;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory#createDomainObject(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public <T> IDomainObject<T> createDomainObject(Handle handle) {
		Class<?> javaClass = handle.getJavaClass();
		IDomainClass domainClass = Domain.lookupAny(javaClass);
		final IDomainObject domainObject = createDomainObjectInternal(domainClass);
		domainObject.assignHandle(handle);
		return domainObject;
	}

	private <T> IDomainObject createDomainObjectInternal(IDomainClass domainClass) {
		final IDomainClassRuntimeBinding classBinding = 
			(IDomainClassRuntimeBinding)domainClass.getBinding();
		T pojo = (T)classBinding.newInstance(_sessionBinding, _initialPersistState, _initialResolveState);
		return ((IPojo)pojo).domainObject();
	}


}
