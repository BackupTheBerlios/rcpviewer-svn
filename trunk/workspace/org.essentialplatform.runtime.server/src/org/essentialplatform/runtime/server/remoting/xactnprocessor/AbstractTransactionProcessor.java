package org.essentialplatform.runtime.server.remoting.xactnprocessor;

import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.ObjectStoreHandleList;

/**
 * Adapter class.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractTransactionProcessor implements ITransactionProcessor {

	protected Map<IDomain, ObjectStoreHandleList<IServerSessionFactory>> _objectStoreListByDomain;
	public void init(Map<IDomain, ObjectStoreHandleList<IServerSessionFactory>> objectStoreListByDomain) {
		_objectStoreListByDomain = objectStoreListByDomain;
	}

	public void process(ITransactionPackage transactionPackage) {
		// does nothing
	}

}
