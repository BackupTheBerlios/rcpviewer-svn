package de.berlios.rcpviewer;

import junit.framework.TestCase;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 *  
 * @author Dan Haywood
 */
public abstract class AbstractTestCase extends TestCase {

	public AbstractTestCase(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		this.domainSpecifics = domainSpecifics;
		this.domainAnalyzer = domainAnalyzer;
	}
	
	public AbstractTestCase(String name, IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(name);
		this.domainSpecifics = domainSpecifics;
		this.domainAnalyzer = domainAnalyzer;
	}

	private final IDomainBuilder domainAnalyzer;
	protected IDomainBuilder getDomainAnalyzer() {
		return domainAnalyzer;
	}

	private final IDeploymentSpecifics domainSpecifics; 
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
