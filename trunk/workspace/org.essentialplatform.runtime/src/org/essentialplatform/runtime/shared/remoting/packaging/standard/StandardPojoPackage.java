package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.remoting.packaging.PackagingException;
import org.essentialplatform.runtime.shared.session.SessionBinding;

final class StandardPojoPackage {

	/////////////////////////////////////////////////////////////////////////
	// Handle
	/////////////////////////////////////////////////////////////////////////
	
	private Handle _handle;
	void packHandle(Handle handle) {
		_handle = handle;
	}
	Handle unpackHandle() {
		return _handle;
	}

	/////////////////////////////////////////////////////////////////////////
	// SessionBinding
	/////////////////////////////////////////////////////////////////////////
	
	private SessionBinding _sessionBinding;
	void packSessionBinding(SessionBinding sessionBinding) {
		_sessionBinding = sessionBinding;
	}
	SessionBinding unpackSessionBinding() {
		return _sessionBinding;
	}

	/////////////////////////////////////////////////////////////////////////
	// PersistState
	/////////////////////////////////////////////////////////////////////////

	private PersistState _persistState;
	void packPersistState(PersistState persistState) {
		_persistState = persistState;
	}
	PersistState unpackPersistState() {
		return _persistState;
	}

	/////////////////////////////////////////////////////////////////////////
	// ResolveState
	/////////////////////////////////////////////////////////////////////////

	private ResolveState _resolveState;
	void packResolveState(ResolveState resolveState) {
		_resolveState = resolveState;
	}
	ResolveState unpackResolveState() {
		return _resolveState;
	}

	/////////////////////////////////////////////////////////////////////////
	// Attributes
	/////////////////////////////////////////////////////////////////////////

	private Map<String, StandardAttributeData> _attributeDataByName = new HashMap<String, StandardAttributeData>();
	void packAttribute(IObjectAttribute attribute) {
		final String name = attribute.getAttribute().getName();
		StandardAttributeData data = new StandardAttributeData(name, attribute.get());
		_attributeDataByName.put(name, data);
	}
	void unpackAttribute(IObjectAttribute attributeToUpdate) {
		final String name = attributeToUpdate.getAttribute().getName();
		StandardAttributeData data = _attributeDataByName.get(name);
		attributeToUpdate.set(data.getValue());
	}

	/////////////////////////////////////////////////////////////////////////
	// OneToOneReferences
	/////////////////////////////////////////////////////////////////////////
	
	private Map<String, StandardOneToOneReferenceData> _oneToOneReferenceDataByName = new HashMap<String, StandardOneToOneReferenceData>();
	void packOneToOneReference(IObjectOneToOneReference reference) {
		final String name = reference.getReference().getName();
		final IDomainObject<Object> referencedDomainObject = reference.get();
		if (referencedDomainObject != null) {
			StandardOneToOneReferenceData data = new StandardOneToOneReferenceData(name, referencedDomainObject.getHandle());
			_oneToOneReferenceDataByName.put(name, data);
		}
	}
	void unpackOneToOneReference(IObjectOneToOneReference oneToOneReferenceToUpdate, IHandleMap handleMap) {
		final String name = oneToOneReferenceToUpdate.getReference().getName();
		final StandardOneToOneReferenceData data = _oneToOneReferenceDataByName.get(name);
		if (data != null) {
			final Handle handle = data.getHandle();
			oneToOneReferenceToUpdate.set(handleMap.getDomainObject(handle));
		} else {
			oneToOneReferenceToUpdate.set(null);
		}
		
	}

	/////////////////////////////////////////////////////////////////////////
	// CollectionReferences
	/////////////////////////////////////////////////////////////////////////
	
	private Map<String, StandardCollectionReferenceData> _collectionReferenceDataByName = new HashMap<String, StandardCollectionReferenceData>();
	void packCollectionReference(IObjectCollectionReference reference) {
		final String name = reference.getReference().getName();
		StandardCollectionReferenceData data = new StandardCollectionReferenceData(name);
		for(IDomainObject<?> domainObjectInCollection: reference.getCollection()) {
			data.addHandle(domainObjectInCollection.getHandle());
		}
		_collectionReferenceDataByName.put(name, data);
	}
	/**
	 * TODO: just dealing with set semantics here, need to support List too I think.
	 * 
	 * @param collectionReferenceToUpdate
	 * @param handleMap TODO
	 */
	void unpackCollectionReference(IObjectCollectionReference collectionReferenceToUpdate, IHandleMap handleMap) {
		// get the handles of all those DOs in in the collection to be updated
		Collection<Handle> collectionToUpdateHandles = new ArrayList<Handle>();
		final Collection<IDomainObject<Object>> collectionToUpdate = 
			collectionReferenceToUpdate.getCollection();
		for(IDomainObject<Object> domainObject: collectionToUpdate) {
			collectionToUpdateHandles.add(domainObject.getHandle());
		}
		
		// unpackage the handles
		final String name = collectionReferenceToUpdate.getReference().getName();
		final StandardCollectionReferenceData data = _collectionReferenceDataByName.get(name);
		final Collection<Handle> unpackagedHandles = data.getHandles();

		// compare the 2 collections of handles
		final Collection<Handle> handlesToAdd = new ArrayList<Handle>();
		final Collection<Handle> handlesToRemove = new ArrayList<Handle>();
		for(Handle handle: unpackagedHandles) {
			if (collectionToUpdateHandles.contains(handle)) {
				// nothing to do
			} else {
				handlesToAdd.add(handle);
			}
		}
		
		// apply the changes.
		for(Handle handle: collectionToUpdateHandles) {
			if (unpackagedHandles.contains(handle)) {
				// nothing to do
			} else {
				handlesToRemove.add(handle);
			}
		}
		
		for(Handle handle: handlesToAdd) {
			collectionReferenceToUpdate.addToCollection(handleMap.getDomainObject(handle));
		}
		for(Handle handle: handlesToRemove) {
			collectionReferenceToUpdate.removeFromCollection(handleMap.getDomainObject(handle));
		}
	}
}
