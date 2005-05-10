package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.*;

/**
 * A <i>perthis</i> aspect that wraps each POJO and creates a corresponding 
 * {@link IDomainObject}.
 * 
 * TODO: at some point should be able to factor out advice into an abstract
 * aspect in the progmodel.impl package.
 * 
 * @author Dan Haywood
 */
public aspect DomainAspect perthis(interactWithPojo(DomainMarker)){
	
	private IDomainObject domainObject;
	public IDomainObject getDomainObject() {
		return domainObject;
	}

	// THIS STUFF IS COMMENTED OUT SINCE NOT SURE IF ANNOTATIONS ARE TRIPPING US UP...
//	/**
//	 * pick out instantiating of a Pojo annotated with @Domain
//	 */
//	public pointcut instantiatePojo(Object pojo): 
//		execution((@Domain *..*).new(..)) && this(pojo);
//
//	/**
//	 * pick out invoking any public method on a Pojo annotated with @DomainObject.
//	 */
//	public pointcut invokePublicMethodOnPojo(Object pojo): 
//		execution(public * (@Domain *..*).*(..)) && this(pojo);
//
//	/**
//	 * TODO: want to use as perthis pointcut, but doesn't seem to work?
//	 */
//	public pointcut interactWithPojo(Object pojo): 
//		(invokePublicMethodOnPojo(pojo) || instantiatePojo(pojo) ) ;
//	

	/**
	 * pick out instantiating of a Pojo annotated with @DomainObject.
	 */
	public pointcut instantiatePojo(DomainMarker pojo): 
		execution(*..DomainMarker+.new(..)) && this(pojo);

	/**
	 * pick out invoking any public method on a Pojo annotated with @DomainObject.
	 */
	public pointcut invokePublicMethodOnPojo(DomainMarker pojo): 
		execution(public * *..DomainMarker+.*(..)) && this(pojo);

	/**
	 * perthis pointcut, but doesn't seem to work?
	 */
	public pointcut interactWithPojo(DomainMarker pojo): 
		(invokePublicMethodOnPojo(pojo) || instantiatePojo(pojo) ) ;
	
	

	/**
	 * Picks out a call to an action method
	 */
	public pointcut invokeActionOnPojo(Object pojo): 
		execution(* (@Domain *..*).action*(..)) && this(pojo);
	
	/**
	 * Picks out a call to a setter
	 */
	public pointcut invokeSetterOnPojo(Object pojo, String postValue): 
		execution(* (@Domain *..*).set*(String)) && this(pojo) && args(postValue);
	
	/**
	 * Picks out any modification of a field on a {@link DomainObject}
	 */
	public pointcut modifyDomainObjectFieldOnPojo(Object pojo, Object postValue) :
		args(postValue) && this(pojo) && set(* (@Domain *..*).*);

	/**
	 * register {@link DomainObject}'s class if necessary (its class may not have been loaded yet),
	 * and store class in this (perthis) aspect.
	 */
	after(Object pojo): instantiatePojo(pojo) {
		doInstantiatePojo(pojo, pojo.getClass());
	}
	
	/**
	 * Should be V pojo rather than Object pojo, but AJDT1.2m3 compiler is 
	 * throwing null pointer exception.
	 */
	private <V> void doInstantiatePojo(Object pojo, Class<V> pojoClass) {
        // we register rather than lookup just in case the class has not been loaded
		IDomainClass<V> domainClass = getMetaModel().register(pojoClass);
		this.domainObject = new DomainObject<V>(domainClass, (V)pojo);
	}
	
	
	public MetaModel getMetaModel() {
		return MetaModel.threadInstance();
	}

}
