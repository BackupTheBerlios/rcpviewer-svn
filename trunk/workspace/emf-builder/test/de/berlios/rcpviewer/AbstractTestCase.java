package de.berlios.rcpviewer;

import junit.framework.TestCase;

/**
 * Aware of certain components.
 * 
 * @author Dan Haywood
 *
 */
public abstract class AbstractTestCase extends TestCase {

	public AbstractTestCase() { }

	public AbstractTestCase(String name) {
		super(name);
	}

}
