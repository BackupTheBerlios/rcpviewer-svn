package de.berlios.rcpviewer.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.berlios.rcpviewer.gui.IEditorContentBuilder;
import de.berlios.rcpviewer.session.IDomainObject;

public class RcpViewerEditorInput implements IEditorInput {
	
	private final IDomainObject _domainObject;
	private final IEditorContentBuilder builder;

	public RcpViewerEditorInput(IDomainObject obj, IEditorContentBuilder builder ) {
		if ( obj == null ) throw new IllegalArgumentException();
		if ( builder == null ) throw new IllegalArgumentException();
		_domainObject = obj;
		this.builder = builder;
	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	public String getName() {
		return _domainObject.getDomainClass().getName();
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return _domainObject.getDomainClass().getDescription();
	}

	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
		
	public IEditorContentBuilder getBuilder() {
		return builder;
	}

	public IDomainObject getDomainObject() {
		return _domainObject;
	}
}
