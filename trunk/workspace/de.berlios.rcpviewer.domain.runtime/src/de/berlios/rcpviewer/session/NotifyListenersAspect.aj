/**
 * 
 */
package de.berlios.rcpviewer.session;

import org.aspectj.lang.Signature;
import org.eclipse.emf.ecore.EAttribute;
import org.aspectj.lang.JoinPoint;

public aspect NotifyListenersAspect extends PojoAspect {


	/**
	 * If we are able to locate the {@link de.berlios.rcpviewer.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this attribute.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFfeature}s of the session.  That's because
	 * a prerequisite of an operation or an attribute might become satisfied
	 * (or no longer satisfied) as a result of this change.
	 * 
	 * <p>
	 * <n>Implementation notes</n>: informing all observed features seems rather
	 * crude.  An alternative design and possibly preferable approach would be 
	 * to wait until the current "workgroup" (as defined by the transaction 
	 * aspect) has completed.   
	 */
	after(IPojo pojo, Object newValue): changeAttributeOnPojo(pojo, newValue) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return;
		}
		IDomainObject.IAttribute attribute = getAttributeFor(domainObject, thisJoinPointStaticPart);
		if (attribute != null) {
			attribute.notifyAttributeListeners(newValue);
		}
		
		// rather crude, see comments above.
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}

	}


}
