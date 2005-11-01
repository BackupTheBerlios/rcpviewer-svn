package org.essentialplatform.session;


public aspect AspectPrecedence {

	declare precedence: TracingAspect, NotifyListenersAspect;
}
