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
public class GuiFactories  {
	
	// that this is a Lits is important as it enforces a specific search order.
	private final List<IGuiFactory<?>> _factories;

	/**
	 * Constructor instantiates all implementations of 
	 * <code>IGuiFactory.EXTENSION_POINT_ID </code> extension point.
	 * @throws CoreException
	 */
	public GuiFactories() throws CoreException {
        IConfigurationElement[] elems
	    	= Platform.getExtensionRegistry()
	              .getConfigurationElementsFor( IGuiFactory.EXTENSION_POINT_ID );
		
		_factories = new ArrayList<IGuiFactory<?>>();
        
		// add all extensions - sort order is important as it esnures more 
		// specific factories are prioritised
        Arrays.sort( elems, new ConfigElementSorter() );
		for( IConfigurationElement config : elems ) {
			Object factory = config.createExecutableExtension( "class" ); //$NON-NLS-1$
			assert factory instanceof IGuiFactory;
			_factories.add( (IGuiFactory)factory );
		}
		
		// now add default factories, again in order of their intended
		// prioritisation
		_factories.add( new StringAttributeGuiFactory() );
		_factories.add( new BigDecimalAttributeGuiFactory() );
		_factories.add( new DateAttributeGuiFactory() );
		_factories.add( new BooleanAttributeGuiFactory() );
		_factories.add( new CharAttributeGuiFactory() );
		_factories.add( new PrimitiveAttributeGuiFactory() );
		_factories.add( new ReferenceGuiFactory() );
		_factories.add( new CollectionGuiFactory() );
		_factories.add( new CollectionTableGuiFactory() );
		_factories.add( new CollectionMasterChildGuiFactory() );
		_factories.add( new DomainClassGuiFactory() );
	}
	
	/**
	 * Returns first factory that is applicable for the passed arguments.
	 * @param model
	 * @param parent
	 * @return
	 */
	public IGuiFactory<?> getFactory(Object model, IGuiFactory parent) {
		for( IGuiFactory<?> factory : _factories ) {
			if ( factory.isApplicable( model, parent ) ) {
				return factory;
			}
		}
		// catch-all
		return new DefaultGuiFactory();
	}
	
	/**
	 * Returns all factories that are applicable for the passed arguments.
	 * <br>Will never be empty as will include the default factory if no
	 * others found.  However if others are found then will <code>not</code>
	 * include the default factory.
	 * @param model
	 * @param parent
	 * @return
	 */
	public List<IGuiFactory<?>> getFactories(Object model, IGuiFactory<?> parent) {
		List<IGuiFactory<?>> applicable = new ArrayList<IGuiFactory<?>>();
		for( IGuiFactory<?> factory : _factories ) {
			if ( factory.isApplicable( model, parent ) ) {
				applicable.add( factory );
			}
		}
		// catch-all
		if ( applicable.isEmpty() ) applicable.add( new DefaultGuiFactory() );
		return applicable;
	}


}
