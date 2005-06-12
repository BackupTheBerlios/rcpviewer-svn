package de.berlios.rcpviewer.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

/**
 * Sorts config elements so more specialised ones come before general
 * ones - this is based on the declaring plugins' dependencies so that a plugin
 * 'A' is deemed more specialist than plugin 'B' if its is dependent on plugin
 * 'B'.
 * @author Mike
 *
 */
public class ConfigElementSorter implements Comparator {
	
    private static final Map BUNDLE_REQUISITES = new HashMap();
    private static final Bundle[] EMPTY_BUNDLE_ARRAY = new Bundle[0];

	public int compare(Object o1, Object o2) {
        assert o1 != null;
        assert o1 instanceof IConfigurationElement;
        assert o2 != null;
        assert o2 instanceof IConfigurationElement;
        
        String bundleId1 = ((IConfigurationElement)o1)
            .getDeclaringExtension().getNamespace();
        String bundleId2 = ((IConfigurationElement)o2)
        	.getDeclaringExtension().getNamespace();
        
        // shortcut
        if ( bundleId1.equals( bundleId2 ) ) return 0;
    	// else must do bundle checks
        try {
            Bundle bundle1 = Platform.getBundle( bundleId1 );
        	Bundle bundle2 = Platform.getBundle( bundleId2 );
            if ( isDependent( bundle1, bundle2 ) ) {
                // plugin1 depends on plugin2 so must be higher in list
                return -1;
            }
            else if ( isDependent( bundle2, bundle1 ) ) {
                // plugin2 depends on plugin1 so must be higher in list
                return 1;
            }
        }
        catch ( BundleException be ) {
        	assert false;
        }
        return 0;
	}
       
    // is plugin1 dependent on plugin2
    private boolean isDependent( Bundle bundle1, 
                                 Bundle bundle2 ) throws BundleException  {
    	
    	List required = Arrays.asList( getRequisiteBundles( bundle1 ) );
        return required.contains( bundle2 );
    }
	
    
    /**
     * Returns all bundles that the passed bundle depends on.  This 
     * traverses the full dependency tree.
     * Note an implementation detail: results are cached for performance
     * as it is not expected that the plugin dependencies can change during
     * the life of the VM.
     * @param bundle
     * @return
     */
    private Bundle[] getRequisiteBundles( Bundle bundle )
            throws BundleException {
        if (bundle == null) throw new IllegalArgumentException();
        Bundle[] requisites = (Bundle[])BUNDLE_REQUISITES.get( bundle );
        if ( requisites == null ) {
            String requires = (String)bundle.getHeaders().get( 
                    Constants.REQUIRE_BUNDLE);
            if ( requires == null ) {
            	requisites = EMPTY_BUNDLE_ARRAY;
            }
            else {
                ManifestElement[] elements = ManifestElement.parseHeader(
                        Constants.REQUIRE_BUNDLE, requires);
                // use a set so do not get duplicates
                Set requiredBundles = new HashSet();
                for ( int i=0 ; i < elements.length; i++ ) {
                    Bundle required = Platform.getBundle( elements[i].getValue() );
                    requiredBundles.add( required );
                    // recursive call
                    Bundle[] requiredsRequired = getRequisiteBundles( required ) ;
                    for ( int j=0 ; j < requiredsRequired.length; j++ ) {
                    	requiredBundles.add( requiredsRequired[j] );
                    }
                }
                // convert to array and cache
                int numRequired = requiredBundles.size();
                requisites = new Bundle[ numRequired ];
                Iterator it = requiredBundles.iterator();
                for ( int i=0 ; i < numRequired; i++ ) {
                    requisites[i] = (Bundle)it.next();
                }
            }
            BUNDLE_REQUISITES.put( bundle, requisites );
        }
        assert requisites != null;
        return requisites;
    }
        
}