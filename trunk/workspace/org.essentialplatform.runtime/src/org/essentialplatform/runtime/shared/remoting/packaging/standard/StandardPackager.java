package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.core.domain.filters.OneToOneReferenceFilter;
import org.essentialplatform.core.domain.filters.CollectionReferenceFilter;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.remoting.packaging.AbstractPackager;

public class StandardPackager extends AbstractPackager {

	@Override
	public Object pack(IPojo pojo) {
		StandardPackage standardPackage = new StandardPackage();
		IDomainObject dObj = pojo.domainObject();
		final IDomainClass dc = dObj.getDomainClass();
		standardPackage.setClass(dc);
		standardPackage.setHandle(dObj.getHandle());
		for(IAttribute iAttrib: dc.iAttributes()) {
			standardPackage.addAttribute(dObj.getAttribute(iAttrib));
		}
		for(IReference iReference: dc.iReferences(new OneToOneReferenceFilter())) {
			standardPackage.addOneToOneReference(dObj.getOneToOneReference(iReference));
		}
		for(IReference iReference: dc.iReferences(new CollectionReferenceFilter())) {
			standardPackage.addCollectionReference(dObj.getCollectionReference(iReference));
		}
		return standardPackage;
	}

	@Override
	public IPojo unpack(Object pojo) {
		StandardPackage standardPackage = (StandardPackage)pojo;
		// UP TO HERE...
		// NEED TO STORE THE DOMAIN CLASS
//		IDomainObject dObj = pojo.domainObject();
//		final IDomainClass dc = dObj.getDomainClass();
//		for(IAttribute iAttrib: dc.iAttributes()) {
//			standardPackage.addAttribute(dObj.getAttribute(iAttrib));
//		}
//		return standardPackage;
		return null;
	}

}
