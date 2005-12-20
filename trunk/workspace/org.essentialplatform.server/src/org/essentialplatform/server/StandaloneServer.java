package org.essentialplatform.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

import org.essentialplatform.remoting.server.ServerRemoting;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.transaction.changes.IChange;
import org.essentialplatform.runtime.transaction.changes.IModificationChange;
import org.essentialplatform.runtime.transaction.changes.InstantiationChange;
import org.essentialplatform.server.persistence.ApplyingChangesComparator;

/**
 * Standalone server.
 * 
 * @author Dan Haywood
 *
 */
public class StandaloneServer {

	/**
	 * This will get moved to the server at some stage.
	 * 
	 * <p>
	 * TODO: there is a downcast to IPojo for ITransactable; need to figure out.
	 * 
	 * @param transaction
	 * @param distribution
	 * @param baos
	 */
	private void serverApplyTransaction(ByteArrayOutputStream baos) {

		org.essentialplatform.remoting.IRemoting serverRemoting = new ServerRemoting();

		Object obj = serverRemoting.getMarshalling().unmarshalFrom(new ByteArrayInputStream(baos.toByteArray()));
		
		ITransaction unmarshalledXactn = (ITransaction)obj;

		List<IChange> committedChanges = unmarshalledXactn.flattenedCommittedChanges();
		Collections.sort(committedChanges, new ApplyingChangesComparator());

		for(IChange change: committedChanges) {
			if (change instanceof InstantiationChange) {
				IPojo pojo = (IPojo)change.getInitiatingPojo();
				IDomainObject<?> domainObject = pojo.domainObject();
				IObjectStore objectStore = domainObject.getSession().getObjectStore();
				objectStore.save(domainObject);
			}
			if (change instanceof IModificationChange) {
				IPojo pojo = (IPojo)change.getInitiatingPojo();
				IDomainObject<?> domainObject = pojo.domainObject();
				IObjectStore objectStore = domainObject.getSession().getObjectStore();
				objectStore.update(domainObject);
			}
			if (change instanceof DeletionChange) {
				IPojo pojo = (IPojo)change.getInitiatingPojo();
				IDomainObject<?> domainObject = pojo.domainObject();
				IObjectStore objectStore = domainObject.getSession().getObjectStore();
				objectStore.delete(domainObject);
			}
		}
	}


}
