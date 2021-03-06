package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.domain.*;
import de.berlios.rcpviewer.progmodel.standard.*;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.Session;
import de.berlios.rcpviewer.session.*;

/**
 * A <i>perthis</i> aspect that wraps each POJO and creates a corresponding 
 * {@link IDomainObject}.
 * 
 * TODO: at some point should be able to factor out advice into an abstract
 * aspect in the progmodel.impl package.
 * 
 * @author Dan Haywood
 */
public aspect DomainAspect<T> perthis(interactWithPojo(DomainMarker)){
	
	private IDomainObject<T> domainObject;
	public IDomainObject<T> getDomainObject() {
		return domainObject;
	}

	// THIS STUFF IS COMMENTED OUT SINCE NOT SURE IF ANNOTATIONS ARE TRIPPING US UP...
//	/**
//	 * pick out instantiating of a Pojo annotated with @InDomain
//	 */
//	public pointcut instantiatePojo(Object pojo): 
//		execution((@InDomain *..*).new(..)) && this(pojo);
//
//	/**
//	 * pick out invoking any public method on a Pojo annotated with @InDomain.
//	 */
//	public pointcut invokePublicMethodOnPojo(Object pojo): 
//		execution(public * (@InDomain *..*).*(..)) && this(pojo);
//
//	/**
//	 * TODO: want to use as perthis pointcut, but doesn't seem to work?
//	 */
//	public pointcut interactWithPojo(Object pojo): 
//		(invokePublicMethodOnPojo(pojo) || instantiatePojo(pojo) ) ;
//	

	/**
	 * pick out instantiating of a Pojo annotated with @InDomain.
	 */
	public pointcut instantiatePojo(DomainMarker pojo): 
		execution(*..DomainMarker+.new(..)) && this(pojo);

	/**
	 * pick out invoking any public method on a Pojo annotated with @InDomain.
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
		execution(* (@InDomain *..*).action*(..)) && this(pojo);
	
	/**
	 * Picks out a call to a setter
	 */
	public pointcut invokeSetterOnPojo(Object pojo, String postValue): 
		execution(* (@InDomain *..*).set*(String)) && this(pojo) && args(postValue);
	
	/**
	 * Picks out any modification of a field on a {@link DomainObject}
	 */
	public pointcut modifyDomainObjectFieldOnPojo(Object pojo, Object postValue) :
		args(postValue) && this(pojo) && set(* (@InDomain *..*).*);

	/**
	 * register {@link DomainObject}'s class if necessary (its class may not have been loaded yet),
	 * and store class in this (perthis) aspect.
	 */
	after(DomainMarker pojo): instantiatePojo(pojo) {
		doInstantiatePojo(pojo, (Class<T>)pojo.getClass());
	}
	
	/**
	 * Should be V pojo rather than Object pojo, but AJDT1.2m3 compiler is 
	 * throwing null pointer exception.
	 */
	private void doInstantiatePojo(DomainMarker pojo, Class<T> pojoClass) {
		IDomainClass<T> domainClass = Domain.instance().lookup(pojoClass);
		this.domainObject = new DomainObject<T>(domainClass, (T)pojo, getSession());
	}
	
	
	private ISession session;
	/**
	 * TODO: get Session via dependency injection.  For now, just returning singleton.
	 */
	public ISession getSession() {
		// return session;
		return Session.instance();
	}
	public void setSession(ISession session) {
		if (this.session != null && this.session != session) {
			throw new IllegalArgumentException("Session already set.");
		}
		this.session = session;
	}

}
