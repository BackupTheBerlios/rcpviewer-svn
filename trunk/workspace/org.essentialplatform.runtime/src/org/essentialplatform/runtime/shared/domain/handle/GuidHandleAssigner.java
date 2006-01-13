package org.essentialplatform.runtime.shared.domain.handle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

/**
 * Generates globally unique {@link Handle}s (unique across all JVMs).
 * 
 * @author Dan Haywood
 */
public final class GuidHandleAssigner extends AbstractHandleAssigner {

	public GuidHandleAssigner() {
		super();
	}

	public GuidHandleAssigner(IHandleAssigner nextAssigner) {
		super(nextAssigner);
	}

	@Override
	protected <T> Handle doGenerateHandleFor(IDomainObject<T> domainObject) {
		IDomainClassRuntimeBinding<T> binding = (IDomainClassRuntimeBinding<T>)domainObject.getDomainClass().getBinding();
		Class<?> objectClass = binding.getJavaClass();
		return new Handle(objectClass, UUID.randomUUID());
	}

}
