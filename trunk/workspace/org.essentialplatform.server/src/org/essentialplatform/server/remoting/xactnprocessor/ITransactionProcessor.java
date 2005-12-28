package org.essentialplatform.server.remoting.xactnprocessor;

import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.server.ObjectStoreList;

public interface ITransactionProcessor {
	
	public void init(Map<IDomain, ObjectStoreList> objectStoreListByDomain);
	
	public void process(ITransaction transaction);

}
