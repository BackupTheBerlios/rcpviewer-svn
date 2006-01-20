package org.essentialplatform.runtime.shared.tracing;

import org.aspectj.lang.*;
import org.aspectj.lang.reflect.SourceLocation;

import org.apache.log4j.Logger;

public abstract aspect TracingAspect {

	declare precedence : TracingAspect, *;

	protected abstract Logger getLogger();
	protected abstract pointcut traceMethods(); 

	final pointcut traceMethodsExcludingAspect(): 
		traceMethods() && 
		!within(TracingAspect+);

	Object around(): traceMethodsExcludingAspect() {
		logEnter(thisJoinPointStaticPart);
		try {
			return proceed();
		} finally {
			logExit(thisJoinPointStaticPart);
		}
	}


	// ////////////////////////

	private StringBuffer buf = new StringBuffer();
	private final String INDENT = "                                                                                          ";
	int logs = 0;
	int depth = 0;

	protected void logEnter(JoinPoint.StaticPart jp) {
		trace('>', depth, jp);
		depth++;
	}

	protected void logExit(JoinPoint.StaticPart jp) {
		depth--;
		trace('<', depth, jp);
	}

	void trace(char decorator, int depth, JoinPoint.StaticPart jp) {
		SourceLocation loc = jp.getSourceLocation();

		buf.setLength(0);
		buf.append(INDENT.substring(0, Math.min(depth, INDENT.length())))
		   .append(decorator)
		   .append(" ")
		   .append(jp.getKind())
		   .append(" ")
		   .append(jp.getSignature().toShortString())
		   .append("  [")
		   .append(loc.getFileName())
		   .append(":")
		   .append(loc.getLine())
		   .append("]");

		getLogger().debug(buf.toString());
	}

}
