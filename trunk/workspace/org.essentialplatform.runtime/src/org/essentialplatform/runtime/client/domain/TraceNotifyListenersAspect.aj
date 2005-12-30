package org.essentialplatform.runtime.client.domain;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.shared.tracing.TracingAspect;

public aspect TraceNotifyListenersAspect extends TracingAspect {

	private final static Logger LOG = Logger.getLogger(TraceNotifyListenersAspect.class);

	protected Logger getLogger() { return LOG; }
	
	protected pointcut traceMethods() : 
		execution(* org.essentialplatform.runtime.shared.transaction.internal.NotifyListenersAspect.*(..)) &&
		!execution(* Object.*(..))
		;

}
