package org.essentialplatform.runtime.server.remoting.xactnprocessor.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.AbstractTransactionProcessor;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.server.session.hibernate.HibernateServerSessionFactory;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.ObjectStoreRefList;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public final class HibernateTransactionProcessor extends AbstractTransactionProcessor {

	
	/**
	 * Process a transaction package.
	 * 
	 * <p>
	 * TODO: will need to enlist the sessions in XA transaction or similar?
	 *  
	 * @param transactionPackage
	 * @param distribution
	 * @param baos
	 */
	public void process(ITransactionPackage transactionPackage) {
		List<IPojoPackage> enlistedPojos = transactionPackage.enlistedPojos();
		
		Map<SessionBinding, IServerSession> openedSessions = 
				new HashMap<SessionBinding, IServerSession>();
		Map<IServerSession, List<IDomainObject>> modifiedObjectsBySession = 
			new LinkedHashMap<IServerSession, List<IDomainObject>>();
		for(IPojoPackage pojoPackage: enlistedPojos) {
			SessionBinding sessionBinding = pojoPackage.unpackSessionBinding();
			IServerSession session = openedSessions.get(sessionBinding);
			if (session == null) {
				IServerSessionFactory sessionFactory = 
					getObjectStoreRouting().lookupSessionFactoryFor(sessionBinding);
				session = sessionFactory.open();
				openedSessions.put(sessionBinding, session);
			}
			Handle handle = pojoPackage.unpackHandle();
			IDomainObject domainObject = session.getDomainObject(handle);
			getUnpackager().merge(domainObject, pojoPackage, session);
			List<IDomainObject> modifiedObjects = modifiedObjectsBySession.get(session);
			if (modifiedObjects == null) {
				modifiedObjects = new ArrayList<IDomainObject>();
				modifiedObjectsBySession.put(session, modifiedObjects);
			}
			modifiedObjects.add(domainObject);
		}

		for(IServerSession session: openedSessions.values()) {
			List<IDomainObject> modifiedObjects = modifiedObjectsBySession.get(session);
			for(IDomainObject modifiedDO: modifiedObjects) {
				session.saveOrUpdate(modifiedDO); // don't need to distinguish whether was PersistState transient or not
			}
		}
	

		for(IServerSession openSession: openedSessions.values()) {
			openSession.close();
		}
	
	}

	
	
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
