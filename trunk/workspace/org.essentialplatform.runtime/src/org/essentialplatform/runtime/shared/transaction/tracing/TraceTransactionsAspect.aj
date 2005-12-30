package org.essentialplatform.runtime.transaction.tracing;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.tracing.TracingAspect;

public aspect TraceTransactionsAspect extends TracingAspect {

	private final static Logger LOG = Logger.getLogger(TraceTransactionsAspect.class);

	protected Logger getLogger() { return LOG; }

	// disable while unit testing... messes up variables view :-(
	protected pointcut traceMethods(); 

//	protected pointcut traceMethods() : 
//		execution(* org.essentialplatform.transaction..*.*(..)) &&
//		!execution(* Object.*(..))
//		;

}
