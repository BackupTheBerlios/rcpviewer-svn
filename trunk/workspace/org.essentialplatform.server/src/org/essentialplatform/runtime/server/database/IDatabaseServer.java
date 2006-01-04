package org.essentialplatform.runtime.server.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.essentialplatform.runtime.server.IService;

public interface IDatabaseServer extends IService {

	
	public String getDriverClassName();
	public String getUrl();
	public String getUser();
	public String getPassword();

	/**
	 * Convenience function, eg where connection pooling is not in use.
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Connection connect() throws IllegalStateException, ClassNotFoundException, SQLException;
	
}
