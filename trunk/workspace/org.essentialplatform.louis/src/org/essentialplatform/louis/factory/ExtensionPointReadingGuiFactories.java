package org.essentialplatform.louis.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.essentialplatform.louis.factory.attribute.BigDecimalAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.BooleanAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.CharAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.DateAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.PrimitiveAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.StringAttributeGuiFactory;
import org.essentialplatform.louis.factory.reference.ReferenceGuiFactory;
import org.essentialplatform.louis.factory.reference.collection.CollectionGuiFactory;
import org.essentialplatform.louis.factory.reference.collection.CollectionMasterChildGuiFactory;
import org.essentialplatform.louis.factory.reference.collection.CollectionTableGuiFactory;
import org.essentialplatform.louis.util.ConfigElementSorter;


/**
 * Collection of all gui factories.
 * @author Mike
 */
public class ExtensionPointReadingGuiFactories extends AbstractGuiFactoriesChain {

	public static final String EXTENSION_POINT_ID = "org.essentialplatform.guifactory"; //$NON-NLS-1$

	/**
	 * Constructor instantiates all implementations of 
	 * <code>IGuiFactory.EXTENSION_POINT_ID </code> extension point.
	 * 
	 * @throws CoreException
	 */
	public ExtensionPointReadingGuiFactories() throws CoreException {
        IConfigurationElement[] elems
	    	= Platform.getExtensionRegistry().getConfigurationElementsFor( EXTENSION_POINT_ID );
		
        
		// add all extensions - sort order is important as it ensures more 
		// specific factories are prioritised
        Arrays.sort( elems, new ConfigElementSorter() );
		for( IConfigurationElement config : elems ) {
			Object factory = config.createExecutableExtension( "class" ); //$NON-NLS-1$
			assert factory instanceof IGuiFactory;
			addFactory((IGuiFactory)factory);
		}
		
	}


}
