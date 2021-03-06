package org.essentialplatform.runtime.shared.tests;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.ProgModelConstants;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;

public abstract class AbstractRuntimeSharedTestCase extends AbstractTestCase {

	/**
	 * Defaults the domain builder to {@link EssentialProgModelRuntimeBuilder}.
	 *
	 */
	public AbstractRuntimeSharedTestCase() {
		this(new EssentialProgModelRuntimeBuilder());
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

	protected abstract IBinding getBinding();



}
