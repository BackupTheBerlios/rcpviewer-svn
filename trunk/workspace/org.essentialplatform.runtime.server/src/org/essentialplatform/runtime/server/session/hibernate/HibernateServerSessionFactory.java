package org.essentialplatform.runtime.server.session.hibernate;

import java.util.Properties;
import org.essentialplatform.runtime.server.session.AbstractServerSessionFactory;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * An implementation of {@link IServerSessionFactory} for Hibernate.
 * 
 * <p>
 * Wraps both a Hibernate <tt>AnnotationConfiguration</tt> and also a 
 * Hibernate <tt>SessionFactory</tt>.  The caller is expected to invoke 
 * {@link #addClass(Class)} for each of the domain classes (eg as discovered
 * by the DomainBuilder) and then open a session using {@link #open()}.  
 * Attempting to add classes after a session has been opened will fail; behind
 * the scenes the first call to {@link #open()} will build the session factory
 * and effectively fix the configuration.  
 * 
 * @author Dan Haywood
 */
public class HibernateServerSessionFactory 
		extends AbstractServerSessionFactory<HibernateServerSession> {

	/**
	 * Properties are initialized in {@link #init()}; classes are mapped in
	 * {@link #addClass(Class)}.
	 */
	private final AnnotationConfiguration _configuration = new AnnotationConfiguration();

	/**
	 * Populated once {@link #open()} has been called.
	 */
	private SessionFactory _sessionFactory;
	

	/**
	 * Sets the properties on the configuration, either taken from the
	 * referenced {@link AbstractServerSessionFactory#getDatabaseServer()} or
	 * directly from {@link #getHibernateProperties()}.
	 */
	@Override
	public SessionBinding init() {
		_configuration.setProperties(getHibernateProperties());
		_configuration.setProperty("hibernate.connection.url", getDatabaseServer().getUrl());
		_configuration.setProperty("hibernate.connection.username", getDatabaseServer().getUser());
		_configuration.setProperty("hibernate.connection.password", getDatabaseServer().getPassword());
		_configuration.setProperty("hibernate.connection.driver_class", getDatabaseServer().getDriverClassName());
		SessionBinding sessionBinding = super.init();
		return sessionBinding;
	}
	
	/**
	 * Whether a session has been opened by this factory, meaning that no
	 * further classes may be defined to it.
	 *  
	 * @return
	 */
	public boolean isFixed() {
		return _sessionFactory != null;
	}
	
	/**
	 * Define classes; should be annotated with EJB3 and optionally Hibernate
	 * annotations.
	 * 
	 * <p>
	 * This method may NOT be called once any session has been opened (using
	 * {@link #open()}) - attempting to do so will throw an exception.
	 * 
	 * @param <V>
	 * @param javaClass
	 * @throws IllegalStateException - if {@link #isFixed()} returns true.
	 */
	public <V> void addClass(Class<V> javaClass) {
		if (isFixed()) {
			throw new IllegalStateException("Cannot add classes since now fixed.");
		}
		_configuration.addAnnotatedClass(javaClass);
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSessionFactory#open()
	 */
	public HibernateServerSession open() {
		if (_sessionFactory == null) {
			// will effectively move this object into a "fixed" state.
			_sessionFactory = _configuration.buildSessionFactory();
		}
		Session session = _sessionFactory.openSession();
		return new HibernateServerSession(getSessionBinding(), session);
	}

	/*
	 * Does nothing.
	 * 
	 * @see org.essentialplatform.runtime.shared.session.IObjectStoreRef#reset()
	 */
	public void reset() {
		// does nothing.
	}


	////////////////////////////////////////////////////////////////////
	// HibernateProperties
	////////////////////////////////////////////////////////////////////

	
	private Properties _hibernateProperties;
	public Properties getHibernateProperties() {
		return _hibernateProperties;
	}
	public void setHibernateProperties(Properties hibernateProperties) {
		_hibernateProperties = hibernateProperties;
	}
	
}
