/**
 * 
 */
package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.runtime.server.persistence.IPersistenceIdAssigner;

public interface IClassServerBinding<T> extends IClassRuntimeBinding<T>, IPersistenceIdAssigner {
	
}