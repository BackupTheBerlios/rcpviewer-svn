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
import org.essentialplatform.runtime.shared.remoting.packaging.AbstractPackager;
import org.essentialplatform.runtime.shared.remoting.packaging.IHandlePackage;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ISessionBindingPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.changes.AddToCollectionChange;
import org.essentialplatform.runtime.shared.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.shared.transaction.changes.DeletionChange;
import org.essentialplatform.runtime.shared.transaction.changes.IChange;
import org.essentialplatform.runtime.shared.transaction.changes.InstantiationChange;
import org.essentialplatform.runtime.shared.transaction.changes.OneToOneReferenceChange;
import org.essentialplatform.runtime.shared.transaction.changes.RemoveFromCollectionChange;

public class StandardPackager extends AbstractPackager {

	private static Logger LOG = Logger.getLogger(StandardPackager.class);
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}
	

	@Override
	public <V extends IPojoPackage> V pack(IPojo pojo) {
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
		return (V)standardPackage;
	}


	@Override
	protected <V extends ITransactionPackage> V doPack(ITransaction transaction) {
		final StandardTransactionPackage transactionPackage = new StandardTransactionPackage();
		for(ITransactable transactable: transaction.getEnlistedPojos()) {
			IPojo pojo = (IPojo)transactable;
			transactionPackage.addEnlistedPojo(pack(pojo));
		}
				
//		transaction.traverseCommittedChanges(new IChange.IVisitor() {
//			public void visit(IChange change) {
//				if (change instanceof InstantiationChange) {
//					InstantiationChange instantiationChange = 
//						(InstantiationChange)change;
//					
//					
//				} else if (change instanceof DeletionChange) {
//					DeletionChange deletionChange = (DeletionChange)change;
//
//					
//				} else if (change instanceof AttributeChange) {
//					AttributeChange attributeChange = (AttributeChange)change;
//					
//					
//				} else if (change instanceof OneToOneReferenceChange) {
//					OneToOneReferenceChange oneToOneReferenceChange =
//						(OneToOneReferenceChange)change;
//					
//					
//				} else if (change instanceof AddToCollectionChange) {
//					AddToCollectionChange addToCollectionChange =
//						(AddToCollectionChange)change;
//					
//					
//				} else if (change instanceof RemoveFromCollectionChange) {
//					RemoveFromCollectionChange removeFromCollectionChange =
//						(RemoveFromCollectionChange)change;
//					
//					
//				}
//			}
//		});
		return (V)transactionPackage;
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
		Class<?> javaClass = handle.getJavaClass();
		IDomainClass dc = Domain.lookupAny(javaClass);
		
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
			XStreamMarshalling xstreamMarshalling = (XStreamMarshalling)marshalling;
			xstreamMarshalling.alias("attribute", StandardAttributeData.class);
			xstreamMarshalling.alias("reference", StandardOneToOneReferenceData.class);
			xstreamMarshalling.alias("collection", StandardCollectionReferenceData.class);
			xstreamMarshalling.alias("pojo", StandardPojoPackage.class);
			xstreamMarshalling.alias("xactn", StandardTransactionPackage.class);
		}
		
	}


}
