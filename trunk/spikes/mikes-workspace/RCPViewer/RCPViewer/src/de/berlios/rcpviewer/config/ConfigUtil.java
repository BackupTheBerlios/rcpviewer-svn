package de.berlios.rcpviewer.config;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.nakedobjects.reflector.java.fixture.JavaFixture;
import org.osgi.framework.Bundle;

/**
 * Reads extension point info
 * @author Mike
 */
public class ConfigUtil {
	
	public static final String FIXTURE_EXTENSION_POINT = "de.berlios.rcpviewer.fixture";
	public static final String CLASS_ATTRIB = "class";

	/**
	 * Returns instantiated instances of all fixtures defined in extension point
	 * <code>FIXTURE_EXTENSION_POINT</code>.  May be an empty array but never 
	 * <code>null</code>.
	 * @return
	 */
	public static JavaFixture[] instantiateFixtures() throws Exception {
		
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                      .getConfigurationElementsFor( FIXTURE_EXTENSION_POINT );
		int num = elems.length;
		JavaFixture[] fixtures = new JavaFixture[num];
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = instantiateClass( elems[i] );
			if ( !( obj instanceof JavaFixture ) ) throw new Exception("invalid fixture class");
			fixtures[i] = (JavaFixture)obj;
		}
		return fixtures;
	}
	
	/**
	 * Return all bundles contributiong extension points to the the app.
	 * @return
	 */
	public static Bundle[] getExtensionBundles() {
        IConfigurationElement[] elems
	    	= Platform.getExtensionRegistry()
	                  .getConfigurationElementsFor( FIXTURE_EXTENSION_POINT );
		int num = elems.length;
		Set bundles = new HashSet();
		for ( int i=0 ; i < num ; i++ ) {
			bundles.add( Platform.getBundle( elems[i].getNamespace() ) );
		}
		return (Bundle[])bundles.toArray( new Bundle[0] );
	}
	
    // main method that creates an implementation of a config element
    private static Object instantiateClass( IConfigurationElement elem ) throws Exception {
        if (elem == null)
			throw new RuntimeException("configuration element must not be null");

        String bundleId = elem.getDeclaringExtension().getNamespace();
        Bundle bundle = Platform.getBundle( bundleId );

        String className = elem.getAttribute( CLASS_ATTRIB );
		if ( className == null ) throw new Exception( "no class defined" );
		Class clazz = bundle.loadClass( className );
        return elem.createExecutableExtension( CLASS_ATTRIB );
    }
}
