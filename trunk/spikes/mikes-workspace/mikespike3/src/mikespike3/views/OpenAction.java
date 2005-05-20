package mikespike3.views;

import mikespike3.Plugin;
import mikespike3.editors.Editor;
import mikespike3.editors.EditorInput;
import mikespike3.gui.IEditorContentBuilder;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Mike
 */
class OpenAction extends Action {
	
	private final Viewer viewer;

	OpenAction( Viewer viewer ) {
		super();
		assert viewer != null;
		this.viewer = viewer;
		setText( "Open" );
	}
	
	public void run() {
		if ( !viewer.getSelection().isEmpty() ) {
			try {
				Object obj = ((StructuredSelection)
						viewer.getSelection()).getFirstElement();
				IEditorContentBuilder builder
					= Plugin.getDefault()
							.getEditorContentBuilderFactory()
							.getDefaultInstance( obj.getClass() );
				PlatformUI.getWorkbench()
				          .getActiveWorkbenchWindow()
				          .getActivePage()
				          .openEditor( new EditorInput( obj, builder ), 
								       Editor.class.getName() );
			}
			catch ( PartInitException pie ) {
				// no visible error msg yet
				Plugin.getDefault().getLog().log( pie.getStatus() );
			}
		}
	}
}