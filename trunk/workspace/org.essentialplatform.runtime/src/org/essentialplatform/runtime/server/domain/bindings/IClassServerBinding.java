/**
 * 
 */
package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.server.persistence.IPersistenceIdAssigner;
import org.essentialplatform.runtime.shared.domain.bindings.IClassRuntimeBinding;

public interface IClassServerBinding<T> extends IClassRuntimeBinding<T>, IPersistenceIdAssigner {
	
}