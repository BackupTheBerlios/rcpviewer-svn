package de.berlios.rcpviewer.metamodel.impl;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.*;

/**
 * Registers {@link DomainClass}es as they are loaded with the
 * {@link DomainClassRegistry}.
 * 
 * TODO: not sure if this is needed because the DomainClassRegistry itself 
 * might not be needed.  However, this would make a nice <T> aspect for 
 * registration of any sort of class.
 */
aspect RegisterDomainClassAspect {

	/**
	 * pick out explicit calls to Class.forName().
	 * The !within is needed to prevent infinite loops
	 */ 
	pointcut classForName():
		call(Class Class.forName(..)) && 
		!within(*..RegisterDomainObjectAspect);

	/**
	 * pick out classloading (subclasses of) pojo annotated with {@link @ADomainObject}
	 */
	public pointcut loadPojo(): 
		staticinitialization(@Domain *);

	
	/**
	 * register pojo's class
	 */
	after() returning(Class javaClass): classForName() {
		getDomainClassRegistry().register(javaClass);
	}
	

	/**
	 * register pojo's class if necessary (its class may not have been loaded yet).
     *	
	 * trying to support implicit registration using staticinitialization, but didn't
	 * seem to work.
	 */
	after(): loadPojo() {
		Class javaClass = thisJoinPointStaticPart.getSignature().getDeclaringType();
		DomainClass domainClass = getDomainClassRegistry().register(javaClass);
	}


	public DomainClassRegistry getDomainClassRegistry() {
		return DomainClassRegistry.instance();
	}

}