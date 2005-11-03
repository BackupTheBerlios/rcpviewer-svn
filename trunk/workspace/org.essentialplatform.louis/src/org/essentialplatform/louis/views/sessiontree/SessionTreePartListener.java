/**
 * 
 */
package org.essentialplatform.louis.views.sessiontree;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.essentialplatform.louis.editors.DefaultEditor;
import org.essentialplatform.louis.editors.DefaultEditorInput;
import org.essentialplatform.louis.util.PlatformUtil;

import org.essentialplatform.runtime.session.IDomainObject;

/**
 * Highlights in viewer the input of the activated editor.
 * @author Mike
 */
public class SessionTreePartListener implements IPartListener {
	
	private final TreeViewer _viewer;

	/**
	 * Constructor requires viewer.
	 * <br>Adds itself as a listener on the current page, or if none - on the 
	 * next opened page.
	 * @param viewer
	 */
	SessionTreePartListener( TreeViewer viewer ) {
		assert viewer != null;
		_viewer = viewer;
		IWorkbenchPage page = PlatformUtil.getActivePage();
		if ( page != null ) {
			page.addPartListener( this );
		}
		else {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPageListener(
				new IPageListener() {
				    public void pageActivated(IWorkbenchPage page){
				    	// does nowt
				    }
				    public void pageClosed(IWorkbenchPage page){
				    	// does nowt
				    }
				    public void pageOpened(IWorkbenchPage page){
				    	page.addPartListener( SessionTreePartListener.this );
				    	PlatformUI.getWorkbench()
				    			  .getActiveWorkbenchWindow()
				    			  .removePageListener( this );
				    }
				} );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partActivated(IWorkbenchPart part) {
		if ( part instanceof DefaultEditor ) {
			_viewer.setSelection( 
				new StructuredSelection(
					getDomainObject( (DefaultEditor)part ) ),
				true );
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partDeactivated(IWorkbenchPart part) {
		if ( part instanceof DefaultEditor ) {
			_viewer.setSelection( StructuredSelection.EMPTY ) ;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	/**
	 * Stops listening.
	 */
	void dispose() {
		IWorkbenchPage page = PlatformUtil.getActivePage();
		if ( page != null ) page.removePartListener( this );
	}
	
	// just to tidy code
	private IDomainObject getDomainObject( DefaultEditor editor ) {
		return ((DefaultEditorInput)editor.getEditorInput()).getDomainObject();
	}
}
