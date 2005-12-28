package org.essentialplatform.runtime.shared.persistence;


/**
 * Objects that require an {@link IObjectStore} should implement this interface;
 * dependency injection will take care of the rest.
 *
 * <p>
 * TODO: probably replace with an Injected annotation.
 * 
 * @author Dan Haywood
 */
public interface IObjectStoreAware {

	void setObjectStore(IObjectStore objectStore);
}
