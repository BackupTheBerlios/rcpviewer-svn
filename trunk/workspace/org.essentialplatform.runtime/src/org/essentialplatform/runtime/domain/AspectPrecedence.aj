package org.essentialplatform.runtime.domain;


public aspect AspectPrecedence {

	declare precedence: TracingAspect, org.essentialplatform.runtime.domain.NotifyListenersAspect;
}
