/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectListener implements IDomainObjectListener {
	boolean persistedCallbackCalled = false;
	DomainObjectEvent event;
	public void persisted(DomainObjectEvent event) {
		persistedCallbackCalled=true;
		this.event = event;
	}

}