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
import org.essentialplatform.runtime.server.database.IDatabaseServer;
import org.essentialplatform.runtime.server.database.hsqldb.HsqlDatabaseServer;
import org.essentialplatform.runtime.server.domain.bindings.RuntimeServerBinding;
import org.essentialplatform.runtime.server.remoting.IRemotingServer;
import org.essentialplatform.runtime.server.remoting.activemq.ActiveMqRemotingServer;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ITransactionProcessor;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.hibernate.HibernateTransactionProcessor;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.server.session.hibernate.HibernateServerSessionFactory;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.ObjectStoreHandleList;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Standalone server.
 * 
 * <p>
 * Configures the components that make up the server, (eg database server, 
 * JMS broker, transaction processor), and starts/shutdown.
 * 
 * @author Dan Haywood
 */
public final class StandaloneServer extends AbstractServer {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(StandaloneServer.class);
	}

	////////////////////////////////////////////////////////////
	// Constructor, init
	////////////////////////////////////////////////////////////

	public StandaloneServer() {
	}
	
	public void init() {

		//
		// YIKES.  THE EssentialProgModelRuntimeBuilder will populate the
		// Domain singleton with a hash of (name,IDomain) tuples.  But I then
		// need to say, for each of those Domains, what the list of
		// ObjectStores are.  Each (domainName, objectStore) then corresponds
		// to a SessionBinding.
		//
		//

		// First, build our Domain(s)
		// for now, only supporting "default".
		Binding.setBinding(
			new RuntimeServerBinding(new EssentialProgModelRuntimeBuilder()));

		
		// build the objectStoreListByDomain.
		//
		// for now, hard-coded to bind "default" domain to "default" objectstore,
		// implemented using Hibernate.
		//
		// in the future, intend to pick up the names of domains and map to the
		// Ids (and possibly implementations) of corresponding lists of objectstores.
		SessionBinding sessionBinding = new SessionBinding(DomainConstants.DEFAULT_NAME, PersistenceConstants.DEFAULT_OBJECT_STORE_ID);
		HibernateServerSessionFactory hssf = new HibernateServerSessionFactory(sessionBinding);
		bind(Domain.instance(sessionBinding.getDomainName()),hssf);
		for(IDomainClass dc: Domain.instance(DomainConstants.DEFAULT_NAME).classes()) {
			IDomainClassRuntimeBinding rccb = (IDomainClassRuntimeBinding)dc.getBinding();
			hssf.addClass(rccb.getJavaClass());
		}
		
		// tell the transaction processor a
		// for each SessionBinding tuple.
	    _transactionProcessor.init(objectStoreListByDomain);
	}

    ////////////////////////////////////////////////////////////
	// Domain -> ObjectStoreList
    ////////////////////////////////////////////////////////////

	private Map<IDomain, ObjectStoreHandleList<IServerSessionFactory>> objectStoreListByDomain = 
		new HashMap<IDomain, ObjectStoreHandleList<IServerSessionFactory>>();
	

	/**
	 * Associates a name with an object store that is capable of supporting
	 * objects from that domain.
	 * 
	 * @param domain
	 * @param serverSessionFactory
	 */
	private void bind(Domain domain, IServerSessionFactory serverSessionFactory) {
		ObjectStoreHandleList<IServerSessionFactory> objectStoreHandleList = objectStoreListByDomain.get(domain);
		if (objectStoreHandleList == null) {
			objectStoreHandleList = new ObjectStoreHandleList<IServerSessionFactory>();
		}
		objectStoreHandleList.add(serverSessionFactory);
	}

    ////////////////////////////////////////////////////////////
	// DomainBuilder
    ////////////////////////////////////////////////////////////

	
	

    ////////////////////////////////////////////////////////////
	// RemotingServer (injected).
    ////////////////////////////////////////////////////////////

	private IRemotingServer _remotingServer = new ActiveMqRemotingServer();
	public IRemotingServer getRemotingServer() {
		return _remotingServer;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified will default to 
	 * {@link ActiveMqRemotingServer}. 
	 * 
	 * @param remoting
	 */
	public void setRemoting(IRemotingServer remotingServer) {
		_remotingServer = remotingServer;
	}


    ////////////////////////////////////////////////////////////
	// Database Server (injected).
    ////////////////////////////////////////////////////////////
	
	private IDatabaseServer _databaseServer = new HsqlDatabaseServer();
	public IDatabaseServer getDatabaseServer() {
		return _databaseServer;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then will default to 
	 * {@link HsqlDatabaseServer}.
	 * 
	 * @param databaseServer
	 */
	public void setDatabaseServer(IDatabaseServer databaseServer) {
		_databaseServer = databaseServer;
	}
	
    ////////////////////////////////////////////////////////////
	// TransactionProcessor (injected)
    ////////////////////////////////////////////////////////////
	
	private ITransactionProcessor _transactionProcessor = 
		new HibernateTransactionProcessor();
	public ITransactionProcessor getTransactionProcessor() {
		return _transactionProcessor;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified will default to 
	 * {@link HibernateTransactionProcessor}. 
	 * 
	 * @param remoting
	 */
	public void setTransactionProcessor(
			ITransactionProcessor transactionProcessor) {
		_transactionProcessor = transactionProcessor;
	}
	
    ////////////////////////////////////////////////////////////
	// Lifecycle methods
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
	// main()
    ////////////////////////////////////////////////////////////

	/**
	 * Allow the StandaloneServer to be run as a standalone program.
	 * 
	 * <p>
	 * TODO: should process args / should be using the Spring config.
	 */
	public static void main(String[] args) {
		StandaloneServer server = new StandaloneServer();
		server.init();
		server.start();
	}
	
}