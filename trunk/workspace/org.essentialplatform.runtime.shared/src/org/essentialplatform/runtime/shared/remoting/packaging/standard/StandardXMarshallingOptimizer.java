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
import org.essentialplatform.runtime.shared.remoting.packaging.IMarshallingOptimizer;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ISessionBindingPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public class StandardXMarshallingOptimizer implements IMarshallingOptimizer {

	private static Logger LOG = Logger.getLogger(StandardXMarshallingOptimizer.class);
	
	protected Logger getLogger() {
		return LOG;
	}
	

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IMarshallingOptimizer#optimize(org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling)
	 */
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
