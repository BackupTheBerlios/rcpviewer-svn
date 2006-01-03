package org.essentialplatform.runtime.shared.tests;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.ProgModelConstants;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.RuntimeClientBinding;

public abstract class AbstractRuntimeSharedTestCase extends AbstractTestCase {

	public AbstractRuntimeSharedTestCase() {
		super(null);
	}

	public AbstractRuntimeSharedTestCase(IDomainBuilder domainBuilder) {
		super(domainBuilder);
	}

	public AbstractRuntimeSharedTestCase(String name, IDomainBuilder domainBuilder) {
		super(name, domainBuilder);
	}

	protected Domain domain;
	

	protected void setUp() throws Exception {
		super.setUp();
		Binding.setBinding(getBinding());
		domain = Domain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
	}

	protected void tearDown() throws Exception {
		domain = null;
		Domain.resetAll();
		super.tearDown();
	}

	protected abstract Binding getBinding();



}
