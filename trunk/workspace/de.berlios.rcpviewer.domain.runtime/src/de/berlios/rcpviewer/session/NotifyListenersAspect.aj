/**
 * 
 */
package de.berlios.rcpviewer.session;

import org.aspectj.lang.Signature;
import org.eclipse.emf.ecore.EAttribute;

public aspect NotifyListenersAspect extends PojoAspect {


	/**
	 * If we are able to locate the {@link de.berlios.rcpviewer.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this attribute.
	 */
	after(IPojo pojo, Object newValue): modifyAttributeOnPojo(pojo, newValue) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return;
		}
		Signature signature = thisJoinPointStaticPart.getSignature();
		String name = signature.getName();
		IDomainObject.IAttribute attribute = 
			domainObject.getAttribute(domainObject.getEAttributeNamed(name));
		attribute.notifyAttributeListeners(newValue);
	}


}
