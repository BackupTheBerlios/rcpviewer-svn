package org.essentialplatform.runtime.session;


public aspect AspectPrecedence {

	declare precedence: TracingAspect, org.essentialplatform.runtime.transaction.internal.NotifyListenersAspect;
}
