package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;

/**
 * Adapter for implementations of {@link IPackager}.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractPackager implements IPackager {

	protected AbstractPackager(IHandleMap handleMap) {
		_handleMap = handleMap;
	}
	
	private final IHandleMap _handleMap;
	public IHandleMap getHandleMap() {
		return _handleMap;
	}

	
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#pack(org.essentialplatform.runtime.shared.domain.IPojo)
	 */
	public abstract Object pack(IPojo pojo);

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#unpack(java.lang.Object)
	 */
	public abstract IPojo unpack(Object pojo);

}
