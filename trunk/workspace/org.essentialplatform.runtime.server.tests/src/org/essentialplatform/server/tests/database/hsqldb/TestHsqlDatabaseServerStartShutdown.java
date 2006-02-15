package org.essentialplatform.server.tests.database.hsqldb;

import org.essentialplatform.runtime.server.database.hsqldb.HsqlDatabaseServer;

import junit.framework.TestCase;

public class TestHsqlDatabaseServerStartShutdown extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testStartAndShutdown() {
		HsqlDatabaseServer databaseServer;
		databaseServer = new HsqlDatabaseServer(9001, "foobar", false);
		databaseServer.start();
		databaseServer.shutdown();
	}

	public void testGetDriverClassName() {
		HsqlDatabaseServer databaseServer;
		databaseServer = new HsqlDatabaseServer(9001, "foobar", false);
		assertEquals("org.hsqldb.jdbcDriver", databaseServer.getDriverClassName());
	}

	public void testGetUrl() {
		HsqlDatabaseServer databaseServer;
		databaseServer = new HsqlDatabaseServer(9001, "foobar", false);
		assertEquals("jdbc:hsqldb:hsql://localhost:9001/foobar", databaseServer.getUrl());
	}

	public void testGetUser() {
		HsqlDatabaseServer databaseServer;
		databaseServer = new HsqlDatabaseServer(9001, "foobar", false);
		assertEquals("sa", databaseServer.getUser());
	}

	public void testGetPassword() {
		HsqlDatabaseServer databaseServer;
		databaseServer = new HsqlDatabaseServer(9001, "foobar", false);
		assertNull(databaseServer.getPassword());
	}

	public void testToString() {
		HsqlDatabaseServer databaseServer;
		databaseServer = new HsqlDatabaseServer(9001, "foobar", false);
		assertEquals("jdbc:hsqldb:hsql://localhost:9001/foobar", databaseServer.toString());
	}
	

}
