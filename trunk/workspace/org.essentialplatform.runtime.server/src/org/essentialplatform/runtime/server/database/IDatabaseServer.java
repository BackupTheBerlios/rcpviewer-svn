package org.essentialplatform.runtime.server.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.essentialplatform.runtime.server.IServer;

public interface IDatabaseServer extends IServer {

	
	public String getDriverClassName();
	public String getUrl();
	public String getUser();
	public String getPassword();

	public void setDatabaseName(String store);
	public String getDatabaseName();

	/**
	 * Convenience function, eg where connection pooling is not in use.
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Connection connect() throws IllegalStateException, ClassNotFoundException, SQLException;
	
}
