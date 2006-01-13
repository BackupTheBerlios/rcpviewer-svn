package org.essentialplatform.runtime.shared.tests.persistence;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdSecond;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleStringId;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.handle.SequentialHandleAssigner;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestGuidHandleAssigner extends AbstractRuntimeClientTestCase {

	private SequentialHandleAssigner assigner;

	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	private Handle persistenceId;
	private Object[] componentValues;
	
	public TestGuidHandleAssigner() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		assigner = new SequentialHandleAssigner();
	}

	@Override
	public void tearDown() throws Exception {
		assigner = null;
		domainClass = null;
		dobj = null;
		persistenceId = null;
		componentValues = null;
		super.tearDown();
	}

	public void testDummy() {}
}
