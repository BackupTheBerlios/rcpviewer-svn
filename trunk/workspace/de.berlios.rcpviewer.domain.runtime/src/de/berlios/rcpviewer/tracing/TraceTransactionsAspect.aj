/**
 * 
 */
package de.berlios.rcpviewer.tracing;

import org.apache.log4j.Logger;

public aspect TraceTransactionsAspect extends TracingAspect {

	private final static Logger LOG = Logger.getLogger(TraceTransactionsAspect.class);

	protected Logger getLogger() { return LOG; }
	
	protected pointcut traceMethods() : 
		execution(* de.berlios.rcpviewer.transaction..*.*(..)) &&
//		!execution(* Object+.toString()) &&
//		!execution(* Object+.equals(Object)) &&
//		!execution(int Object+.hashCode()) &&
		!execution(* Object.*(..))
		;

}
