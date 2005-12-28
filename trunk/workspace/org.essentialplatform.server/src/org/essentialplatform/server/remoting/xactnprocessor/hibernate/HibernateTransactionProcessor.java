package org.essentialplatform.server.remoting.xactnprocessor.hibernate;

import java.util.Collections;
import java.util.List;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.session.SessionBinding;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.transaction.changes.IChange;
import org.essentialplatform.runtime.transaction.changes.IModificationChange;
import org.essentialplatform.runtime.transaction.changes.InstantiationChange;
import org.essentialplatform.server.ObjectStoreList;
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
		
		for(IChange change: committedChanges) {
			IDomainObject initiatingPojoDO = change.getInitiatingPojoDO();
			if (initiatingPojoDO == null) { // eg IrreversibleChange and other special cases
				continue;
			}
			SessionBinding sessionBinding = initiatingPojoDO.getSessionBinding();
			IObjectStore objectStore = lookupObjectStoreFor(sessionBinding);
			if (change instanceof InstantiationChange) {
				objectStore.save(initiatingPojoDO);
			}
			if (change instanceof IModificationChange) {
				objectStore.update(initiatingPojoDO);
			}
			if (change instanceof DeletionChange) {
				objectStore.delete(initiatingPojoDO);
			}
		}
	}

	private IObjectStore lookupObjectStoreFor(SessionBinding sessionBinding) {
		IDomain domain = Domain.instance(sessionBinding.getDomainName());
		ObjectStoreList objectStoreList = _objectStoreListByDomain.get(domain);
		if (objectStoreList == null) {
			return null;
		}
		for(IObjectStore objectStore: objectStoreList) {
			if (objectStore.getId().equals(sessionBinding.getObjectStoreId())) {
				return objectStore;
			}
		}
		return null;
	}

}
