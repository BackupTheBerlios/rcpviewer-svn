package org.essentialplatform.server.tests.database.hsqldb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.essentialplatform.server.database.hsqldb.HsqlDatabaseServer;

import junit.framework.TestCase;

public class TestHsqlDatabaseServer extends TestCase {

	private HsqlDatabaseServer databaseServer;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		databaseServer = new HsqlDatabaseServer();
		databaseServer.start();
	}

	@Override
	protected void tearDown() throws Exception {
		if (databaseServer != null) {
			databaseServer.shutdown();
		}
		super.tearDown();
	}

	/**
	 * This is redundant really since the shutdown functionality (tested 
	 * elsewhere) does the same thing.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 *
	 */
	public void testCanConnectOnceStarted() throws ClassNotFoundException, SQLException {
		Connection con = databaseServer.connect();
		assertNotNull(con);
		Statement stmt = con.createStatement();
		stmt.executeUpdate("drop table t1 if exists");
		stmt.executeUpdate("create table t1(a int, b int)");
		stmt.executeUpdate("insert into t1 (a, b) values (1, 1)");
		stmt.executeUpdate("insert into t1 (a, b) values (2, 2)");
		ResultSet rs = stmt.executeQuery("select count(*) from t1");
		rs.next();
		int cnt = rs.getInt(1);
		assertEquals(2, cnt);
		stmt.executeUpdate("drop table t1 if exists");
		con.close();
	}

	

}
