/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyExtendedDomainObjectOperationListener implements IExtendedDomainObjectOperationListener {
	
	boolean operationPrerequisitesChangedCallbackCalled = false;
	ExtendedDomainObjectOperationEvent event;
	
	public void operationPrerequisitesChanged(ExtendedDomainObjectOperationEvent event) {
		operationPrerequisitesChangedCallbackCalled=true;
		this.event = event;
	}

}