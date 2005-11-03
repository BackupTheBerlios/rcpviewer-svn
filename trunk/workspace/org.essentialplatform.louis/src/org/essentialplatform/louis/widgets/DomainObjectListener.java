/**
 * 
 */
package org.essentialplatform.louis.widgets;

import org.essentialplatform.runtime.session.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.session.DomainObjectEvent;
import org.essentialplatform.runtime.session.DomainObjectReferenceEvent;
import org.essentialplatform.runtime.session.IDomainObjectListener;

/**
 * Adapater for the interface.
 * <br>All methokds do nothing.
 * @author Mike
 *
 */
public class DomainObjectListener implements IDomainObjectListener {


	/**
	 * Null method
	 * @see org.essentialplatform.runtime.session.IDomainObjectListener#attributeChanged(org.essentialplatform.runtime.session.DomainObjectAttributeEvent)
	 */
	public void attributeChanged(DomainObjectAttributeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.runtime.session.IDomainObjectListener#collectionAddedTo(org.essentialplatform.runtime.session.DomainObjectReferenceEvent)
	 */
	public void collectionAddedTo(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.runtime.session.IDomainObjectListener#collectionRemovedFrom(org.essentialplatform.runtime.session.DomainObjectReferenceEvent)
	 */
	public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.runtime.session.IDomainObjectListener#persisted(org.essentialplatform.runtime.session.DomainObjectEvent)
	 */
	public void persisted(DomainObjectEvent event) {
		// TODO Auto-generated method stub

	}

}
