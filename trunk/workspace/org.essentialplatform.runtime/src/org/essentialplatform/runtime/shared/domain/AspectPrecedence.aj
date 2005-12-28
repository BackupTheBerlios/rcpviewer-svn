package org.essentialplatform.runtime.shared.domain;


public aspect AspectPrecedence {

	declare precedence: TracingAspect, org.essentialplatform.runtime.domain.NotifyListenersAspect;
}
