/**
 * 
 */
package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.runtime.domain.event.DomainObjectOperationEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectOperationEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectOperationListener;

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