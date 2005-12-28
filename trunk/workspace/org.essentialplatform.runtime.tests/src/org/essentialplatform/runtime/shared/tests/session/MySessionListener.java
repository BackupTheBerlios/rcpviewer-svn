/**
 * 
 */
package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.runtime.shared.session.event.ISessionListener;
import org.essentialplatform.runtime.shared.session.event.SessionObjectEvent;

class MySessionListener implements ISessionListener {
	boolean attachedCallbackCalled = false;
	boolean detachedCallbackCalled = false;
	public void domainObjectAttached(SessionObjectEvent event) {
		attachedCallbackCalled=true;
	}
	public void domainObjectDetached(SessionObjectEvent event) {
		detachedCallbackCalled=true;
	}
}