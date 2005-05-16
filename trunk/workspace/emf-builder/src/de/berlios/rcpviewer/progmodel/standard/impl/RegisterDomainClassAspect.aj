package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.*;

/**
 * Registers {@link DomainClass}es as they are loaded with the
 * {@link MetaModel}.
 * 
 * TODO: at some point should be able to factor out advice into an abstract
 * aspect in the progmodel.impl package.
 * 
 * TODO: make into a <T> aspect.
 * 
 * @author Dan Haywood
 */
aspect RegisterDomainClassAspect {

	/**
	 * pick out explicit calls to Class.forName().
	 * The !within is needed to prevent infinite loops
	 */ 
	pointcut classForName():
		call(Class Class.forName(..)) && 
		!within(*..RegisterDomainObjectAspect);


//	/**
//	 * pick out instantiating a DomainClass
//	 * The !within is needed to prevent infinite loops
//
//   * - NOT WORKING YET...
//	 */ 
//	pointcut instantiateDomainClass(DomainClass domainClass):
//		call(*..DomainClass.new(..)) && target(domainClass) && 
//		!within(*..RegisterDomainObjectAspect) &&
//		!within(*..MetaModel);

	/**
	 * pick out classloading (subclasses of) pojo annotated with {@link @ADomainObject}
	 */
	public pointcut loadPojo(): 
		staticinitialization(@Domain *);

	
	/**
	 * register pojo's class
	 */
	after() returning(Class javaClass): classForName() {
		// HACK: make sure not an IAdapterFactory. 
		if (IAdapterFactory.class.isAssignableFrom(javaClass)) {
			return;
		}
		getMetaModel().lookup(javaClass);
	}
	
//
//  * - NOT WORKING YET...
//	after(DomainClass domainClass): instantiateDomainClass(domainClass) {
//		getMetaModel().register(domainClass.getJavaClass());
//	}
	

	/**
	 * register pojo's class if necessary (its class may not have been loaded yet).
     *	
	 * trying to support implicit registration using staticinitialization, but didn't
	 * seem to work.
	 */
	after(): loadPojo() {
		Class<?> javaClass = thisJoinPointStaticPart.getSignature().getDeclaringType();
		IDomainClass<?> domainClass = getMetaModel().lookup(javaClass);
	}


	public MetaModel getMetaModel() {
		return MetaModel.instance();
	}

}
