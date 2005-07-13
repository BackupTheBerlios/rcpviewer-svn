package de.berlios.rcpviewer.session;


public aspect AspectPrecedence {

	declare precedence: TracingAspect, NotifyListenersAspect;
}
