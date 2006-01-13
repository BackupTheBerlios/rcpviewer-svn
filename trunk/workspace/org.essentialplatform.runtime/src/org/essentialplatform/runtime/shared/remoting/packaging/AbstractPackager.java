package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.domain.IPojo;

public abstract class AbstractPackager implements IPackager {

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#pack(org.essentialplatform.runtime.shared.domain.IPojo)
	 */
	public abstract Object pack(IPojo pojo);

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPackager#unpack(java.lang.Object)
	 */
	public abstract IPojo unpack(Object pojo);

}
