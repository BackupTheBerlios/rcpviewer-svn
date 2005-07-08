/**
 * 
 */
package de.berlios.rcpviewer.gui.widgets;

import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.DomainObjectEvent;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObjectListener;

/**
 * Adapater for the interface.
 * <br>All methokds do nothing.
 * @author Mike
 *
 */
public class DomainObjectListener implements IDomainObjectListener {


	/**
	 * Null method
	 * @see de.berlios.rcpviewer.session.IDomainObjectListener#attributeChanged(de.berlios.rcpviewer.session.DomainObjectAttributeEvent)
	 */
	public void attributeChanged(DomainObjectAttributeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see de.berlios.rcpviewer.session.IDomainObjectListener#collectionAddedTo(de.berlios.rcpviewer.session.DomainObjectReferenceEvent)
	 */
	public void collectionAddedTo(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see de.berlios.rcpviewer.session.IDomainObjectListener#collectionRemovedFrom(de.berlios.rcpviewer.session.DomainObjectReferenceEvent)
	 */
	public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see de.berlios.rcpviewer.session.IDomainObjectListener#persisted(de.berlios.rcpviewer.session.DomainObjectEvent)
	 */
	public void persisted(DomainObjectEvent event) {
		// TODO Auto-generated method stub

	}

}
