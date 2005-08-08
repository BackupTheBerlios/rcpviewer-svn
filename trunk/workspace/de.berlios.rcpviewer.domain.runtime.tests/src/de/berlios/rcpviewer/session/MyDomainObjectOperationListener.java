/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectOperationListener implements IDomainObjectOperationListener {
	
	boolean operationInvokedCallbackCalled = false;
	DomainObjectOperationEvent event;
	public void operationInvoked(DomainObjectOperationEvent event) {
		operationInvokedCallbackCalled=true;
		this.event = event;
	}

}