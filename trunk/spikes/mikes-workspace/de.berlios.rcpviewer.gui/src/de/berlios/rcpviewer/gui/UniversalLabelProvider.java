/**
 * 
 */
package de.berlios.rcpviewer.gui;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.util.ConfigElementSorter;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * @author Mike
 *
 */
class UniversalLabelProvider extends LabelProvider {

	private final IDomainObjectLabelProvider[] _domainObjectLabelProviders;
	private final Map<IDomainClass, ILabelProvider> _mappings;
	private final ILabelProvider _defaultDomainObjectProvider;
	private final ILabelProvider _defaultGenericProvider;
	
	/* constructor */
	
	/**
	 * Constructor instantiates all implementations of 
	 * <code>IDomainObjectLabelProvider.EXTENSION_POINT_ID </code> extension point.
	 * @throws CoreException
	 */
	UniversalLabelProvider() throws CoreException {
		super();
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( 
                		  IDomainObjectLabelProvider.EXTENSION_POINT_ID );
		Arrays.sort( elems, new ConfigElementSorter() );
		int num = elems.length;
		_domainObjectLabelProviders = new IDomainObjectLabelProvider[ num ];
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = elems[i].createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof IFieldBuilder;
			_domainObjectLabelProviders[i] = (IDomainObjectLabelProvider)obj;
		}
		_mappings = new HashMap<IDomainClass, ILabelProvider>();
		
		_defaultDomainObjectProvider = new DefaultDomainObjectLabelProvider();
		_defaultGenericProvider = new DefaultGenericLabelProvider();
	}
	
	/* partial ILabelProvider contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return getLabelProvider( element ).getImage( element );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		return getLabelProvider( element ).getText( element );
	}

	
	/* private methods */
	
	/**
	 * Returns label provider that can handle any object.
	 * <br>If the object is a <code>IDomainObject</code> or an
	 * <code>IDomainClass</code> passes to the more specialised method
	 * @param object
	 * @return
	 */
	private ILabelProvider getLabelProvider( Object object ) {
		IDomainClass<?> dClass = null;
		if ( object != null && object instanceof IDomainClass<?>) {
			dClass = (IDomainClass<?>)object;
		}
		else if ( object != null && object instanceof IDomainObject<?>) {
			dClass = ((IDomainObject<?>)object).getDomainClass();
		}
		if ( dClass != null ) {
			return getLabelProvider( dClass );
		}
		else {
			return _defaultGenericProvider;
		}
	}

	/**
	 * Selects and generates <code>ILabelProvider</code> appropriate for 
	 * passed domain class
	 * @param clazz
	 * @return
	 */
	private ILabelProvider getLabelProvider( IDomainClass clazz ) {
		assert clazz != null;
		ILabelProvider provider = _mappings.get( clazz );
		if ( provider == null ) {
			for ( int i=0, num = _domainObjectLabelProviders.length ; i < num ; i++ ) {
				if ( _domainObjectLabelProviders[i].isApplicable( clazz ) ) {
					provider = _domainObjectLabelProviders[i];
					break;
				}
			}
			if ( provider == null ) {
				provider = _defaultDomainObjectProvider;
			}
			_mappings.put( clazz, provider );
		}
		return provider;
	}
	
	
	/* private classes */
	
	/**
	 * Can handle any object. See <code>getText()</code> for rules applied.
	 * @author Mike
	 */
	private class DefaultGenericLabelProvider extends LabelProvider {

		/**
		 * <ul>
		 * <li><code>null</code>'s returned as blank strings
		 * <li><code>Date</code>'s given a common format
		 * <li>all otheres subject to <code>String.valueof()</code>
		 * <ul>
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if ( element == null ) return ""; //$NON-NLS-1$
			if ( element instanceof Date ) {
				return GuiPlugin.DATE_FORMATTER.format( (Date)element );
			}
			else {
				return String.valueOf( element );
			}
		}
	}
	
	/**
	 * Used when no other available <code>IDomainObjectLabelProvider</code> 
	 * is applicable.
	 * @author Mike
	 */
	private class DefaultDomainObjectLabelProvider extends LabelProvider {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if ( element == null ) throw new IllegalArgumentException();
			if ( !( element instanceof IDomainObject ) ) {
				throw new IllegalArgumentException();
			}
			return ((IDomainObject<?>)element).title();
		}

		/**
		 * Creates an image based on the passed object's <code>IDomainClass</code>
		 * using <code>ImageUtil</code>.
		 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object element) {
			if ( element == null ) throw new IllegalArgumentException();
			if ( !( element instanceof IDomainObject ) ) {
				throw new IllegalArgumentException();
			}
			return ImageUtil.getImage( ((IDomainObject<?>)element).getDomainClass() );
		}
	}

}
