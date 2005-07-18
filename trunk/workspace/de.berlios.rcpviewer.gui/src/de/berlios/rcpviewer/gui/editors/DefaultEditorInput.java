package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Input for the <code>DefaultEditor</code>
 * @author Mike
 * @see de.berlios.rcpviewer.gui.editors.DefaultEditor
 */
public class DefaultEditorInput implements IEditorInput {

	private final IDomainObject _domainObject;

	/**
	 * @param obj
	 * @param builder
	 */
	public DefaultEditorInput( IDomainObject obj ) {
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
		Image image = GuiPlugin.getDefault()
						       .getLabelProvider( _domainObject )
						       .getImage( _domainObject );
		if ( image != null ) {
			return ImageDescriptor.createFromImage( image );
		}
		return ImageDescriptor.getMissingImageDescriptor();
	}

	

	/**
	 * Takes name from label provider for domain object.
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return GuiPlugin.getDefault()
			            .getLabelProvider( _domainObject )
			            .getText( _domainObject );

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
			s = GuiPlugin.getResourceString( "DefaultEditorInput.NoDescription"); //$NON-NLS-1$
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
	 * Accessor
	 * @return
	 */
	public IDomainObject getDomainObject() {
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