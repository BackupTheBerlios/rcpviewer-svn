package mikespike2_runtime.editors;

import mikespike2_runtime.model.God;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class DefaultEditorInput implements IEditorInput {
	
	private final Object obj;

	public DefaultEditorInput( Object obj ) {
		if ( obj == null ) throw new IllegalArgumentException();
		// check God knows about it
		God.getInstance().getName( obj.getClass() );
		this.obj = obj;
	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	public String getName() {
		return God.getInstance().getName( obj.getClass() );
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return God.getInstance().getName( obj.getClass() );
	}

	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}
