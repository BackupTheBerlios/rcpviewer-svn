/**
 * 
 */
package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.runtime.session.ISessionListener;
import org.essentialplatform.runtime.session.SessionObjectEvent;

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