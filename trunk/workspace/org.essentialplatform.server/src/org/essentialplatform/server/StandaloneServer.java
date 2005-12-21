package org.essentialplatform.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

import org.essentialplatform.remoting.IRemoting;
import org.essentialplatform.remoting.server.ServerRemoting;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.transaction.changes.IChange;
import org.essentialplatform.runtime.transaction.changes.IModificationChange;
import org.essentialplatform.runtime.transaction.changes.InstantiationChange;
import org.essentialplatform.server.database.IDatabaseServer;
import org.essentialplatform.server.database.hsqldb.HsqlDatabaseServer;
import org.essentialplatform.server.persistence.ApplyingChangesComparator;

/**
 * Standalone server.
 * 
 * @author Dan Haywood
 *
 */
public class StandaloneServer {

	private IRemoting _remoting;
	private IDatabaseServer _databaseServer;

	public StandaloneServer() {
		_databaseServer = new HsqlDatabaseServer();
		_remoting = new ServerRemoting();
	}
	
	/**
	 * Starts the database server and the remoting.
	 *
	 */
	public void start() {
		_databaseServer.start();
		_remoting.start();
	}
	

	// just a sketch
	public void consume(byte[] bytes) {
		
		Object obj = _remoting.getMarshalling().unmarshalFrom(new ByteArrayInputStream(bytes));
		
		ITransaction unmarshalledXactn = (ITransaction)obj;

		apply(unmarshalledXactn);
	}
	

	public IRemoting getRemoting() {
		return _remoting;
	}
	/**
	 * Dependency injection.
	 * 
	 * @param remoting
	 */
	public void setRemoting(IRemoting remoting) {
		_remoting = remoting;
	}
	


	/**
	 * 
	 * @param transaction
	 * @param distribution
	 * @param baos
	 */
	public void apply(ITransaction transaction) {

		List<IChange> committedChanges = transaction.flattenedCommittedChanges();
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
