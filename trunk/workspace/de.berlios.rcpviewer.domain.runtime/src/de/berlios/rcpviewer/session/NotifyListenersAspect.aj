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
	 * In addition, notify all {@link IObservedFeature}s of the session.  That's because
	 * a prerequisite of an operation or an attribute might become satisfied
	 * (or no longer satisfied) as a result of this change.
	 * 
	 * <p>
	 * <n>Implementation notes</n>: informing all observed features seems rather
	 * crude.  An alternative design and possibly preferable approach would be 
	 * to wait until the current "workgroup" (as defined by the transaction 
	 * aspect) has completed.   
	 */
	after(IPojo pojo, Object newValue): changingAttributeOnPojo(pojo, newValue) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return;
		}
		
		IDomainObject.IAttribute attribute = getAttributeFor(domainObject, thisJoinPointStaticPart);
		if (attribute != null) {
			attribute.notifyListeners(newValue);
		}
		
		// rather crude, see comments above.
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}

	}


	/**
	 * If we are able to locate the {@link de.berlios.rcpviewer.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this 1:1 reference.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.  That's because
	 * a prerequisite of an operation or an attribute might become satisfied
	 * (or no longer satisfied) as a result of this change.
	 * 
	 * <p>
	 * <n>Implementation notes</n>: informing all observed features seems rather
	 * crude.  An alternative design and possibly preferable approach would be 
	 * to wait until the current "workgroup" (as defined by the transaction 
	 * aspect) has completed.   
	 */
	after(IPojo pojo, Object newReference): changingOneToOneReferenceOnPojo(pojo, newReference) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return;
		}
		
		IDomainObject.IOneToOneReference reference = getOneToOneReferenceFor(domainObject, thisJoinPointStaticPart);
		if (reference != null) {
			reference.notifyListeners(newReference);
		}
		
		// rather crude, see comments above.
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}
	}


	/**
	 * If we are able to locate the {@link de.berlios.rcpviewer.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this collection.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.
	 */
	after(IPojo pojo, Object addedObject): addingToCollectionOnPojo(pojo, addedObject) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return;
		}
		
		IDomainObject.ICollectionReference reference = getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
		if (reference != null) {
			reference.notifyListeners(addedObject, true);
		}
		
		// rather crude, see comments above.
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}
	}


	/**
	 * If we are able to locate the {@link de.berlios.rcpviewer.session.IDomainObject}
	 * wrapper for this pojo then get it to notify any listeners it has for
	 * this collection.
	 * 
	 * <p>
	 * In addition, notify all {@link IObservedFeature}s of the session.    
	 */
	after(IPojo pojo, Object removedObject): removingFromCollectionOnPojo(pojo, removedObject) { 
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return;
		}
		
		IDomainObject.ICollectionReference reference = getCollectionReferenceFor(domainObject, thisJoinPointStaticPart);
		if (reference != null) {
			reference.notifyListeners(removedObject, false);
		}
		
		// rather crude, see comments above.
		ISession session = domainObject.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}
	}
}
