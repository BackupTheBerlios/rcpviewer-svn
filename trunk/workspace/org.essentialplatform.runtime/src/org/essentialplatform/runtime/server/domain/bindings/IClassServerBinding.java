/**
 * 
 */
package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.deployment.IClassBinding;
import org.essentialplatform.runtime.server.persistence.IPersistenceIdAssigner;

public interface IClassServerBinding<T> extends IClassBinding<T>, IPersistenceIdAssigner {
	
}