package de.berlios.rcpviewer.session;

/**
* Objects that require an {@link IWrapper} should implement this interface;
* dependency injection will take care of the rest.
* 
* TODO: probably replace with a @Injected annotation.
*/
public interface IWrapperAware {

	void setWrapper(IWrapper wrapper);
}
