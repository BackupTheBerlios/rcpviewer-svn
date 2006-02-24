package org.essentialplatform.runtime.shared.domain.handle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

/**
 * Does nothing.
 * 
 * @author Dan Haywood
 */
public final class NoopHandleAssigner extends AbstractHandleAssigner {

	public NoopHandleAssigner() {
		super();
	}

	public NoopHandleAssigner(IHandleAssigner nextAssigner) {
		super(nextAssigner);
	}

	@Override
	protected <T> Handle doGenerateHandleFor(IDomainObject<T> domainObject) {
		return null;
	}

}
