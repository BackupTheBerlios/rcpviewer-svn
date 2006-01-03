package org.essentialplatform.server.tests.hsqldb;

import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;

public abstract class AbstractEssentialAndHibTestCase extends AbstractRuntimeClientTestCase {

	private static SessionFactory hibSessionFactory;
	private static AnnotationConfiguration hibConfiguration;
	private static Dialect dialect;
	private static Class lastTestClass;
	private Session hibSession;

	public AbstractEssentialAndHibTestCase() {
		super();
	}

	private void buildSessionFactory(Class[] classes, String[] packages) throws Exception {

		if ( getHibSessionFactory()!=null ) getHibSessionFactory().close();
		try {
			setHibConfiguration( new AnnotationConfiguration() );
			configure(hibConfiguration);
			if ( recreateSchema() ) {
				hibConfiguration.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
			}
			for (int i=0; i<packages.length; i++) {
				getHibConfiguration().addPackage( packages[i] );
			}
			for (int i=0; i<classes.length; i++) {
				getHibConfiguration().addAnnotatedClass( classes[i] );
			}
			setDialect( Dialect.getDialect() );
			setHibSessionFactory( getHibConfiguration().buildSessionFactory( /*new TestInterceptor()*/ ) );
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	protected void setUp() throws Exception {
		super.setUp();
		if ( getHibSessionFactory()==null || lastTestClass!=getClass() ) {
			buildSessionFactory( getMappings(), getAnnotatedPackages() );
			lastTestClass = getClass();
		}
	}

	protected void runTest() throws Throwable {
		try {
			super.runTest();
			if ( hibSession!=null && hibSession.isOpen() ) {
				if ( hibSession.isConnected() ) hibSession.connection().rollback();
				hibSession.close();
				hibSession = null;
				fail("unclosed session");
			}
			else {
				hibSession=null;
			}
		}
		catch (Throwable e) {
			try {
				if ( hibSession!=null && hibSession.isOpen() ) {
					if ( hibSession.isConnected() ) hibSession.connection().rollback();
					hibSession.close();
				}
			}
			catch (Exception ignore) {}
			try {
				if (hibSessionFactory!=null) {
					hibSessionFactory.close();
					hibSessionFactory=null;
				}
			}
			catch (Exception ignore) {}
			throw e;
		}
	}

	public Session openSession() throws HibernateException {
		hibSession = getHibSessionFactory().openSession();
		return hibSession;
	}

	protected abstract Class[] getMappings();
	
	protected String[] getAnnotatedPackages() {
		return new String[] {};
	}

	private void setHibSessionFactory(SessionFactory sessions) {
		AbstractEssentialAndHibTestCase.hibSessionFactory = sessions;
	}

	protected SessionFactory getHibSessionFactory() {
		return hibSessionFactory;
	}

	private void setDialect(Dialect dialect) {
		AbstractEssentialAndHibTestCase.dialect = dialect;
	}

	protected Dialect getDialect() {
		return dialect;
	}

	protected static void setHibConfiguration(AnnotationConfiguration cfg) {
		AbstractEssentialAndHibTestCase.hibConfiguration = cfg;
	}

	protected static AnnotationConfiguration getHibConfiguration() {
		return hibConfiguration;
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
