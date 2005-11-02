/**
 * 
 */
package org.essentialplatform.session;

class MyDomainObjectOperationListener implements IDomainObjectOperationListener {
	
	boolean operationInvokedCallbackCalled = false;
	DomainObjectOperationEvent event;
	public void operationInvoked(DomainObjectOperationEvent event) {
		operationInvokedCallbackCalled=true;
		this.event = event;
	}

	boolean operationPrerequisitesChangedCallbackCalled = false;
	ExtendedDomainObjectOperationEvent extendedEvent;
	
	public void operationPrerequisitesChanged(ExtendedDomainObjectOperationEvent extendedEvent) {
		operationPrerequisitesChangedCallbackCalled=true;
		this.extendedEvent = extendedEvent;
	}


}