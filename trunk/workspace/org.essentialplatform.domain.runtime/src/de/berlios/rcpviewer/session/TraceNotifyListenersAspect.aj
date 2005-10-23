package de.berlios.rcpviewer.session;

import org.apache.log4j.Logger;
import de.berlios.rcpviewer.tracing.TracingAspect;

public aspect TraceNotifyListenersAspect extends TracingAspect {

	private final static Logger LOG = Logger.getLogger(TraceNotifyListenersAspect.class);

	protected Logger getLogger() { return LOG; }
	
	protected pointcut traceMethods() : 
		execution(* de.berlios.rcpviewer.session.NotifyListenersAspect.*(..)) &&
		!execution(* Object.*(..))
		;

}
