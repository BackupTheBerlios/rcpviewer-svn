package org.essentialplatform.runtime.server.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.server.database.IDatabaseServer;
import org.essentialplatform.runtime.shared.RuntimePlugin;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.essentialplatform.runtime.shared.domain.SingleDomainRegistry;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.hibernate.cfg.AnnotationConfiguration;

public abstract class AbstractServerSessionFactory<V extends IServerSession> implements IServerSessionFactory<V> {


	/*
	 * @see org.essentialplatform.runtime.server.session.IServerSessionFactory#init()
	 */
	public SessionBinding init() {
		String domainName = getDomainDefinition().getName();
		String objectStoreName = getDatabaseServer().getDatabaseName();

		_sessionBinding = new SessionBinding(domainName, objectStoreName);
		
		for(IDomainClass dc: Domain.instance(domainName).classes()) {
			IDomainClassRuntimeBinding dcrb = (IDomainClassRuntimeBinding)dc.getBinding();
			addClass(dcrb.getJavaClass());
		}

		return _sessionBinding;
	}
	
	private SessionBinding _sessionBinding;
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}
	
	
	/////////////////////////////////////////
	// TODO: make obsolete... 
	
	/**
	 * TODO: to go, if rename IObjectStoreRef to ISessionBindingProvider
	 */
	public String getObjectStoreId() {
		return _sessionBinding.getObjectStoreId();
	}
	
	/**
	 * TODO: to get rid of, in favour of session binding.
	 * 
	 * @param objectStoreId
	 */
	public void setObjectStoreId(String objectStoreId) {
		throw new IllegalArgumentException("use setSessionBinding instead");
	}

	/////////////////////////////////////////

	
	////////////////////////////////////////////////////////////////////
	// DomainDefinition
	////////////////////////////////////////////////////////////////////

	private IDomainDefinition _domainDefinition;
	public IDomainDefinition getDomainDefinition() {
		return _domainDefinition;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param domainDefinition
	 */
	public void setDomainDefinition(IDomainDefinition domainDefinition) {
		_domainDefinition = domainDefinition;
	}
	
	////////////////////////////////////////////////////////////////////
	// DatabaseServer (injected)
	////////////////////////////////////////////////////////////////////
	
	private IDatabaseServer _databaseServer;
	public IDatabaseServer getDatabaseServer() {
		return _databaseServer;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * The <tt>hibernate.connection.*</tt> properties are obtained directly
	 * from the databaseServer. 
	 * 
	 * @param databaseServer
	 */
	public void setDatabaseServer(IDatabaseServer databaseServer) {
		_databaseServer = databaseServer;
	}



	
}
