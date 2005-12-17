//$Id: TestCase.java,v 1.5 2005/11/15 00:06:22 epbernard Exp $
package org.hibernate.test.annotations;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;

public abstract class TestCase extends junit.framework.TestCase {

	private static SessionFactory sessions;
	private static AnnotationConfiguration cfg;
	private static Dialect dialect;
	private static Class lastTestClass;
	private Session session;

	public TestCase() {
		super();
	}

	public TestCase(String x) {
		super(x);
	}

	private void buildSessionFactory(Class[] classes, String[] packages) throws Exception {

		if ( getSessions()!=null ) getSessions().close();
		try {
			setCfg( new AnnotationConfiguration() );
			configure(cfg);
			if ( recreateSchema() ) {
				cfg.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
			}
			for (int i=0; i<packages.length; i++) {
				getCfg().addPackage( packages[i] );
			}
			for (int i=0; i<classes.length; i++) {
				getCfg().addAnnotatedClass( classes[i] );
			}
			setDialect( Dialect.getDialect() );
			setSessions( getCfg().buildSessionFactory( /*new TestInterceptor()*/ ) );
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	protected void setUp() throws Exception {
		if ( getSessions()==null || lastTestClass!=getClass() ) {
			buildSessionFactory( getMappings(), getAnnotatedPackages() );
			lastTestClass = getClass();
		}
	}

	protected void runTest() throws Throwable {
		try {
			super.runTest();
			if ( session!=null && session.isOpen() ) {
				if ( session.isConnected() ) session.connection().rollback();
				session.close();
				session = null;
				fail("unclosed session");
			}
			else {
				session=null;
			}
		}
		catch (Throwable e) {
			try {
				if ( session!=null && session.isOpen() ) {
					if ( session.isConnected() ) session.connection().rollback();
					session.close();
				}
			}
			catch (Exception ignore) {}
			try {
				if (sessions!=null) {
					sessions.close();
					sessions=null;
				}
			}
			catch (Exception ignore) {}
			throw e;
		}
	}

	public Session openSession() throws HibernateException {
		session = getSessions().openSession();
		return session;
	}

	protected abstract Class[] getMappings();
	
	protected String[] getAnnotatedPackages() {
		return new String[] {};
	}

	private void setSessions(SessionFactory sessions) {
		TestCase.sessions = sessions;
	}

	protected SessionFactory getSessions() {
		return sessions;
	}

	private void setDialect(Dialect dialect) {
		TestCase.dialect = dialect;
	}

	protected Dialect getDialect() {
		return dialect;
	}

	protected static void setCfg(AnnotationConfiguration cfg) {
		TestCase.cfg = cfg;
	}

	protected static AnnotationConfiguration getCfg() {
		return cfg;
	}

	protected void configure(Configuration cfg) {
		//cfg.setNamingStrategy( AlternativeNamingStrategy.INSTANCE );
		//cfg.getSessionEventListenerConfig().setFlushEventListener( new EJB3FlushEventListener() );
		//cfg.getSessionEventListenerConfig().setAutoFlushEventListener( new EJB3AutoFlushEventListener() );
	}

	protected boolean recreateSchema() {
		return true;
	}

}
