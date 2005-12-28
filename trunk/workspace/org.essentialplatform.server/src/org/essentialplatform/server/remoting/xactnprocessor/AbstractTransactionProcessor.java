package org.essentialplatform.server.remoting.xactnprocessor;

import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.server.ObjectStoreList;

/**
 * Adapter class.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractTransactionProcessor implements ITransactionProcessor {

	protected Map<IDomain, ObjectStoreList> _objectStoreListByDomain;
	public void init(Map<IDomain, ObjectStoreList> objectStoreListByDomain) {
		_objectStoreListByDomain = objectStoreListByDomain;
	}

	public void process(ITransaction transaction) {
		// does nothing
	}

}
