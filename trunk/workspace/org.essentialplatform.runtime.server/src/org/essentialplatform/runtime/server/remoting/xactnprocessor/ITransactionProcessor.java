package org.essentialplatform.runtime.server.remoting.xactnprocessor;

import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.ObjectStoreRefList;

public interface ITransactionProcessor {
	
	public void process(ITransactionPackage transactionPackage);

	
	public ObjectStoreRouting getObjectStoreRouting();
	public void setObjectStoreRouting(ObjectStoreRouting objectStoreRouting);

}
