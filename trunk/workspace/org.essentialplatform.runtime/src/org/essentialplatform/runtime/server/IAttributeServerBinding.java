package org.essentialplatform.runtime.server;

import org.essentialplatform.core.deployment.IAttributeBinding;

public interface IAttributeServerBinding extends IAttributeBinding {
	Object invokeAccessor(Object pojo);
	void invokeMutator(Object pojo, Object newValue);
}
