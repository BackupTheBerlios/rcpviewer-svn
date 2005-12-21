package org.essentialplatform.server.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseServer {

	
	public String getDriverClassName();
	public String getUrl();
	public String getUser();
	public String getPassword();

	public boolean isStarted();
	
	public void start()  throws IllegalStateException;
	public void shutdown() throws IllegalStateException;
	
	/**
	 * Convenience function, eg where connection pooling is not in use.
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Connection connect() throws IllegalStateException, ClassNotFoundException, SQLException;
	
}
