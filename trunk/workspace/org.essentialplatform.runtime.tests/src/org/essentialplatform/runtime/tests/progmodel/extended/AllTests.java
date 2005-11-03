package org.essentialplatform.runtime.tests.progmodel.extended;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.essentialplatform.progmodel.extended");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestOptionalAtRuntime.class);
		suite.addTestSuite(TestMaskAtRuntime.class);
		suite.addTestSuite(TestImmutableOncePersistedAtRuntime.class);
		suite.addTestSuite(TestBusinessKeyAtRuntime.class);
		suite.addTestSuite(TestFieldLengthOfAtRuntime.class);
		suite.addTestSuite(TestRegexAtRuntime.class);
		suite.addTestSuite(TestExtendedClassAdapter.class);
		suite.addTestSuite(TestMinLengthOfAtRuntime.class);
		suite.addTestSuite(TestMaxLengthOfAtRuntime.class);
		suite.addTestSuite(TestInvisibleAtRuntime.class);
		suite.addTestSuite(TestRelativeOrderForAttributesAtRuntime.class);
		suite.addTestSuite(TestIdAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
