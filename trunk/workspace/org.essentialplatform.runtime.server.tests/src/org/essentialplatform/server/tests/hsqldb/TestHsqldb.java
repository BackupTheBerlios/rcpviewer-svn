package org.essentialplatform.server.tests.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.Hsqldb;

import junit.framework.TestCase;

public class TestHsqldb extends TestCase {

	public void testLoadDriver() throws ClassNotFoundException {
		Class.forName(Hsqldb.JDBC_DRIVER_CLASSNAME);
	}

	public void testConnectToInMemoryDatabase() throws ClassNotFoundException, SQLException {
		Class.forName(Hsqldb.JDBC_DRIVER_CLASSNAME);
		Connection conn = DriverManager.getConnection(Hsqldb.URL_PREFIX_IN_MEMORY + "foobar", "sa", "");
		conn.close();
	}

	public void testCreateTableAndPopulate() throws ClassNotFoundException, SQLException {
		Class.forName(Hsqldb.JDBC_DRIVER_CLASSNAME);
		Connection conn = DriverManager.getConnection(Hsqldb.URL_PREFIX_IN_MEMORY + "foobar", "sa", "");
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("create table t1(a int)");
		stmt.executeUpdate("insert into t1(a) values(1)");
		stmt.executeUpdate("insert into t1(a) values(2)");
		stmt.executeUpdate("insert into t1(a) values(3)");
		ResultSet rs = stmt.executeQuery("select count(*) from t1");
		rs.next();
		assertEquals(3, rs.getInt(1));
		rs.close();
		stmt.close();
		conn.close();
	}

	/**
	 * If don't close down normally, HSQLDB will leave a .log and .lck file
	 * hanging around - cannot connect within some time period (heartbeat/check 
	 * that no other process is using the DB).  If a SHUTDOWN is performed, then
	 * these files will be removed (clean shutdown).
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void testConnectToFileBasedDatabaseAndThenCloseDown() throws ClassNotFoundException, SQLException {
		Class.forName(Hsqldb.JDBC_DRIVER_CLASSNAME);
		Connection conn = DriverManager.getConnection(Hsqldb.URL_PREFIX_FILE + "/tmp/hsqldata/foobar", "sa", "");
		conn.createStatement().execute("SHUTDOWN");
		conn.close();
	}



}
