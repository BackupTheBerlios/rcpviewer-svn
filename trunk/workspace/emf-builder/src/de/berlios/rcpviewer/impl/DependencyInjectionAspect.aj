package de.berlios.rcpviewer.impl;

import de.berlios.rcpviewer.metamodel.EmfFacade;
import de.berlios.rcpviewer.metamodel.EmfFacadeAware;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.impl.Wrapper;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionAware;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.IWrapperAware;

/**
 * TODO: convert to use the generic container approach used in DSFA
 * TODO: actually, a simpler design still would be to look for @Injected 
 *       attribute and provide by intercepting the getter 
 *       (type 3 DI, I think it's called)
 *       
 * @author Dan Haywood
 */
aspect DependencyInjectionAspect { 

	declare precedence: *, DependencyInjectionAspect;

	pointcut initializeEmfFacadeAware(EmfFacadeAware aware):
		execution(EmfFacadeAware+.new(..)) && 
		this(aware) &&
		!within(DependencyInjectionAspect);
	before(EmfFacadeAware aware): initializeEmfFacadeAware(aware) {
		aware.setEmfFacade(getEmfFacade());
	}

	pointcut initializeWrapperAware(IWrapperAware aware):
		execution(IWrapperAware+.new(..)) && 
		this(aware) &&
		!within(DependencyInjectionAspect);
	before(IWrapperAware aware): initializeWrapperAware(aware) {
		aware.setWrapper(getWrapper());
	}
	
	pointcut initializeObjectStoreAware(IObjectStoreAware aware):
		execution(IObjectStoreAware+.new(..)) && 
		this(aware) &&
		!within(DependencyInjectionAspect);
	before(IObjectStoreAware aware): initializeObjectStoreAware(aware) {
		aware.setObjectStore(getObjectStore());
	}

	pointcut initializeSessionAware(ISessionAware aware):
		execution(ISessionAware+.new(..)) && 
		this(aware) &&
		!within(DependencyInjectionAspect);
	before(ISessionAware aware): initializeSessionAware(aware) {
		aware.setSession(getSession());
	}

	/**
	 * TODO: configure via Spring
	 */
	{
		setEmfFacade(new EmfFacade());
		setWrapper(new Wrapper());
		setObjectStore(new InMemoryObjectStore());
		// TODO: currently not injecting because attempting to instantiate a
		// Session inside this aspect creates an aspectBound error....
		// setSession(Session.instance());
	}
		
	private EmfFacade emfFacade;
	public EmfFacade getEmfFacade() {
		return emfFacade;
	}
	public void setEmfFacade(EmfFacade emfFacade) {
		this.emfFacade = emfFacade;
	}
	
	private IWrapper wrapper;
	public IWrapper getWrapper() {
		return wrapper;
	}
	public void setWrapper(IWrapper wrapper) {
		this.wrapper = wrapper;
	}

	private IObjectStore objectStore;
	public IObjectStore getObjectStore() {
		return objectStore;
	}
	public void setObjectStore(IObjectStore objectStore) {
		this.objectStore = objectStore;
	}
	
	private ISession session;
	public ISession getSession() {
		return session;
	}
	public void setSession(ISession session) {
		this.session = session;
	}

}
