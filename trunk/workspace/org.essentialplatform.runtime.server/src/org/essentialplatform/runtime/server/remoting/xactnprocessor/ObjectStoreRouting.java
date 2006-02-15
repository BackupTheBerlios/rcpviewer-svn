package org.essentialplatform.runtime.server.remoting.xactnprocessor;

import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.session.ObjectStoreRefList;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Factoring out the routing.
 * 
 * @author Dan Haywood
 *
 */
public class ObjectStoreRouting {

	private final Map<IDomain, ObjectStoreRefList<IServerSessionFactory>> _objectStoreListByDomain = 
		new HashMap<IDomain, ObjectStoreRefList<IServerSessionFactory>>();
	
	public Map<IDomain, ObjectStoreRefList<IServerSessionFactory>> getMap() {
		return _objectStoreListByDomain;
	}

	
	/**
	 * Associates a name with an object store that is capable of supporting
	 * objects from that domain.
	 * 
	 * @param domain
	 * @param serverSessionFactory
	 */
	public void bind(Domain domain, IServerSessionFactory serverSessionFactory) {
		ObjectStoreRefList<IServerSessionFactory> objectStoreHandleList = _objectStoreListByDomain.get(domain);
		if (objectStoreHandleList == null) {
			objectStoreHandleList = new ObjectStoreRefList<IServerSessionFactory>();
			_objectStoreListByDomain.put(domain, objectStoreHandleList);
		}
		objectStoreHandleList.add(serverSessionFactory);
	}

	
	public IServerSessionFactory lookupSessionFactoryFor(SessionBinding sessionBinding) {
		IDomain domain = Domain.instance(sessionBinding.getDomainName());
		ObjectStoreRefList<IServerSessionFactory> factoryList = _objectStoreListByDomain.get(domain);
		if (factoryList == null) {
			return null;
		}
		for(IServerSessionFactory sessionFactory: factoryList) {
			if (sessionFactory.getObjectStoreId().equals(sessionBinding.getObjectStoreId())) {
				return sessionFactory;
			}
		}
		return null;
	}

	


}
