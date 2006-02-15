package org.essentialplatform.runtime.server.remoting.xactnprocessor.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.AbstractTransactionProcessor;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.server.session.hibernate.HibernateServerSessionFactory;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.ObjectStoreRefList;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public final class HibernateTransactionProcessor extends AbstractTransactionProcessor {

	
	/**
	 * 
	 * @param transaction
	 * @param distribution
	 * @param baos
	 */
	public void process(ITransactionPackage transaction) {
		List<IPojoPackage> enlistedPojos = transaction.enlistedPojos();
		
		// open up all the sessions we will need
		Map<SessionBinding, IServerSession> openedSessions = 
				new HashMap<SessionBinding, IServerSession>();
		for(IPojoPackage pojoPackage: enlistedPojos) {
			SessionBinding sessionBinding = pojoPackage.unpackSessionBinding();
			IServerSessionFactory sessionFactory = 
				getObjectStoreRouting().lookupSessionFactoryFor(sessionBinding);
			IServerSession session = sessionFactory.open();
			openedSessions.put(sessionBinding, session);
		}
		
		// TODO: will probably need to enlist these within an XA transaction
		
		// this stuff commented out, being replaced with the package approach.
//		List<IChange> committedChanges = transaction.flattenedCommittedChanges();
//		
//		Collections.sort(committedChanges, new ApplyingChangesComparator());
//		
//		Map<SessionBinding, IServerSession> openedSessions = new HashMap<SessionBinding, IServerSession>();
//		try {
//			for(IChange change: committedChanges) {
//				IDomainObject initiatingPojoDO = change.getInitiatingPojoDO();
//				if (initiatingPojoDO == null) { // eg IrreversibleChange and other special cases
//					continue;
//				}
//				SessionBinding sessionBinding = initiatingPojoDO.getSessionBinding();
//				IServerSession session = openedSessions.get(sessionBinding);
//				if (session == null) {
//					IServerSessionFactory sessionFactory = lookupSessionFactoryFor(sessionBinding);
//					session = sessionFactory.open();
//					openedSessions.put(sessionBinding, session);
//				}
//				if (change instanceof InstantiationChange) {
//					session.save(initiatingPojoDO);
//				}
//				if (change instanceof IModificationChange) {
//					session.update(initiatingPojoDO);
//				}
//				if (change instanceof DeletionChange) {
//					session.delete(initiatingPojoDO);
//				}
//			}
//		} finally {
//			for(IServerSession session: openedSessions.values()) {
//				session.close();
//			}
//		}
	}


}
