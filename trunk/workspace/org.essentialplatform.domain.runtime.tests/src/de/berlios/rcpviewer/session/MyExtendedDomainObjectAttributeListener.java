/**
 * 
 */
package de.berlios.rcpviewer.session;

class MyExtendedDomainObjectAttributeListener implements IExtendedDomainObjectAttributeListener {
	boolean attributePrerequisitesChangedCallbackCalled = false;
	ExtendedDomainObjectAttributeEvent event;
	
	public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent event) {
		attributePrerequisitesChangedCallbackCalled=true;
		this.event = event;
	}

}