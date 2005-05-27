package de.berlios.rcpviewer;

import junit.framework.TestCase;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainAnalyzer;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionFactory;
import de.berlios.rcpviewer.session.local.SessionManager;

/**
 *  
 * @author Dan Haywood
 */
public abstract class AbstractTestCase extends TestCase {

	public AbstractTestCase(IDomainSpecifics domainSpecifics, IDomainAnalyzer domainAnalyzer) {
		this.domainSpecifics = domainSpecifics;
		this.domainAnalyzer = domainAnalyzer;
	}
	
	public AbstractTestCase(String name, IDomainSpecifics domainSpecifics, IDomainAnalyzer domainAnalyzer) {
		super(name);
		this.domainSpecifics = domainSpecifics;
		this.domainAnalyzer = domainAnalyzer;
	}

	private final IDomainAnalyzer domainAnalyzer;
	protected IDomainAnalyzer getDomainAnalyzer() {
		return domainAnalyzer;
	}

	private final IDomainSpecifics domainSpecifics; 
	protected IDomain getDomainInstance() {
		return domainSpecifics.getDomainInstance();
	}
	
	protected IDomain getDomainInstance(final String domainName) {
		return domainSpecifics.getDomainInstance(domainName);
	}

	protected <T> IDomainClass<T> lookupAny(Class<T> domainClassIdentifier) {
		return domainSpecifics.lookupAny(domainClassIdentifier);
	}
	
	protected void resetAll() {
		domainSpecifics.resetAll();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		resetAll();
		super.tearDown();
	}

}
