/**
 * 
 */
package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.runtime.shared.domain.event.DomainObjectEvent;
import org.essentialplatform.runtime.shared.domain.event.IDomainObjectListener;

class MyDomainObjectListener implements IDomainObjectListener {
	boolean persistedCallbackCalled = false;
	DomainObjectEvent event;
	public void persisted(DomainObjectEvent event) {
		persistedCallbackCalled=true;
		this.event = event;
	}

}