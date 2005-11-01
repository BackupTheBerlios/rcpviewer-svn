package org.essentialplatform.session;

import org.apache.log4j.Logger;
import org.essentialplatform.tracing.TracingAspect;

public aspect TraceNotifyListenersAspect extends TracingAspect {

	private final static Logger LOG = Logger.getLogger(TraceNotifyListenersAspect.class);

	protected Logger getLogger() { return LOG; }
	
	protected pointcut traceMethods() : 
		execution(* org.essentialplatform.session.NotifyListenersAspect.*(..)) &&
		!execution(* Object.*(..))
		;

}
