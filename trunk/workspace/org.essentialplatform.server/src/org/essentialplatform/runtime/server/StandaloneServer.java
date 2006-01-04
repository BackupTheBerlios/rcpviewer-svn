package org.essentialplatform.runtime.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.RuntimeClientBinding;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.server.database.IDatabaseServer;
import org.essentialplatform.runtime.server.database.hsqldb.HsqlDatabaseServer;
import org.essentialplatform.runtime.server.persistence.IObjectStore;
import org.essentialplatform.runtime.server.persistence.hibernate.HibernateObjectStore;
import org.essentialplatform.runtime.server.remoting.IRemotingServer;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqRemotingServer;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ITransactionProcessor;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.hibernate.HibernateTransactionProcessor;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.server.session.hibernate.HibernateServerSessionFactory;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.ObjectStoreHandleList;

/**
 * Standalone server.
 * 
 * <p>
 * Configures the components that make up the server, (eg database server, 
 * JMS broker, transaction processor).
 * 
 * @author Dan Haywood
 */
public class StandaloneServer extends AbstractService {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(StandaloneServer.class);
	}

	private ITransactionManager _transactionManager;
	
	////////////////////////////////////////////////////////////

	private Map<IDomain, ObjectStoreHandleList<IServerSessionFactory>> objectStoreListByDomain = 
		new HashMap<IDomain, ObjectStoreHandleList<IServerSessionFactory>>();
	
	public StandaloneServer() {

		//
		// YIKES.  THE EssentialProgModelRuntimeBuilder will populate the
		// Domain singleton with a hash of (name,IDomain) tuples.  But I then
		// need to say, for each of those Domains, what the list of
		// ObjectStores are.  Each (domainName, objectStore) then corresponds
		// to a SessionBinding.
		//
		//
		// ALSO: rename ISessionManager to IClientSessionManager, etc
		// and consider introducing a new plugin for client-side only stuff 
		// (now that I've finally identified some stuff that I do want to
		// use only on the client.
		//

		// First, build our Domain(s)
		// for now, only supporting "default".
		Binding.setBinding(
				// TODO: should be using SERVER bindings ... 
			new RuntimeClientBinding(new EssentialProgModelRuntimeBuilder()));

		
		// build the objectStoreListByDomain.
		//
		// for now, hard-coded to bind "default" domain to "default" objectstore,
		// implemented using Hibernate.
		//
		// in the future, intend to pick up the names of domains and map to the
		// Ids (and possibly implementations) of corresponding lists of objectstores.
		HibernateServerSessionFactory hssf = 
			new HibernateServerSessionFactory(PersistenceConstants.DEFAULT_OBJECT_STORE_ID);
		bind(
			Domain.instance(DomainConstants.DEFAULT_NAME), 
			hssf);
		for(IDomainClass dc: Domain.instance(DomainConstants.DEFAULT_NAME).classes()) {
			// TODO: should be using SERVER bindings ... 
			RuntimeClientBinding.RuntimeClientClassBinding rccb = 
				(RuntimeClientBinding.RuntimeClientClassBinding)dc.getBinding();
			hssf.addClass(rccb.getJavaClass());
		}
		
		// tell the transaction processor a
		// for each SessionBinding tuple.
	    _transactionProcessor.init(objectStoreListByDomain);

	    // is a xactnMgr needed?
		_transactionManager = TransactionManager.instance();

	}

	/**
	 * Associates a name with an object store that is capable of supporting
	 * objects from that domain.
	 * 
	 * @param domain
	 * @param objectStore
	 */
	private void bind(Domain domain, IServerSessionFactory objectStore) {
		ObjectStoreHandleList<IServerSessionFactory> objectStoreList = objectStoreListByDomain.get(domain);
		if (objectStoreList == null) {
			objectStoreList = new ObjectStoreHandleList<IServerSessionFactory>();
		}
		objectStoreList.add(objectStore);
	}

    ////////////////////////////////////////////////////////////

	private IRemotingServer _remotingServer = new ActiveMqRemotingServer();
	public IRemotingServer getRemotingServer() {
		return _remotingServer;
	}
	/**
	 * Optionally specify the {@link IRemotingServer} to use, eg by dependency injection.
	 * 
	 * <p>
	 * If not specified will default to {@link ActiveMqRemotingServer}.
	 * @param remoting
	 */
	public void setRemoting(IRemotingServer remotingServer) {
		_remotingServer = remotingServer;
	}


    ////////////////////////////////////////////////////////////
	
	private IDatabaseServer _databaseServer = new HsqlDatabaseServer();
	public IDatabaseServer getDatabaseServer() {
		return _databaseServer;
	}
	/**
	 * Optionally specify the {@link IDatabaseServer} to use, eg by dependency injection.
	 * 
	 * <p>
	 * If not specified then will default to {@link HsqlDatabaseServer}.
	 * 
	 * @param databaseServer
	 */
	public void setDatabaseServer(IDatabaseServer databaseServer) {
		_databaseServer = databaseServer;
	}
	
    ////////////////////////////////////////////////////////////
	
	private ITransactionProcessor _transactionProcessor = new HibernateTransactionProcessor();
	public ITransactionProcessor getTransactionProcessor() {
		return _transactionProcessor;
	}
	/**
	 * Optionally specify the {@link ITransactionProcessor} to use, eg by 
	 * dependency injection.
	 * 
	 * <p>
	 * If not specified will default to {@link HibernateTransactionProcessor}.
	 * @param remoting
	 */
	public void setTransactionProcessor(
			ITransactionProcessor transactionProcessor) {
		_transactionProcessor = transactionProcessor;
	}
	
    ////////////////////////////////////////////////////////////

	
	/**
	 * Starts the database server and the remoting server.
	 */
	protected boolean doStart() {
		_databaseServer.start();
		_remotingServer.setTransactionProcessor(_transactionProcessor);
		_remotingServer.start();
		return true;
	}

	
	protected boolean doShutdown()  {
		_remotingServer.shutdown();
		_databaseServer.shutdown();
		return true;
	}

    ////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		StandaloneServer server = new StandaloneServer();
		server.start();
	}
	
}