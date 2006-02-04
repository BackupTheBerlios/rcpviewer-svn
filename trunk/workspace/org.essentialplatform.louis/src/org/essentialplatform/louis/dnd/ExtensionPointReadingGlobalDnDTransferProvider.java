package org.essentialplatform.louis.dnd;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.essentialplatform.louis.util.ConfigElementSorter;

/**
 * A global <code>IDndTransferProvider</code> that can handle any object.
 * 
 * <p>
 * Collects all declared <code>IDndTransferProvider</code>'s and 
 * loops through these using the first that can handle the passed object.
 * If none found creates a default transfer provider that will work only
 * within Louis.
 * 
 * @author Mike
 */
public final class ExtensionPointReadingGlobalDnDTransferProvider extends AbstractDndTransferProvider {
	
	public static final String EXTENSION_POINT_ID = "org.essentialplatform.louis.dnd.transferprovider"; //$NON-NLS-1$


	/**
	 * Constructor instantiates all implementations of 
	 * <code>ExtensionPointReadingGlobalDnDTransferProvider.EXTENSION_POINT_ID </code> extension point.
	 * 
	 * @throws CoreException
	 */
	public ExtensionPointReadingGlobalDnDTransferProvider() throws CoreException {
        
		IConfigurationElement[] elems = 
    		Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID );
        Arrays.sort( elems, new ConfigElementSorter() );
        
        for ( IConfigurationElement elem : elems ) {
			Object obj = elem.createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof IDndTransferProvider;
			addDeclaredProvider((IDndTransferProvider)obj);
        }
	}


}
