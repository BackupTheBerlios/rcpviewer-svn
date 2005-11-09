package org.essentialplatform.core.tests;

import junit.framework.TestCase;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;

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
