/**
 * 
 */
package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.runtime.domain.event.DomainObjectReferenceEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectReferenceEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectReferenceListener;

class MyDomainObjectReferenceListener implements IDomainObjectReferenceListener {

	boolean collectionAddedToCallbackCalled = false;
	DomainObjectReferenceEvent event;
	
	public void collectionAddedTo(DomainObjectReferenceEvent event) {
		collectionAddedToCallbackCalled=true;
		this.event = event;
	}
	
	boolean collectionRemovedFromCallbackCalled = false;
	public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
		collectionRemovedFromCallbackCalled=true;
		this.event = event;
	}
	
	boolean referenceChanged = false;
	public void referenceChanged(DomainObjectReferenceEvent event) {
		referenceChanged = true;
		this.event = event;
	}

	boolean referencePrerequisitesChangedCallbackCalled = false;
	ExtendedDomainObjectReferenceEvent extendedEvent;
	
	public void referencePrerequisitesChanged(ExtendedDomainObjectReferenceEvent extendedEvent) {
		referencePrerequisitesChangedCallbackCalled=true;
		this.extendedEvent = extendedEvent;
	}


}