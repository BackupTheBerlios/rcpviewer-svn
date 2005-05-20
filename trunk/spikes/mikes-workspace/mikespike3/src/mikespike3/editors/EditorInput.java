package mikespike3.editors;

import mikespike3.gui.IEditorContentBuilder;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class EditorInput implements IEditorInput {
	
	private final Object obj;
	private final IEditorContentBuilder builder;

	public EditorInput( Object obj, IEditorContentBuilder builder ) {
		if ( obj == null ) throw new IllegalArgumentException();
		if ( builder == null ) throw new IllegalArgumentException();
		this.obj = obj;
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
		return obj.getClass().getName();
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return obj.getClass().getName();
	}

	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Object getObject() {
		return obj;
	}
	
	public IEditorContentBuilder getBuilder() {
		return builder;
	}

}
