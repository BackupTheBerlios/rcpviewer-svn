/**
 * 
 */
package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.session.PojoAspect;

public aspect TracingAspect extends PojoAspect {

	private static boolean ENABLED = false;
	
	/**
	 * Testing only.
	 */
	Object around(IPojo pojo): invokeOperationOnPojo(pojo) && if(ENABLED) {
		System.out.println("IPojoAspect: " + thisJoinPoint);
		IDomainObject domainObject = pojo.getDomainObject();
		try {
			System.out.println("domainObject = " + domainObject);
			return proceed(pojo);
		} finally {
			System.out.println("IPojoAspect: " + thisJoinPoint + ": exit");
		}
	}

	
	/**
	 * Testing only.
	 */
	Object around(IPojo pojo): instantiatePojo(pojo) && if(ENABLED) {
		System.out.println("IPojoAspect: " + thisJoinPoint + ": enter");
		try {
			return proceed(pojo);
		} finally {
			System.out.println("IPojoAspect: " + thisJoinPoint + ": exit, instantiated " + pojo);
		}
	}


}
