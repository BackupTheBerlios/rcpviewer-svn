package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.progmodel.IProgrammingModel;

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
