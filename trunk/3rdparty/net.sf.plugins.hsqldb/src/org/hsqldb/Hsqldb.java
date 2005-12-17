package org.hsqldb;

public final class Hsqldb {
	private Hsqldb() {}
	
	public final static String JDBC_DRIVER_CLASSNAME = "org.hsqldb.jdbcDriver";
	
	public final static String URL_PREFIX_IN_MEMORY = "jdbc:hsqldb:mem:";

	public final static String URL_PREFIX_FILE = "jdbc:hsqldb:file:";

}
