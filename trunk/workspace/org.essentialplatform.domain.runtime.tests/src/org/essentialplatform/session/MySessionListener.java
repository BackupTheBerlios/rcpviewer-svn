/**
 * 
 */
package org.essentialplatform.session;

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