/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectAttributeListener implements IDomainObjectAttributeListener {
	
	boolean attributeChangedCallbackCalled = false;
	DomainObjectAttributeEvent event;
	public void attributeChanged(DomainObjectAttributeEvent event) {
		attributeChangedCallbackCalled=true;
		this.event = event;
	}

	boolean attributePrerequisitesChangedCallbackCalled = false;
	ExtendedDomainObjectAttributeEvent extendedEvent;
	public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent extendedEvent) {
		attributePrerequisitesChangedCallbackCalled=true;
		this.extendedEvent = extendedEvent;
	}

}