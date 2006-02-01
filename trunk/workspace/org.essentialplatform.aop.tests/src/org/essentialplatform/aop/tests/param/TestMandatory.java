package org.essentialplatform.aop.tests.param;

import junit.framework.TestCase;

public class TestMandatory extends TestCase {

	private MandatoryFixture _fixture;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_fixture = new MandatoryFixture();
	}
	
	@Override
	protected void tearDown() throws Exception {
		_fixture = null;
		super.tearDown();
	}
	
	public void testWithPreconditionsAndMandatoryParam0WhenSupplied() {
		_fixture.withPreconditionsAndMandatoryParam0("supplied");
		new MandatoryFixture("supplied");
	}

	public void testWithPreconditionsAndMandatoryParam0WhenNotSupplied() {
		try {
			_fixture.withPreconditionsAndMandatoryParam0(null);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
		try {
			new MandatoryFixture(null);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testWithPreconditionsButNonMandatoryParam0WhenNotSupplied() {
		_fixture.withPreconditionsButNonMandatoryParam0(null);
		// should not throw exception, because we haven't added the @Mandatory annotation
	}

	public void testWithoutPreconditionsButWithMandatoryParam0WhenNotSupplied() {
		_fixture.withoutPreconditionsButWithMandatoryParam0(null);
		// should not throw exception, because we forgot to add the @WithPreconditions annotation
	}

	/////////////////
	
	public void testWithPreconditionsAndMandatoryParam1WhenSupplied() {
		_fixture.withPreconditionsAndMandatoryParam1(null, "supplied");
		new MandatoryFixture(null, "supplied");
	}

	public void testWithPreconditionsAndMandatoryParam1WhenNotSupplied() {
		try {
			_fixture.withPreconditionsAndMandatoryParam1("foo", null);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
		try {
			new MandatoryFixture("foo", null);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testWithPreconditionsButNonMandatoryParam1WhenNotSupplied() {
		_fixture.withPreconditionsButNonMandatoryParam1("foo", null);
		// should not throw exception, because we haven't added the @Mandatory annotation
	}

	public void testWithoutPreconditionsButWithMandatoryParam1WhenNotSupplied() {
		_fixture.withoutPreconditionsButWithMandatoryParam1("foo", null);
		// should not throw exception, because we forgot to add the @WithPreconditions annotation
	}

	/////////////////

	public void testWithPreconditionsButNonMandatoryParamWhenNotSupplied() {
		new MandatoryFixture(null, null, null);
		// should not throw exception, because we haven't added the @Mandatory annotation
	}

	public void testWithoutPreconditionsButWithMandatoryParamWhenNotSupplied() {
		new MandatoryFixture(null, null, null, null);
		// should not throw exception, because we forgot to add the @WithPreconditions annotation
	}


	/////////////////
	
	public void testWithPreconditionsAndMandatoryParam7WhenSupplied() {
		_fixture.withPreconditionsAndMandatoryParam7(null, null, null, null, null, null, null, "supplied");
	}

	public void testWithPreconditionsAndMandatoryParam7WhenNotSupplied() {
		try {
			_fixture.withPreconditionsAndMandatoryParam7("foo", "foo", "foo", "foo", "foo", "foo", "foo", null);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testWithPreconditionsButNonMandatoryParam7WhenNotSupplied() {
		_fixture.withPreconditionsButNonMandatoryParam7("foo", "foo", "foo", "foo", "foo", "foo", "foo", null);
		// should not throw exception, because we haven't added the @Mandatory annotation
	}

	public void testWithoutPreconditionsButWithMandatoryParam7WhenNotSupplied() {
		_fixture.withoutPreconditionsButWithMandatoryParam7("foo", "foo", "foo", "foo", "foo", "foo", "foo", null);
		// should not throw exception, because we forgot to add the @WithPreconditions annotation
	}

	/////////////////
	
	public void testWithPreconditionsAndMandatoryParam8() {
		try {
			_fixture.withPreconditionsAndMandatoryParam8("foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "supplied");
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	
	
}
