package org.essentialplatform.runtime.server.remoting.xactnprocessor;

import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.ObjectStoreHandleList;

public interface ITransactionProcessor {
	
	public void init(Map<IDomain, ObjectStoreHandleList<IServerSessionFactory>> serverSessionFactoryListByDomain);
	
	public void process(ITransactionPackage transactionPackage);

}
