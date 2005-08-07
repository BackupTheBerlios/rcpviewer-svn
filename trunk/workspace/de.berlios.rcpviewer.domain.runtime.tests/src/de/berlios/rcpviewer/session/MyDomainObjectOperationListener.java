/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyDomainObjectOperationListener implements IDomainObjectOperationListener {
	
	boolean operationInvokedCallbackCalled = false;
	public void operationInvoked(DomainObjectOperationEvent event) {
		operationInvokedCallbackCalled=true;
	}

}