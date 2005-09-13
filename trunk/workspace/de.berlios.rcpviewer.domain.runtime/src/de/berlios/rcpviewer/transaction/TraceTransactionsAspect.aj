package de.berlios.rcpviewer.transaction;

import org.apache.log4j.Logger;
import de.berlios.rcpviewer.tracing.TracingAspect;

public aspect TraceTransactionsAspect extends TracingAspect {

	private final static Logger LOG = Logger.getLogger(TraceTransactionsAspect.class);

	protected Logger getLogger() { return LOG; }

	// disable while unit testing... messes up variables view :-(
	protected pointcut traceMethods(); 

//	protected pointcut traceMethods() : 
//		execution(* de.berlios.rcpviewer.transaction..*.*(..)) &&
//		!execution(* Object.*(..))
//		;

}
