package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.domain.IPojo;

/**
 * Package and unpackage pojos so that they can be marshalled.
 * 
 * @author Dan Haywood
 */
public interface IPackager {
	
	Object pack(IPojo pojo);

	IPojo unpack(Object pojo);
}
