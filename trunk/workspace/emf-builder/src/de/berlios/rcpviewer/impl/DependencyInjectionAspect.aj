package de.berlios.rcpviewer.impl;

import de.berlios.rcpviewer.metamodel.*;

/**
 * TODO: convert to use the generic container approach used in DSFA
 * TODO: actually, a simpler design still would be to look for @Injected 
 *       attribute and provide by intercepting the getter 
 *       (type 3 DI, I think it's called)
 */
aspect DependencyInjectionAspect 
	implements IProgrammingModelAware,
	           EmfFacadeAware { 

	declare precedence: *, DependencyInjectionAspect;

	pointcut initializeProgrammingModelAware(IProgrammingModelAware aware):
		execution(IProgrammingModelAware+.new(..)) && 
		this(aware) &&
		!within(DependencyInjectionAspect);
	
	before(IProgrammingModelAware aware): initializeProgrammingModelAware(aware) {
		aware.setProgrammingModel(getProgrammingModel());
	}
	
	pointcut initializeEmfFacadeAware(EmfFacadeAware aware):
		execution(EmfFacadeAware+.new(..)) && 
		this(aware) &&
		!within(DependencyInjectionAspect);
	
	before(EmfFacadeAware aware): initializeEmfFacadeAware(aware) {
		aware.setEmfFacade(getEmfFacade());
	}
	

	/**
	 * TODO: configure via Spring
	 */
	{
		setProgrammingModel(new ProgrammingModel());
		setEmfFacade(new EmfFacade());
	}
		
	private IProgrammingModel programmingModel;
	public IProgrammingModel getProgrammingModel() {
		return programmingModel;
	}
	public void setProgrammingModel(IProgrammingModel programmingModel) {
		this.programmingModel = programmingModel;
	}

	private EmfFacade emfFacade;
	public EmfFacade getEmfFacade() {
		return emfFacade;
	}
	public void setEmfFacade(EmfFacade emfFacade) {
		this.emfFacade = emfFacade;
	}

}
