package org.essentialplatform.runtime.client.remoting.packaging.standard;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.core.domain.filters.CollectionReferenceFilter;
import org.essentialplatform.core.domain.filters.OneToOneReferenceFilter;
import org.essentialplatform.runtime.client.remoting.packaging.AbstractPackager;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardTransactionPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardXMarshallingOptimizer;

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
		for(IPojo pojo: transaction.getEnlistedPojos()) {
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


	public void optimize(IMarshalling marshalling) {
		if (marshalling instanceof XStreamMarshalling) {
			new StandardXMarshallingOptimizer().optimize(marshalling);
		}
	}


}
