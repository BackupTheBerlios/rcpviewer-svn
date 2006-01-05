package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.deployment.IAttributeBinding;

public interface IAttributeRuntimeBinding extends IAttributeBinding, IMemberRuntimeBinding {
	Object invokeAccessor(Object pojo);
	void invokeMutator(Object pojo, Object newValue);
}
