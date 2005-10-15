/**
 * 
 */
package org.essentialplatform.louis.labelproviders;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.util.ConfigElementSorter;

/**
 * A global <code>ILabelProvider</code> that can handle any object.
 * <br>Collects all declared <code>ILouisLabelProvider</code>'s and 
 * loops through these using the first that can handle the passed object.
 * If none found uses a generic label provider.
 * @author Mike
 */
public class GlobalLabelProvider extends LabelProvider implements ILabelProvider {

	private final ILouisLabelProvider[] _labelProviders;
	
	/* constructor */
	
	/**
	 * Constructor instantiates all implementations of 
	 * <code>ILouisLabelProvider.EXTENSION_POINT_ID </code> extension point.
	 * @throws CoreException
	 */
	public GlobalLabelProvider() throws CoreException {
		super();
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( 
                		  ILouisLabelProvider.EXTENSION_POINT_ID );
		Arrays.sort( elems, new ConfigElementSorter() );
		int num = elems.length;
		_labelProviders = new ILouisLabelProvider[ num + 3 ];
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = elems[i].createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof ILouisLabelProvider;
			_labelProviders[i] = (ILouisLabelProvider)obj;
		}
		_labelProviders[num] = new DefaultDomainObjectLabelProvider();
		_labelProviders[num+1] = new BaseClassLabelProvider();
		_labelProviders[num+2] = new DefaultLabelProvider();
	}
	
	/* ILabelProvider contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		for ( ILabelProvider provider : _labelProviders ) {
			String s = provider.getText( element );
			if ( s != null ) return s;
		}
		// should bnever get here as last in array is default label provider
		assert false;
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		for ( ILabelProvider provider : _labelProviders ) {
			Image i = provider.getImage( element );
			if ( i != null ) return i;
		}
		// should bnever get here as last in array is default label provider
		assert false;
		return null;
	}
}
