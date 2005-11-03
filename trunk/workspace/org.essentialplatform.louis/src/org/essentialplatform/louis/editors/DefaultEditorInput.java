package org.essentialplatform.louis.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.ImageUtil;

import org.essentialplatform.runtime.session.IDomainObject;

/**
 * Input for the <code>DefaultEditor</code>
 * @author Mike
 * @see org.essentialplatform.louis.editors.DefaultEditor
 */
public class DefaultEditorInput<T> implements IEditorInput {

	private static final Point IMAGE_SIZE = new Point( 16, 16 );
	
	private final IDomainObject<T> _domainObject;
	
	// lazily instantiated
	private Image _image = null;

	/**
	 * @param obj
	 * @param builder
	 */
	public DefaultEditorInput( IDomainObject<T> obj ) {
		if ( obj == null ) throw new IllegalArgumentException();
		_domainObject = obj;
	}


	/**
	 * Always returns <code>false</code> - no not want on recently used list.
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return false;
	}

	/**
	 * Takes image from label provider for domain object.
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromImage( getImage() );
	}

	/**
	 * Takes image from label provider for domain object.
	 */
	public Image getImage() {
		if ( _image == null ) {
			_image = LouisPlugin.getImage( _domainObject );
			_image = ImageUtil.resize( _image, IMAGE_SIZE );
		}
		return _image;
	}

	/**
	 * Takes name from label provider for domain object.
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return LouisPlugin.getText( _domainObject );

	}

	
	/**
	 * Currently always returns <code>null</code> - cannot persiist editor.
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * Ensures never <code>null</code> even if domain model has no description
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		String s = _domainObject.getDomainClass().getDescription();
		if ( s == null ) {
			s = LouisPlugin.getResourceString( "DefaultEditorInput.NoDescription"); //$NON-NLS-1$
		}
		return s;
	}

	/**
	 * No adapters currently.
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	/**
	 * Underlying domain object for which this is the input.
	 * 
	 * @return
	 */
	public IDomainObject<T> getDomainObject() {
		return _domainObject;
	}
	
	/**
	 * Used to decide whether there is an open editor for the passed input.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object arg0 ) {
		if ( arg0 == null ) return false;
		if ( arg0 == this ) return true;
		if ( !( arg0 instanceof DefaultEditorInput ) ) return false;
		return _domainObject.equals( 
				((DefaultEditorInput)arg0).getDomainObject() ) ;

	}
	
	/**
	 * Compatible with <code>equals()</code>.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 17 + 19*_domainObject.hashCode();
	}



}