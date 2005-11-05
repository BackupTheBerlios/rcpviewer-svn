/**
 * 
 */
package org.essentialplatform.louis.factory.reference.collection;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.progress.UIJob;
import org.essentialplatform.louis.jobs.OpenDomainObjectJob;

import org.essentialplatform.runtime.domain.IDomainObject;

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
