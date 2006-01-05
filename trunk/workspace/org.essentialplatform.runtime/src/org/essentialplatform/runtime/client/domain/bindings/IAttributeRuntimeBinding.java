package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.deployment.IAttributeBinding;

public interface IAttributeRuntimeBinding extends IAttributeBinding {
	Object invokeAccessor(Object pojo);
	void invokeMutator(Object pojo, Object newValue);
}
