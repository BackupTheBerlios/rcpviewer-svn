package org.essentialplatform.server.remoting.xactnprocessor.hibernate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.server.persistence.IObjectStore;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.session.ObjectStoreHandleList;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.shared.transaction.changes.IChange;
import org.essentialplatform.runtime.shared.transaction.changes.IModificationChange;
import org.essentialplatform.runtime.shared.transaction.changes.InstantiationChange;
import org.essentialplatform.server.persistence.ApplyingChangesComparator;
import org.essentialplatform.server.remoting.xactnprocessor.AbstractTransactionProcessor;

public final class HibernateTransactionProcessor extends AbstractTransactionProcessor {

	/**
	 * 
	 * @param transaction
	 * @param distribution
	 * @param baos
	 */
	public void process(ITransaction transaction) {
		List<IChange> committedChanges = transaction.flattenedCommittedChanges();
		
		Collections.sort(committedChanges, new ApplyingChangesComparator());
		
		Map<SessionBinding, IServerSession> openedSessions = new HashMap<SessionBinding, IServerSession>();
		try {
			for(IChange change: committedChanges) {
				IDomainObject initiatingPojoDO = change.getInitiatingPojoDO();
				if (initiatingPojoDO == null) { // eg IrreversibleChange and other special cases
					continue;
				}
				SessionBinding sessionBinding = initiatingPojoDO.getSessionBinding();
				IServerSession session = openedSessions.get(sessionBinding);
				if (session == null) {
					IServerSessionFactory sessionFactory = lookupSessionFactoryFor(sessionBinding);
					session = sessionFactory.open();
					openedSessions.put(sessionBinding, session);
				}
				if (change instanceof InstantiationChange) {
					session.save(initiatingPojoDO);
				}
				if (change instanceof IModificationChange) {
					session.update(initiatingPojoDO);
				}
				if (change instanceof DeletionChange) {
					session.delete(initiatingPojoDO);
				}
			}
		} finally {
			for(IServerSession session: openedSessions.values()) {
				session.close();
			}
		}
	}

	private IServerSessionFactory lookupSessionFactoryFor(SessionBinding sessionBinding) {
		IDomain domain = Domain.instance(sessionBinding.getDomainName());
		ObjectStoreHandleList<IServerSessionFactory> factoryList = _objectStoreListByDomain.get(domain);
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
