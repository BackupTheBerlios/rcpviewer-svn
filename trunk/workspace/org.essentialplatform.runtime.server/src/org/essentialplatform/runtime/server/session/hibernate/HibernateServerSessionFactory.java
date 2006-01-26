package org.essentialplatform.runtime.server.session.hibernate;

import org.essentialplatform.runtime.server.session.AbstractServerSessionFactory;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
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

	private AnnotationConfiguration _cfg;
	private SessionFactory _sessionFactory;
	
	/**
	 * 
	 * @param sessionBinding - available as {@link #getSessionBinding}.
	 */
	public HibernateServerSessionFactory(final SessionBinding sessionBinding) {
		super(sessionBinding);
		_cfg = new AnnotationConfiguration();
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
		_cfg.addAnnotatedClass(javaClass);
	}

	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSessionFactory#open()
	 */
	public HibernateServerSession open() {
		if (_sessionFactory == null) {
			// will effectively move this object into a "fixed" state.
			_sessionFactory = _cfg.buildSessionFactory();
		}
		Session session = _sessionFactory.openSession();
		return new HibernateServerSession(getSessionBinding(), session);
	}

	/*
	 * Does nothing.
	 * 
	 * @see org.essentialplatform.runtime.shared.session.IObjectStoreHandle#reset()
	 */
	public void reset() {
		// does nothing.
	}

}
