/**
 * 
 */
package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.runtime.session.event.ISessionListener;
import org.essentialplatform.runtime.session.event.SessionObjectEvent;

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