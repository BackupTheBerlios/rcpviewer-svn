package de.berlios.rcpviewer.progmodel.extended;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for de.berlios.rcpviewer.progmodel.extended");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestOptionalAtRuntime.class);
		suite.addTestSuite(TestExtendedClassAdapter.class);
		suite.addTestSuite(TestInvisibleAtRuntime.class);
		suite.addTestSuite(TestOrderForAttributesAtRuntime.class);
		suite.addTestSuite(TestBusinessKeyAtRuntime.class);
		suite.addTestSuite(TestMinLengthOfAtRuntime.class);
		suite.addTestSuite(TestMaxLengthOfAtRuntime.class);
		suite.addTestSuite(TestFieldLengthOfAtRuntime.class);
		suite.addTestSuite(TestMaskAtRuntime.class);
		suite.addTestSuite(TestImmutableOncePersistedAtRuntime.class);
		//$JUnit-END$
		return suite;
	}

}
