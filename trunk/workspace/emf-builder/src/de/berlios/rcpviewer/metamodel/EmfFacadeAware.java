package de.berlios.rcpviewer.metamodel;

/**
 * Objects that require an {@link EmfFacade} should implement this interface;
 * dependency injection will take care of the rest.
 * 
 * TODO: probably replace with a @Injected annotation.
 *
 * @author Dan Haywood
 */
public interface EmfFacadeAware {

	EmfFacade getEmfFacade();
	void setEmfFacade(EmfFacade emfFacade);
}
