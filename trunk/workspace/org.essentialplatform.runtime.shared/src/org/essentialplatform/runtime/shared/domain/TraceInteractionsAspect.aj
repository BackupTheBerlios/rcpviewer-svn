package org.essentialplatform.runtime.shared.domain;

import org.apache.log4j.Logger;

import org.essentialplatform.runtime.shared.domain.InteractionsAspect;
import org.essentialplatform.runtime.shared.domain.IPojo;

public aspect TraceInteractionsAspect extends InteractionsAspect {

	private final static boolean ENABLED = false;
	
	private final static Logger LOG = Logger.getLogger(TraceInteractionsAspect.class);
	
	Object around(IPojo pojo): invokeGetterForAttributeOnPojo(pojo) && if(ENABLED){
		LOG.debug("getter attrib >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("getter attrib << " + thisJoinPoint.toShortString());
		}
	}
	
	Object around(IPojo pojo, Object newValue): changingAttributeOnPojo(pojo, newValue)  && if(ENABLED){
		LOG.debug("changing attrib >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo, newValue);
		} finally {
			LOG.debug("changing attrib    " + thisJoinPoint.toShortString());
		}
	}
	

	Object around(IPojo pojo): invokeSetterForAttributeOnPojo(pojo, Object) && if(ENABLED) {
		LOG.debug("setter attrib >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("setter attrib    " + thisJoinPoint.toShortString());
		}
	}
	
	Object around(IPojo pojo): invokeSetterForOneToOneReferenceOnPojo(pojo, Object) && if(ENABLED) {
		LOG.debug("setter 1:1ref >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("setter 1:1 ref   " + thisJoinPoint.toShortString());
		}
	}
	
	Object around(IPojo pojo): invokeAssociatorForOneToOneReferenceOnPojo(pojo, IPojo) && if(ENABLED){
		LOG.debug("associator >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("associator    " + thisJoinPoint.toShortString());
		}
	}
	
	Object around(IPojo pojo): invokeDissociatorForOneToOneReferenceOnPojo(pojo, IPojo) && if(ENABLED) {
		LOG.debug("dissociator >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("dissociator  " + thisJoinPoint.toShortString());
		}
	}
	
	Object around(IPojo pojo): invokeAddToCollectionOnPojo(pojo, IPojo) && if(ENABLED) {
		LOG.debug("addTo >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("addTo    " + thisJoinPoint.toShortString());
		}
	}
	
	Object around(IPojo pojo): invokeRemoveFromCollectionOnPojo(pojo, IPojo) && if(ENABLED) {
		LOG.debug("removeFrom >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("removeFrom    " + thisJoinPoint.toShortString());
		}
	}
	
	Object around(IPojo pojo): invokeOperationOnPojo(pojo) && if(ENABLED) {
		LOG.debug("operation >> " + thisJoinPoint.toLongString());
		try {
			return proceed(pojo);
		} finally {
			LOG.debug("operation    " + thisJoinPoint.toShortString());
		}
	}
	
	
}
