package org.essentialplatform;

import junit.framework.TestCase;

import org.essentialplatform.domain.Deployment;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomain;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.runtime.RuntimeDeployment;

/**
 *  
 * @author Dan Haywood
 */
public abstract class AbstractTestCase extends TestCase {

	public AbstractTestCase() {
		this(null);
	}
	
	public AbstractTestCase(IDomainBuilder domainBuilder) {
		this.domainBuilder = domainBuilder;
	}
	
	public AbstractTestCase(String name, IDomainBuilder domainBuilder) {
		super(name);
		this.domainBuilder = domainBuilder != null? domainBuilder: IDomainBuilder.NOOP;
	}

	private final IDomainBuilder domainBuilder;
	protected IDomainBuilder getDomainBuilder() {
		return domainBuilder;
	}

	protected IDomain getDomainInstance() {
		return Domain.instance();
	}
	
	protected IDomain getDomainInstance(final String domainName) {
		return Domain.instance(domainName);
	}

	protected <T> IDomainClass lookupAny(Class<T> domainClassIdentifier) {
		return Domain.lookupAny(domainClassIdentifier);
	}
	
	protected void resetAll() {
		Domain.resetAll();
		Deployment.reset();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		resetAll();
		super.tearDown();
	}

}
