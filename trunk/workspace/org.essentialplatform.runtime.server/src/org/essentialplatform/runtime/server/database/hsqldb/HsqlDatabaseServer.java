package org.essentialplatform.runtime.server.database.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.server.AbstractServer;
import org.essentialplatform.runtime.server.database.IDatabaseServer;
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
public class HsqlDatabaseServer extends AbstractServer implements IDatabaseServer {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(HsqlDatabaseServer.class);
	}

	/**
	 * Ensures that the HSQL properties file exists and contains the correct
	 * property settings, and populates the getters.
	 * 
	 * <p>
	 * The properties file is called <tt>server.properties</tt>.
	 */
	public HsqlDatabaseServer() {
		this(DEFAULT_PORT, DEFAULT_DATABASE_NAME, DEFAULT_SILENT_MODE);
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
		setPort(port);
		init(database);
		setSilent(silent);
	}

	////////////////////////////////////////////////////////////////////
	// DriverClassName
	// (constant)
	////////////////////////////////////////////////////////////////////

	public final static String JDBC_DRIVER_CLASSNAME = "org.hsqldb.jdbcDriver";

	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getDriverClassName()
	 */
	public String getDriverClassName() {
		return JDBC_DRIVER_CLASSNAME;
	}

	////////////////////////////////////////////////////////////////////
	// Url
	// (derived)
	////////////////////////////////////////////////////////////////////

	public final static String URL_PREFIX_LOCALHOST = "jdbc:hsqldb:hsql://localhost:";

	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getUrl()
	 */
	public String getUrl() {
		return URL_PREFIX_LOCALHOST + _port;
	}

	////////////////////////////////////////////////////////////////////
	// User (dependency injected)
	////////////////////////////////////////////////////////////////////


	public static final String DEFAULT_USER = "sa";

	private String _user = DEFAULT_USER;
	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getUser()
	 */
	public String getUser() {
		return DEFAULT_USER;
	}
	/**
	 * For dependency injection.
	 * 
	 * Optional; defaults to {@link #DEFAULT_USER}.
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		_user = user;
	}

	////////////////////////////////////////////////////////////////////
	// Port (dependency injected)
	////////////////////////////////////////////////////////////////////

	public static final int DEFAULT_PORT = 9001;

	private int _port = DEFAULT_PORT;
	/**
	 * The port number on which the database server is listening.
	 * 
	 * <p>
	 * A constituent part of the url on which to connect,
	 * 
	 * @see #getUrl()
	 * @see #connect()
	 * @return
	 */
	public int getPort() {
		return _port;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; defaults to {@link #DEFAULT_PORT}.  Updates the
	 * HSQLDB properties to change the port that HSQLDB listens on when it is 
	 * started.
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		_port = port;
		updateHsqldbProperties();
	}
	
	////////////////////////////////////////////////////////////////////
	// DatabaseName (dependency injected/directed configuration)
	////////////////////////////////////////////////////////////////////

	public static final String DEFAULT_DATABASE_NAME = "essential";

	private String _databaseName = DEFAULT_DATABASE_NAME;
	public String getDatabaseName() {
		return _databaseName;
	}
	/**
	 * For dependency injection, or direction configuration.
	 * 
	 * <p>
	 * Optional; defaults to {@link #DEFAULT_DATABASE_NAME}.  Updates the
	 * HSQLDB properties to change the database name that HSQLDB will make 
	 * available when it is started.
	 * 
	 * @param databaseName
	 */
	public void init(String databaseName) {
		_databaseName = databaseName;
		updateHsqldbProperties();
	}

	////////////////////////////////////////////////////////////////////
	// Password (dependency injected)
	////////////////////////////////////////////////////////////////////

	private String _password = null;
	/*
	 * @see org.essentialplatform.server.database.IDatabaseServer#getPassword()
	 */
	public String getPassword() {
		return _password;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; defaults to <tt>null</tt>.
	 * @param password
	 */
	public void setPassword(String password) {
		_password = password;
	}

	
	////////////////////////////////////////////////////////////////////
	// Silent mode (dependency injected)
	////////////////////////////////////////////////////////////////////

	public static final boolean DEFAULT_SILENT_MODE = false;

	private boolean _silent = DEFAULT_SILENT_MODE;
	/**
	 * Whether the HSQL <tt>silent</tt> property should be set.
	 * 
	 * @return
	 */
	public boolean isSilent() {
		return _silent;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; default mode is {@link #DEFAULT_SILENT_MODE}.  Updates the
	 * HSQLDB properties to alter behaviour of HSQLDB when it is started. 
	 * 
	 * @param silent
	 */
	public void setSilent(boolean silent) {
		_silent = silent;
		updateHsqldbProperties();
	}

	////////////////////////////////////////////////////////////////////
	// HSQLDB Startup Properties
	////////////////////////////////////////////////////////////////////

	private void updateHsqldbProperties() {
		_properties = overridingProperties(getPort(), getDatabaseName(), isSilent(), true); 
	}

	/**
	 * Holds the properties used to start the HSQLDB server.
	 * 
	 * <p>
	 * Based on the properties {@link #getPort()}, {@link #getDatabaseName()} 
	 * and {@link #isSilent()}, and is updated whenever these properties are
	 * updated.
	 */
	private Properties _properties;


	private static Properties overridingProperties(int port, String database, boolean silent, boolean noSystemExitOnShutdown) {
		Properties props = new Properties();
		props.put(ServerConstants.SC_KEY_PORT, ""+port);
		props.put(ServerConstants.SC_KEY_DATABASE+".0", database);
		props.put(ServerConstants.SC_KEY_SILENT, ""+silent);
		props.put(ServerConstants.SC_KEY_NO_SYSTEM_EXIT, ""+noSystemExitOnShutdown);
		return props;
	}

	
	////////////////////////////////////////////////////////////////////
	// Lifecycle methods
	////////////////////////////////////////////////////////////////////

	@Override
	protected boolean doStart() {
		String[] args = propertiesAsMainArgs();
		Server.main(args);
		return true;
	}

	@Override
	protected boolean doShutdown() {
		Connection conn = null;
		try {
			conn = connect();
			conn.createStatement().executeUpdate("SHUTDOWN");
			return true;
		} catch (ClassNotFoundException ex) {
			return false;
		} catch (SQLException ex) {
			return false;
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

	////////////////////////////////////////////////////////////////////
	// toString
	////////////////////////////////////////////////////////////////////

	@Override
	public String toString() {
		return getUrl();
	}

}


