package org.essentialplatform.runtime.server.tests.session.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.jms.JMSException;

import org.apache.commons.io.IOUtils;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.runtime.client.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.server.database.hsqldb.HsqlDatabaseServer;
import org.essentialplatform.runtime.server.domain.bindings.RuntimeServerBinding;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ObjectStoreRouting;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.hibernate.HibernateTransactionProcessor;
import org.essentialplatform.runtime.server.session.hibernate.HibernateServerSessionFactory;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.ProgrammaticDomainDefinition;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardUnpackager;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

/**
 * This test does NOT send through remoting mechanism.  Instead, we start with
 * the Binding set to the client-side binding, then reset the Binding and set
 * it to server-side binding to test that the xactn is handled correctly
 * "server-side".
 * 
 * <p>
 * The following code was removed for remoting (copied up here in case want to
 * develop another test that needs all this gumph):
 * <pre>
 * private ActiveMqRemotingServer _remotingServer;
 * private ClientRemoting _clientRemoting;
 * private ActiveMqTransport _clientTransport;
 * 
 * setup() {
 * 	_remotingServer = new ActiveMqRemotingServer();
 *	_remotingServer.setTransactionProcessor(_hibernateTransactionProcessor);
 *	_remotingServer.setMessageListenerEnabled(true);
 *		
 *	_remotingServer.dependenciesInjected();
 *	_remotingServer.start();
 *
 *	_clientTransport = new ActiveMqTransport();
 *	_clientRemoting = new ClientRemoting();
 *	_clientRemoting.setTransport(_clientTransport);
 *	_clientRemoting.start();
 *  ...
 * }
 * tearDown() {
 *	if (_clientTransport != null) {
 *		_clientTransport.shutdown();
 *	}
 *	_hibernateTransactionProcessor = null;
 *	if (_remotingServer != null) {
 *		_remotingServer.shutdown();
 *	}
 *  ...
 * }
 * test...() {
 * 	_clientRemoting.send(xactn);
 *	// and then need some mechanism to wait into the HibernateTransactionProcessor
 *	// to assert on what it does.
 * }
 * 
 * </pre>
 */
public class TestHibernateServerSession extends AbstractRuntimeClientTestCase {

	private HibernateTransactionProcessor _hibernateTransactionProcessor;

	private ObjectStoreRouting _objectStoreRouting;
	private HibernateServerSessionFactory _hibernateServerSessionFactory;
	private HsqlDatabaseServer _hsqlDatabaseServer;
	private Properties _hibernateProperties;
	private ProgrammaticDomainDefinition _domainDefinition;
	
	private StandardPackager _packager;
	private StandardUnpackager _unpackager;

	public TestHibernateServerSession() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		_domainDefinition = new ProgrammaticDomainDefinition(
				"default", getDomainBuilder(), Department.class.getName());
		_domainDefinition.registerClasses();
		domain = Domain.instance(_domainDefinition.getName()); // replace that provided by superclass.

		_hsqlDatabaseServer = new HsqlDatabaseServer();
		_hsqlDatabaseServer.setDatabaseName("default");
		_hsqlDatabaseServer.start();
		
		_hibernateServerSessionFactory = new HibernateServerSessionFactory();
		_hibernateServerSessionFactory.setDatabaseServer(_hsqlDatabaseServer);
		_hibernateProperties = new Properties();
		_hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		_hibernateProperties.setProperty("hibernate.hbm2ddl.auto","create-drop");
		_hibernateProperties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
		_hibernateServerSessionFactory.setHibernateProperties(_hibernateProperties);

		_hibernateServerSessionFactory.setDomainDefinition(_domainDefinition);
		_hibernateServerSessionFactory.init();

		_objectStoreRouting = new ObjectStoreRouting();
		_objectStoreRouting.bind(domain, _hibernateServerSessionFactory);
		
		_hibernateTransactionProcessor = new HibernateTransactionProcessor();
		_hibernateTransactionProcessor.setObjectStoreRouting(_objectStoreRouting);
		_packager = new StandardPackager();
		_unpackager = new StandardUnpackager();
		_hibernateTransactionProcessor.setUnpackager(_unpackager);

	}

	@Override
	protected void tearDown() throws Exception {
		if (_hsqlDatabaseServer != null) {
			_hsqlDatabaseServer.shutdown();
		}
		super.tearDown();
	}
	

	protected void switchToClientBindings() {
		switchTo(new RuntimeClientBinding());
	}

	protected void switchToServerBindings() {
		switchTo(new RuntimeServerBinding());
	}

	private void switchTo(Binding binding) {
        Binding.reset();
		Binding.setBinding(binding);
		domain.replaceBindings(Binding.getBinding());
	}


	public void xtestCanInstantiate() throws InterruptedException, JMSException, ClassNotFoundException, SQLException {
		
        // create a xactn
        IDomainClass departmentDC = lookupAny(Department.class);
		IDomainObject<Department> departmentDO = clientSession.create(departmentDC);
		Department departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
//		departmentPojo.addEmployee("Joe", "Blow");
//		departmentPojo.addEmployee("Mary", "Doe");

		ITransaction xactn = transactionManager.getCurrentTransactionFor(departmentPojo);
		transactionManager.commit(departmentPojo);

        ITransactionPackage packagedXactn = _packager.pack(xactn);
        switchToServerBindings();
        _hibernateTransactionProcessor.process(packagedXactn);
        
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
        try {
			con = _hsqlDatabaseServer.connect();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT Name FROM Department");
			rs.next();
			assertEquals("HR", rs.getString(1));
		} finally {
			rs.close();
			stmt.close();
			con.close();
		}
	}

	public void xtestIdGeneration() throws InterruptedException, JMSException, ClassNotFoundException, SQLException {

        IDomainClass departmentDC = lookupAny(Department.class);
		ITransaction xactn;
        ITransactionPackage packagedXactn;

		// generate first object
		IDomainObject<Department> hrDepartmentDO = clientSession.create(departmentDC);
		Department hrDepartmentPojo = hrDepartmentDO.getPojo();
		hrDepartmentPojo.setName("HR");

		xactn = transactionManager.getCurrentTransactionFor(hrDepartmentPojo);
		transactionManager.commit(hrDepartmentPojo);

        packagedXactn = _packager.pack(xactn);
        switchToServerBindings();
        _hibernateTransactionProcessor.process(packagedXactn);
        
        switchToClientBindings();

		// generate a second object
		IDomainObject<Department> itDepartmentDO = clientSession.create(departmentDC);
		Department itDepartmentPojo = itDepartmentDO.getPojo();
		itDepartmentPojo.setName("IT");

		xactn = transactionManager.getCurrentTransactionFor(itDepartmentPojo);
		transactionManager.commit(itDepartmentPojo);

        packagedXactn = _packager.pack(xactn);
        switchToServerBindings();
        _hibernateTransactionProcessor.process(packagedXactn);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
        try {
			con = _hsqlDatabaseServer.connect();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT id FROM Department order by id");
			rs.next();
			assertEquals(1, rs.getInt("id"));
			rs.next();
			assertEquals(2, rs.getInt("id"));
		} finally {
			rs.close();
			stmt.close();
			con.close();
		}

	}


	public void testRelationships() throws InterruptedException, JMSException, ClassNotFoundException, SQLException {
		
        // create a xactn
        IDomainClass departmentDC = lookupAny(Department.class);
		IDomainObject<Department> departmentDO = clientSession.create(departmentDC);
		Department departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.addEmployee("Joe", "Blow");
		departmentPojo.addEmployee("Mary", "Doe");

		ITransaction xactn = transactionManager.getCurrentTransactionFor(departmentPojo);
		transactionManager.commit(departmentPojo);

        ITransactionPackage packagedXactn = _packager.pack(xactn);
        String xml = new XStreamMarshalling().marshal(packagedXactn);

        switchToServerBindings();

        _hibernateTransactionProcessor.process(packagedXactn);
        
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
        try {
			con = _hsqlDatabaseServer.connect();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT count(*) FROM Employee");
			rs.next();
			assertEquals(2, rs.getInt(1));
		} finally {
			rs.close();
			stmt.close();
			con.close();
		}
	}

	public void xtestHandleIsUpdated() {
		
	}


}
