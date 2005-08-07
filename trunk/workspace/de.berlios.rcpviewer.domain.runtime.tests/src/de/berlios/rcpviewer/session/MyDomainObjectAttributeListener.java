/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectAttributeListener implements IDomainObjectAttributeListener {
	boolean attributeChangedCallbackCalled = false;
	public void attributeChanged(DomainObjectAttributeEvent event) {
		attributeChangedCallbackCalled=true;
	}

}