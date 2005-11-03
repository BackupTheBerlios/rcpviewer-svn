/**
 * 
 */
package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.runtime.session.DomainObjectOperationEvent;
import org.essentialplatform.runtime.session.ExtendedDomainObjectOperationEvent;
import org.essentialplatform.runtime.session.IDomainObjectOperationListener;

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