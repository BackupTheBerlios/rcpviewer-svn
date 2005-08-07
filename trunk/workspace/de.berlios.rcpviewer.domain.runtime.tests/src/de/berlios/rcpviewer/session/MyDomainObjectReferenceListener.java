/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectReferenceListener implements IDomainObjectReferenceListener {

	boolean collectionAddedToCallbackCalled = false;
	public void collectionAddedTo(DomainObjectReferenceEvent event) {
		collectionAddedToCallbackCalled=true;
	}
	
	boolean collectionRemovedFromCallbackCalled = false;
	public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
		collectionRemovedFromCallbackCalled=true;
	}
	
	boolean referenceChanged = false;
	public void referenceChanged(DomainObjectReferenceEvent event) {
		referenceChanged = true;
	}

}