/**
 * 
 */
package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.runtime.domain.event.DomainObjectEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectListener;

class MyDomainObjectListener implements IDomainObjectListener {
	boolean persistedCallbackCalled = false;
	DomainObjectEvent event;
	public void persisted(DomainObjectEvent event) {
		persistedCallbackCalled=true;
		this.event = event;
	}

}