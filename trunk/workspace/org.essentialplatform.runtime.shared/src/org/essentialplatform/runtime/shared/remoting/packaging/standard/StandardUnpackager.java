package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import org.apache.log4j.Logger;
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
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.AbstractUnpackager;
import org.essentialplatform.runtime.shared.remoting.packaging.IHandlePackage;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ISessionBindingPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public class StandardUnpackager extends AbstractUnpackager {

	private static Logger LOG = Logger.getLogger(StandardUnpackager.class);
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}
	


	@Override
	public SessionBinding unpackSessionBinding(ISessionBindingPackage sessionBindingPackage) {
		return sessionBindingPackage.unpackSessionBinding();
	}
	
	@Override
	public Handle unpackHandle(IHandlePackage handlePackage) {
		return handlePackage.unpackHandle();
	}
	
	@Override
	public void merge(IDomainObject domainObject, IPojoPackage iPojoPackage, IHandleMap handleMap) {
		StandardPojoPackage pojoPackage = (StandardPojoPackage)iPojoPackage;

		SessionBinding sessionBinding = pojoPackage.unpackSessionBinding();
		
		PersistState persistState = pojoPackage.unpackPersistState();
		ResolveState resolveState = pojoPackage.unpackResolveState();

		Handle handle = pojoPackage.unpackHandle();
		domainObject.assignHandle(handle);
		Class<?> javaClass = handle.getJavaClass();
		Domain domain = Domain.instance(sessionBinding.getDomainName());
		IDomainClass dc = domain.lookup(javaClass);
		
		// update state (this is a merge if the dObj already existed).
		for(IAttribute iAttrib: dc.iAttributes()) {
			pojoPackage.unpackAttribute(domainObject.getAttribute(iAttrib));
		}
		for(IReference iReference: dc.iReferences(new OneToOneReferenceFilter())) {
			pojoPackage.unpackOneToOneReference(domainObject.getOneToOneReference(iReference), handleMap);
		}
		for(IReference iReference: dc.iReferences(new CollectionReferenceFilter())) {
			pojoPackage.unpackCollectionReference(domainObject.getCollectionReference(iReference), handleMap);
		}
	}

	@Override
	public void optimize(IMarshalling marshalling) {
		if (marshalling instanceof XStreamMarshalling) {
			new StandardXMarshallingOptimizer().optimize(marshalling);
		}
	}


}
