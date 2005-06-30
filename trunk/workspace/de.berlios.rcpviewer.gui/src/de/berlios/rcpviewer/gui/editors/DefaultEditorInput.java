package de.berlios.rcpviewer.gui.editors;

import org.eclipse.jface.resource.ImageDescriptor;
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
	private final IEditorContentBuilder _builder;

	/**
	 * @param obj
	 * @param builder
	 */
	public DefaultEditorInput(IDomainObject obj, IEditorContentBuilder builder ) {
		if ( obj == null ) throw new IllegalArgumentException();
		if ( builder == null ) throw new IllegalArgumentException();
		_domainObject = obj;
		_builder = builder;
	}


	/**
	 * Always returns <code>false</code> - no not want on recently used list.
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return false;
	}

	/**
	 * Currently always the default missing image descriptor
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	
	/**
	 * Ensures never <code>null</code> even if domain model has no name
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		String s = _domainObject.getDomainClass().getName();
		if ( s == null ) {
			s = GuiPlugin.getResourceString( "DefaultEditorInput.NoName");
		}
		return s;
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
			s = GuiPlugin.getResourceString( "DefaultEditorInput.NoDescription");
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
	
	/* package-private methods */
		
	/**
	 * Accessor
	 * @return
	 */
	IEditorContentBuilder getBuilder() {
		return _builder;
	}

	/**
	 * Accessor
	 * @return
	 */
	IDomainObject getDomainObject() {
		return _domainObject;
	}
}