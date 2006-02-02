/**
 * 
 */
package org.essentialplatform.louis.labelproviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.util.ConfigElementSorter;

/**
 * A global <code>ILouisLabelProvider</code> that can handle any object.
 * 
 * <p>
 * Collects all declared <code>ILouisLabelProvider</code>'s and 
 * loops through these using the first that can handle the passed object.
 * If none found uses a generic label provider.
 * 
 * @author Mike
 */
public final class ExtensionPointReadingGlobalLabelProvider extends AbstractLabelProviderChain {

	public static final String EXTENSION_POINT_ID = 
		"org.essentialplatform.louis.labelprovider"; //$NON-NLS-1$
	

	/**
	 * Constructor instantiates all implementations of 
	 * <code>AbstractLabelProviderChain.EXTENSION_POINT_ID</code> 
	 * extension point.
	 * 
	 * @throws CoreException
	 */
	public ExtensionPointReadingGlobalLabelProvider() throws CoreException {
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID );
		Arrays.sort( elems, new ConfigElementSorter() );
		for ( int i=0 ; i < elems.length ; i++ ) {
			Object obj = elems[i].createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof ILouisLabelProvider;
			addLabelProvider((ILouisLabelProvider)obj);
		}
	}


}
