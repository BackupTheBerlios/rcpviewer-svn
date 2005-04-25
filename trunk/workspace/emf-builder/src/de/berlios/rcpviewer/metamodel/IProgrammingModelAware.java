package de.berlios.rcpviewer.metamodel;

/**
 * Objects that require an {@link IProgrammingModel} should implement this interface;
 * dependency injection will take care of the rest.
 * 
 * TODO: probably replace with a @Injected annotation.
 *
 */
public interface IProgrammingModelAware {

	void setProgrammingModel(IProgrammingModel programmingModel);
}
