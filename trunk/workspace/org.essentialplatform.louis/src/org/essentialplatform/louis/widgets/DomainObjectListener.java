/**
 * 
 */
package org.essentialplatform.louis.widgets;

import org.essentialplatform.runtime.domain.event.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.domain.event.DomainObjectEvent;
import org.essentialplatform.runtime.domain.event.DomainObjectReferenceEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectListener;

/**
 * Adapater for the interface.
 * <br>All methokds do nothing.
 * @author Mike
 *
 */
public class DomainObjectListener implements IDomainObjectListener {


	/**
	 * Null method
	 * @see org.essentialplatform.runtime.domain.event.IDomainObjectListener#attributeChanged(org.essentialplatform.runtime.domain.event.DomainObjectAttributeEvent)
	 */
	public void attributeChanged(DomainObjectAttributeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.runtime.domain.event.IDomainObjectListener#collectionAddedTo(org.essentialplatform.runtime.domain.event.DomainObjectReferenceEvent)
	 */
	public void collectionAddedTo(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.runtime.domain.event.IDomainObjectListener#collectionRemovedFrom(org.essentialplatform.runtime.domain.event.DomainObjectReferenceEvent)
	 */
	public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Null method
	 * @see org.essentialplatform.runtime.domain.event.IDomainObjectListener#persisted(org.essentialplatform.runtime.domain.event.DomainObjectEvent)
	 */
	public void persisted(DomainObjectEvent event) {
		// TODO Auto-generated method stub

	}

}
