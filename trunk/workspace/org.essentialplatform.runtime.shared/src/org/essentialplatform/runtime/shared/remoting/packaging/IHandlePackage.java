package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.domain.Handle;

/**
 * Represents a packaging that contains a {@link Handle}.
 * 
 * @author Dan Haywood
 */
public interface IHandlePackage {
	
	Handle unpackHandle();

}
