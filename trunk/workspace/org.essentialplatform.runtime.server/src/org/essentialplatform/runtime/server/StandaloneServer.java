package org.essentialplatform.runtime.server;

import java.util.HashMap;
import java.util.List;
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
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ObjectStoreRouting;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.hibernate.HibernateTransactionProcessor;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.server.session.hibernate.HibernateServerSessionFactory;
import org.essentialplatform.runtime.shared.IRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.ObjectStoreRefList;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Standalone server.
 * 
 * <p>
 * The primary responsibilities of the standalone server are:
 * <ul> 
 * <li> to control the startup/shutdown lifecycele of the subcomponent servers
 *      (remoting and database), and
 * <li> to route requests for different domains to the correct place 
 *      (internally, the <tt>objectStoreListByDomain</tt> map).
 * </ul>
 *
 * <p>
 * In addition, it sets up the runtime binding. 
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

	/**
	 * Sets up the routing all provided server session factories. 
	 */
	public void init() {

		Binding.setBinding(_binding);

		for(IServerSessionFactory<IServerSession> serverSessionFactory: _serverSessionFactories) {
			SessionBinding sessionBinding = serverSessionFactory.init();
			Domain domain = Domain.instance(sessionBinding.getDomainName());
			_objectStoreRouting.bind(domain,serverSessionFactory);
		}
		
	}


	//////////////////////////////////////////////////////////////////
	// ObjectStoreRouting
	// (Domain -> ObjectStoreList)
	//////////////////////////////////////////////////////////////////
	
	private ObjectStoreRouting _objectStoreRouting;
	public ObjectStoreRouting getObjectStoreRouting() {
		return _objectStoreRouting;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param objectStoreListByDomain
	 */
	public void setObjectStoreRouting(ObjectStoreRouting objectStoreRouting) {
		_objectStoreRouting = objectStoreRouting;
	}
	
	
	//////////////////////////////////////////////////////////////////
	// Binding
	//////////////////////////////////////////////////////////////////
	
	private IRuntimeBinding _binding;

	public IRuntimeBinding getBinding() {
		return _binding;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param binding
	 */
	public void setBinding(IRuntimeBinding binding) {
		_binding = binding;
	}
	
	

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
	public void setRemotingServer(IRemotingServer remotingServer) {
		_remotingServer = remotingServer;
	}


    ////////////////////////////////////////////////////////////
	// ServerSessionFactory (injected)
    ////////////////////////////////////////////////////////////

	private List<IServerSessionFactory> _serverSessionFactories;
	public List<IServerSessionFactory> getServerSessionFactories() {
		return _serverSessionFactories;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param serverSessionFactories
	 */
	public void setServerSessionFactories(
			List<IServerSessionFactory> serverSessionFactories) {
		_serverSessionFactories = serverSessionFactories;
	}
	

    ////////////////////////////////////////////////////////////
	// Lifecycle methods
    ////////////////////////////////////////////////////////////

	
	/**
	 * Starts the database server and the remoting server.
	 */
	protected final boolean doStart() {
		startDatabaseServers();
		_remotingServer.start();
		return true;
	}
	protected final boolean doShutdown()  {
		_remotingServer.shutdown();
		shutdownDatabaseServers();
		return true;
	}

	private void startDatabaseServers() {
		for(IServerSessionFactory<IServerSession> serverSessionFactory: _serverSessionFactories) {
			serverSessionFactory.getDatabaseServer().start();
		}
	}

	private void shutdownDatabaseServers() {
		for(IServerSessionFactory<IServerSession> serverSessionFactory: _serverSessionFactories) {
			serverSessionFactory.getDatabaseServer().shutdown();
		}
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