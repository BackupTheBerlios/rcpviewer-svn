package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference;

public final class StandardPackage {
	private Map<String, StandardAttributeData> _attributeDataByName = new HashMap<String, StandardAttributeData>();
	private Map<String, StandardOneToOneReferenceData> _oneToOneReferenceDataByName = new HashMap<String, StandardOneToOneReferenceData>();
	private Map<String, StandardCollectionReferenceData> _collectionReferenceDataByName = new HashMap<String, StandardCollectionReferenceData>();
	private String _class;
	private Handle _persistenceId;
	
	void addAttribute(IObjectAttribute attribute) {
		final String name = attribute.getAttribute().getName();
		StandardAttributeData data = new StandardAttributeData(name, attribute.get());
		_attributeDataByName.put(name, data);
	}

	public void setClass(IDomainClass dc) {
		_class = dc.getEClass().getInstanceClassName();
	}

	public void setHandle(Handle persistenceId) {
		_persistenceId = persistenceId;
	}

	public void addOneToOneReference(IObjectOneToOneReference reference) {
		final String name = reference.getReference().getName();
		StandardOneToOneReferenceData data = new StandardOneToOneReferenceData(name, reference.getDomainObject().getHandle());
		_oneToOneReferenceDataByName.put(name, data);
	}

	public void addCollectionReference(IObjectCollectionReference reference) {
		final String name = reference.getReference().getName();
		StandardCollectionReferenceData data = new StandardCollectionReferenceData(name);
		for(IDomainObject<?> domainObjectInCollection: reference.getCollection()) {
			data.addHandle(domainObjectInCollection.getHandle());
		}
		_collectionReferenceDataByName.put(name, data);
	}
	
}
