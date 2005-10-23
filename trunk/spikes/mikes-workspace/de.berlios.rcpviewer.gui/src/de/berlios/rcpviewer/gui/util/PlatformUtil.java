/*
 * Copyright (c) Incremental Ltd. 2004, 2005
 */
package de.berlios.rcpviewer.gui.util;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.berlios.rcpviewer.gui.editors.DefaultEditor;
import de.berlios.rcpviewer.gui.editors.DefaultEditorInput;
import de.berlios.rcpviewer.session.IDomainObject;




/**
 * Static library functions
 */
public class PlatformUtil {
    
    
    /**
     * Tidy access to <code>getActivePage</code>
     * @return
     */
    public static final IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench()
          .getActiveWorkbenchWindow()
          .getActivePage();
    }
    
    /**
     * Tidy access to workbench's <code>Shell</code>
     * @return
     */
    public static final Shell getActiveShell() {
        return PlatformUI.getWorkbench()
          .getActiveWorkbenchWindow()
          .getShell();
    }
        
    /**
     * Looks for and returns the <b>active</b> editor 
     * displaying the passed object or <code>null</code> if none.
     * <br>This compliments <code>IWorkbenchPage.openEditor()</code> which
     * would automatically open the editor if it was not already.
     * <br>Note this currently assumes that only one editor can display 
     * an object at a time so returns the first it finds.  This may not hold
     * true in the future.
     * @param obj
     * @return
     * @see org.eclipse.ui.IWorkbenchPage
     */
    public static final DefaultEditor getActiveEditor( IDomainObject obj ) {
    	if ( obj == null ) throw new IllegalArgumentException();
    	IEditorReference[] refs = getActivePage().getEditorReferences();
    	for ( IEditorReference ref : refs ) {
    		IEditorPart part =  ref.getEditor( false );
    		assert part != null;
    		IEditorInput input = part.getEditorInput();
    		if (input instanceof DefaultEditorInput ) {
				DefaultEditorInput defInput = (DefaultEditorInput )input;
				if ( obj.equals( defInput.getDomainObject() ) ) {
					return (DefaultEditor)part;
				}
			}
    	}
    	return null;
    }

    /**
     * Returns the view in the current page with the given id or 
     * <code>null</code> if it does not exist.
     * @param id
     * @return
     */
    public static final IViewPart getView( String id ) {
    	 if (id == null) throw new IllegalArgumentException();
    	 IViewReference[] refs  = getActivePage().getViewReferences();
    	 for ( int i=0, num = refs.length; i < num ; i++ ) {
    	 	if ( refs[i].getId().equals( id ) ) {
    	 		return refs[i].getView( false );
    	 	}
    	 }
    	 return null;
    }
}
