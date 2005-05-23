/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectListener implements IDomainObjectListener {
	boolean attributeChangedCallbackCalled = false;
	public void attributeChanged(DomainObjectAttributeEvent event) {
		attributeChangedCallbackCalled=true;
	}

	boolean persistedCallbackCalled = false;
	public void persisted(DomainObjectEvent event) {
		persistedCallbackCalled=true;
	}

	boolean collectionAddedToCallbackCalled = false;
	public void collectionAddedTo(DomainObjectReferenceEvent event) {
		collectionAddedToCallbackCalled=true;
	}
	
	boolean collectionRemovedFromCallbackCalled = false;
	public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
		collectionRemovedFromCallbackCalled=true;
	}

}