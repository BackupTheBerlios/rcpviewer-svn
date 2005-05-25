package mikespike3.views;

import mikespike3.Plugin;
import mikespike3.editors.Editor;
import mikespike3.editors.EditorInput;
import mikespike3.gui.IEditorContentBuilder;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Mike
 */
class OpenWithAction extends Action implements IMenuCreator {
	
	private final Viewer viewer;
	private Menu menu = null;
	
	public OpenWithAction( Viewer viewer ) {
		super( "Open With", IAction.AS_DROP_DOWN_MENU ) ;
		this.viewer = viewer;
		setMenuCreator( this );
		// create actions
		
	}
	
	public Menu getMenu(Control parent) {
		return null;
	}
	
	public Menu getMenu(Menu parent) {
		if ( viewer.getSelection().isEmpty() ) return null;
		Object obj = 
			((StructuredSelection)viewer.getSelection()).getFirstElement();
		IEditorContentBuilder[] builders = Plugin
		      .getDefault()
		      .getEditorContentBuilderFactory()
		      .getInstances( obj.getClass() );
		menu = new Menu( parent );
		for ( int i=0 ; i < builders.length ; i++ ) {
			ActionContributionItem item
				= new ActionContributionItem( 
					new BuilderAction( builders[i], obj ) );
			item.fill( menu, i );
		}
		return menu;
	}
	
	public void dispose() {
		if ( menu != null ) menu.dispose();
	}
	
	/* private class */
	
	private class BuilderAction extends Action {
		
		private final IEditorContentBuilder builder;
		private final Object obj;
		
		BuilderAction(  IEditorContentBuilder builder, Object obj ) {
			super( builder.getDisplay() );
			this.builder = builder;
			this.obj = obj;
		}

		@Override
		public void run() {
			try {
				PlatformUI.getWorkbench()
		          .getActiveWorkbenchWindow()
		          .getActivePage()
		          .openEditor( new EditorInput( obj, builder ), 
						       Editor.class.getName() );
				Plugin.getDefault()
				      .getEditorContentBuilderFactory()
				      .setDefaultInstance( obj.getClass(), builder );
			}
			catch ( PartInitException pie ) {
				// no visible error msg yet
				Plugin.getDefault().getLog().log( pie.getStatus() );
			}
		}
	}
}