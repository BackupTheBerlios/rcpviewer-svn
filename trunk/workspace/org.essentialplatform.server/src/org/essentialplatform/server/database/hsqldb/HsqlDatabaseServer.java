package org.essentialplatform.server.database.hsqldb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.essentialplatform.server.database.IDatabaseServer;
import org.hsqldb.Server;
import org.hsqldb.ServerConstants;

/**
 * Properties are picked up from <tt>server.properties</tt> (relative to the
 * working directory).
 * 
 * <p>
 * Key properties are:
 * <ul>
 * <li><tt>server.port</tt>
 * <li><tt>server.database.0</tt>
 * <li><tt>server.silent</tt>
 * </li>
 * </ul>
 * 
 * <p>
 * These can be overridden using the appropriate constructor if required.
 * 
 * @author Dan Haywood
 */
public class HsqlDatabaseServer implements IDatabaseServer {

	public final static String JDBC_DRIVER_CLASSNAME = "org.hsqldb.jdbcDriver";
	public final static String URL_PREFIX_LOCALHOST = "jdbc:hsqldb:hsql://localhost:";

	private Properties _properties;
	private int _port;
	private boolean _started;

	/**
	 * Ensures that the HSQL properties file exists and contains the correct
	 * property settings, and populates the getters.
	 * 
	 * <p>
	 * The properties file is called <tt>server.properties</tt>.
	 */
	public HsqlDatabaseServer() {
		this(9001, "essential", true);
	}

	/**
	 * Ensures that the HSQL properties file exists and contains the correct
	 * property settings, and populates the getters.
	 * 
	 * <p>
	 * The properties file is called <tt>server.properties</tt>, but can be
	 * overridden by the specific properties.
	 */
	public HsqlDatabaseServer(int port, String database, boolean silent) {
		this(overridingProperties(port, database, silent, true));
		_port = port;
	}

	/**
	 * Ensures that the HSQL properties file exists and contains the correct
	 * property settings, and populates the getters.
	 * 
	 * <p>
	 * The properties file is called <tt>server.properties</tt>, but can be
	 * overridden by the supplied properties.
	 */
	private HsqlDatabaseServer(final Properties properties) {
		_properties = properties;
	}

	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getDriverClassName()
	 */
	public String getDriverClassName() {
		return JDBC_DRIVER_CLASSNAME;
	}

	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getUrl()
	 */
	public String getUrl() {
		return URL_PREFIX_LOCALHOST + _port;
	}

	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getLogin()
	 */
	public String getUser() {
		return "sa";
	}

	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getPassword()
	 */
	public String getPassword() {
		return null;
	}

	public void shutdown() {
		assertStarted();
		Connection conn = null;
		try {
			conn = connect();
			conn.createStatement().executeUpdate("SHUTDOWN");
			_started = false;
		} catch (ClassNotFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} finally {
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// do nothing
				}
			}
		}
	}


	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#connect()
	 */
	public Connection connect() throws ClassNotFoundException, SQLException {
		assertStarted();
		Class.forName(getDriverClassName());
		return DriverManager.getConnection(getUrl(), getUser(), getPassword());
	}


	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#start()
	 */
	public void start() {
		assertNotStarted();
		String[] args = propertiesAsMainArgs();
		Server.main(args);
		_started = true;
	}

	@Override
	public String toString() {
		return getUrl();
	}
	
	private String[] propertiesAsMainArgs() {
		String[] args = new String[_properties.keySet().size()*2];
		int i=0;
		for(Object key: _properties.keySet()) {
			Object value = _properties.get(key);
			args[i++] = "-"+key.toString().substring(7);
			args[i++] = ""+value;
		}
		return args;
	}

	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#isStarted()
	 */
	public boolean isStarted() {
		return _started;
	}

	private void assertStarted() {
		if (!isStarted()) {
			throw new IllegalStateException("Not started");
		}
	}
	private void assertNotStarted() {
		if (isStarted()) {
			throw new IllegalStateException("Already started");
		}
	}
	private static Properties overridingProperties(int port, String database, boolean silent, boolean noSystemExitOnShutdown) {
		Properties props = new Properties();
		props.put(ServerConstants.SC_KEY_PORT, ""+port);
		props.put(ServerConstants.SC_KEY_DATABASE+".0", database);
		props.put(ServerConstants.SC_KEY_SILENT, ""+silent);
		props.put(ServerConstants.SC_KEY_NO_SYSTEM_EXIT, ""+noSystemExitOnShutdown);
		return props;
	}

}


