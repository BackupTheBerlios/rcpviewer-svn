package mikespike3.editors;

import mikespike3.Plugin;
import mikespike3.gui.IFormBuilder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class DefaultEditor extends EditorPart {

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite( site );
		setInput( input );
		setPartName( ((DefaultEditorInput)
				getEditorInput()).getObject().getClass().getName() );
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Object object = ((DefaultEditorInput)getEditorInput()).getObject();
		IFormBuilder builder =
			Plugin.getDefault().getFormBuilderFactory().create( object  );
		builder.createGui( parent, object );
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	
}
