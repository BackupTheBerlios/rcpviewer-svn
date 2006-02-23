package org.essentialplatform.runtime.server.session.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.builders.IClassLoader;
import org.essentialplatform.core.domain.filters.IFilter;
import org.essentialplatform.core.domain.filters.IdAttributeFilter;
import org.essentialplatform.runtime.server.session.AbstractServerSession;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.shared.domain.handle.AutoAddingHandleMap;
import org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory;
import org.essentialplatform.runtime.shared.persistence.ConcurrencyException;
import org.essentialplatform.runtime.shared.persistence.DuplicateObjectException;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.hibernate.Session;

public class HibernateServerSession extends AbstractServerSession {

	private final Session _session;
	HibernateDomainObjectFactory _hibernateDomainObjectFactory;
	
	public HibernateServerSession(final SessionBinding sessionBinding, final Session hibernateSession) {
		super(sessionBinding);
		_session = hibernateSession;
		_hibernateDomainObjectFactory = new HibernateDomainObjectFactory(sessionBinding, hibernateSession);
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSession#attach(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public void attach(IDomainObject<?> domainObject) {
		_session.merge(domainObject.getPojo());
	}


	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSession#close()
	 */
	public void close() {
		_session.close();
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSession#save(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public <T> void save(IDomainObject<T> domainObject) throws DuplicateObjectException {
		_session.save(domainObject.getPojo());
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSession#update(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public <T> void update(IDomainObject<T> domainObject) throws ConcurrencyException, DuplicateObjectException {
		_session.update(domainObject.getPojo());
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSession#saveOrUpdate(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public <T> void saveOrUpdate(IDomainObject<T> domainObject) {
		_session.saveOrUpdate(domainObject.getPojo());
		List<IAttribute> idAttributes = domainObject.getDomainClass().iAttributes(new IdAttributeFilter());
		Object[] updatedValues = new Object[idAttributes.size()];
		int i=0;
		for(IAttribute idAttribute: idAttributes) {
			updatedValues[i++] = domainObject.getAttribute(idAttribute).get();
		}
		domainObject.updateHandle(updatedValues);
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSession#delete(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public void delete(IDomainObject<?> domainObject) {
		_session.delete(domainObject.getPojo());
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSession#isPersistent(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public <T> boolean isPersistent(IDomainObject<T> domainObject) {
		Class javaClass = (Class)domainObject.getDomainClass().getClassRepresentation();
		return _session.get(javaClass, getId(domainObject.getHandle())) != null;
	}

	//////////////////////////////////////////////////////
	// HandleMap 
	// (delegates to HibernateDomainObjectFactory as required)
	//////////////////////////////////////////////////////

	public IDomainObject getDomainObject(Handle handle) {
		Class javaClass = handle.getJavaClass();
		IPojo pojo = (IPojo)_session.get(javaClass, getId(handle));
		if (pojo != null) {
			return pojo.domainObject();
		}
		IDomainObject domainObject = _hibernateDomainObjectFactory.createDomainObject(handle);
		return domainObject;
	}
	
	// TODO
	private Serializable getId(final Handle handle) {
		Serializable id = (Serializable)handle.getComponentValues()[0];
		return id;
	}

	public Handle getHandle(IDomainObject domainObject) {
		Handle domainObjectHandle = domainObject.getHandle();
		IDomainObject lookedUpDomainObject = getDomainObject(domainObjectHandle);
		return lookedUpDomainObject!=null?lookedUpDomainObject.getHandle():null;
	}

	public boolean add(IDomainObject domainObject) throws IllegalStateException {
		_session.persist(domainObject.getPojo());
		return true;
	}

	public boolean remove(IDomainObject domainObject) throws IllegalStateException {
		return remove(domainObject.getHandle());
	}

	public boolean remove(Handle handle) throws IllegalStateException {
		_session.delete(getId(handle));
		return true;
	}

	// TODO: should probably remove this from the IHandleMap interface.
	public Set<Handle> handles() {
		throw new UnsupportedOperationException();
	}

}
