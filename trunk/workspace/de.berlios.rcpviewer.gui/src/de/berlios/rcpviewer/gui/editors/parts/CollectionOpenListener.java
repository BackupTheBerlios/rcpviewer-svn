/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.gui.jobs.OpenDomainObjectJob;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Opens the selected domain object in its own editor
 * @author Mike
 */
class CollectionOpenListener implements IOpenListener {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IOpenListener#open(org.eclipse.jface.viewers.OpenEvent)
	 */
	public void open(OpenEvent event) {
		if( event == null ) throw new IllegalArgumentException();
    	Object obj = ((StructuredSelection)event.getSelection()).getFirstElement();
    	// defensive programing!
    	if ( obj != null && obj instanceof IDomainObject ) {
    		UIJob job = new OpenDomainObjectJob( (IDomainObject)obj );
    		job.schedule();
    	}
    }
}
