package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.core.domain.filters.CollectionReferenceFilter;
import org.essentialplatform.core.domain.filters.OneToOneReferenceFilter;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.remoting.packaging.AbstractPackager;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public class StandardPackager extends AbstractPackager {

	@Override
	public Object pack(IPojo pojo) {
		StandardPojoPackage standardPackage = new StandardPojoPackage();
		IDomainObject dObj = pojo.domainObject();
		final IDomainClass dc = dObj.getDomainClass();
		standardPackage.packHandle(dObj.getHandle()); // includes java class name
		standardPackage.packSessionBinding(dObj.getSessionBinding());
		standardPackage.packPersistState(dObj.getPersistState());
		standardPackage.packResolveState(dObj.getResolveState());
		for(IAttribute iAttrib: dc.iAttributes()) {
			standardPackage.packAttribute(dObj.getAttribute(iAttrib));
		}
		for(IReference iReference: dc.iReferences(new OneToOneReferenceFilter())) {
			standardPackage.packOneToOneReference(dObj.getOneToOneReference(iReference));
		}
		for(IReference iReference: dc.iReferences(new CollectionReferenceFilter())) {
			standardPackage.packCollectionReference(dObj.getCollectionReference(iReference));
		}
		return standardPackage;
	}

	@Override
	public SessionBinding unpackSessionBinding(Object packedPojo) {
		StandardPojoPackage standardPackage = (StandardPojoPackage)packedPojo;
		return standardPackage.unpackSessionBinding();
	}
	
	@Override
	public Handle unpackHandle(Object packedPojo) {
		StandardPojoPackage standardPackage = (StandardPojoPackage)packedPojo;
		return standardPackage.unpackHandle();
	}
	
	@Override
	public void merge(IDomainObject domainObject, IHandleMap handleMap, Object packedPojo) {
		StandardPojoPackage standardPackage = (StandardPojoPackage)packedPojo;

		SessionBinding sessionBinding = standardPackage.unpackSessionBinding();
		
		PersistState persistState = standardPackage.unpackPersistState();
		ResolveState resolveState = standardPackage.unpackResolveState();

		Handle handle = standardPackage.unpackHandle();
		Class<?> javaClass = handle.getJavaClass();
		IDomainClass dc = Domain.lookupAny(javaClass);
		
		// update state (this is a merge if the dObj already existed).
		for(IAttribute iAttrib: dc.iAttributes()) {
			standardPackage.unpackAttribute(domainObject.getAttribute(iAttrib));
		}
		for(IReference iReference: dc.iReferences(new OneToOneReferenceFilter())) {
			standardPackage.unpackOneToOneReference(domainObject.getOneToOneReference(iReference), handleMap);
		}
		for(IReference iReference: dc.iReferences(new CollectionReferenceFilter())) {
			standardPackage.unpackCollectionReference(domainObject.getCollectionReference(iReference), handleMap);
		}
	}
	
}
