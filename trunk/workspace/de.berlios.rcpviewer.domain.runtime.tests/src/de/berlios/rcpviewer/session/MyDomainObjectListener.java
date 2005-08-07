/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectListener implements IDomainObjectListener {
	boolean persistedCallbackCalled = false;
	public void persisted(DomainObjectEvent event) {
		persistedCallbackCalled=true;
	}

}