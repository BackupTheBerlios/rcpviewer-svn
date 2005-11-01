/**
 * 
 */
package org.essentialplatform.louis.widgets;

import org.essentialplatform.session.DomainObjectAttributeEvent;
import org.essentialplatform.session.DomainObjectEvent;
import org.essentialplatform.session.DomainObjectReferenceEvent;
import org.essentialplatform.session.IDomainObjectListener;

/**
 * Adapater for the interface.
 * <br>All methokds do nothing.
 * @author Mike
 *
 */
public class DomainObjectListener implements IDomainObjectListener {


	/**
	 * Null method
	 * @see org.essentialplatform.session.IDomainObjectListener#attributeChanged(org.essentialplatform.session.DomainObjectAttributeEvent)
	 */
	public void attributeChanged(DomainObjectAttributeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.session.IDomainObjectListener#collectionAddedTo(org.essentialplatform.session.DomainObjectReferenceEvent)
	 */
	public void collectionAddedTo(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.session.IDomainObjectListener#collectionRemovedFrom(org.essentialplatform.session.DomainObjectReferenceEvent)
	 */
	public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.session.IDomainObjectListener#persisted(org.essentialplatform.session.DomainObjectEvent)
	 */
	public void persisted(DomainObjectEvent event) {
		// TODO Auto-generated method stub

	}

}
