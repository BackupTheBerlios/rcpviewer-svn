package de.berlios.rcpviewer.app;

import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.reflector.java.JavaObjectFactory;
import org.osgi.framework.Bundle;

import de.berlios.rcpviewer.config.ConfigUtil;

/**
 * Handles plugin classloader fun.
 * @author Mike
 */
class RCPObjectFactory extends JavaObjectFactory {

	/**
	 * Overrides parent class method to handle plugin classloader issues.
	 * @see org.nakedobjects.object.ObjectFactory#createObject(org.nakedobjects.object.NakedObjectSpecification)
	 */
	public Object createObject(NakedObjectSpecification specification) {
		String className = specification.getFullName();
		Bundle[] bundles = ConfigUtil.getExtensionBundles();
		for ( int i=0, num = bundles.length ; i < num ; i++ ) {
			try {
				Class clazz = bundles[i].loadClass( className );
				return super.createObject( clazz );
			}
			catch (ClassNotFoundException ignore ) {
				// try next bundle
			}
		}
		return super.createObject(specification);
	}

	

}
