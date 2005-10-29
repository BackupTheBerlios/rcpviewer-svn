package de.berlios.rcpviewer;

import junit.framework.TestCase;

import de.berlios.rcpviewer.domain.Deployment;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment;

/**
 *  
 * @author Dan Haywood
 */
public abstract class AbstractTestCase extends TestCase {

	public AbstractTestCase(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		this.domainSpecifics = domainSpecifics;
		this.domainBuilder = domainBuilder;
	}
	
	public AbstractTestCase(String name, IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(name);
		this.domainSpecifics = domainSpecifics;
		this.domainBuilder = domainBuilder != null? domainBuilder: IDomainBuilder.NOOP;
	}

	private final IDomainBuilder domainBuilder;
	protected IDomainBuilder getDomainBuilder() {
		return domainBuilder;
	}

	private final IDeploymentSpecifics domainSpecifics; 
	protected IDomain getDomainInstance() {
		return domainSpecifics.getDomainInstance();
	}
	
	protected IDomain getDomainInstance(final String domainName) {
		return domainSpecifics.getDomainInstance(domainName);
	}

	protected <T> IDomainClass lookupAny(Class<T> domainClassIdentifier) {
		return domainSpecifics.lookupAny(domainClassIdentifier);
	}
	
	protected void resetAll() {
		domainSpecifics.resetAll();
		Deployment.reset();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		new RuntimeDeployment();
	}

	protected void tearDown() throws Exception {
		resetAll();
		super.tearDown();
	}

}
