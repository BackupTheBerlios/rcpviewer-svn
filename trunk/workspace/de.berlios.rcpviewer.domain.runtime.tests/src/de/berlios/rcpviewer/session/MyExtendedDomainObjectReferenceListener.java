/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyExtendedDomainObjectReferenceListener implements IExtendedDomainObjectReferenceListener {

	boolean referencePrerequisitesChangedCallbackCalled = false;
	ExtendedDomainObjectReferenceEvent event;
	
	public void referencePrerequisitesChanged(ExtendedDomainObjectReferenceEvent event) {
		referencePrerequisitesChangedCallbackCalled=true;
		this.event = event;
	}


}