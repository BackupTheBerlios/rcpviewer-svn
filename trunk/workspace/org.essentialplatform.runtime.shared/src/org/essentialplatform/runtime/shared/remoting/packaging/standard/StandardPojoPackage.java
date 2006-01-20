package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.PackagingException;
import org.essentialplatform.runtime.shared.session.SessionBinding;

final class StandardPojoPackage implements IPojoPackage {

	/////////////////////////////////////////////////////////////////////////
	// Handle
	/////////////////////////////////////////////////////////////////////////
	
	private Handle _handle;
	void packHandle(Handle handle) {
		_handle = handle;
	}
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IHandlePackage#unpackHandle()
	 */
	public Handle unpackHandle() {
		return _handle;
	}

	/////////////////////////////////////////////////////////////////////////
	// SessionBinding
	/////////////////////////////////////////////////////////////////////////
	
	private SessionBinding _sessionBinding;
	void packSessionBinding(SessionBinding sessionBinding) {
		_sessionBinding = sessionBinding;
	}
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.ISessionBindingPackage#unpackSessionBinding()
	 */
	public SessionBinding unpackSessionBinding() {
		return _sessionBinding;
	}

	/////////////////////////////////////////////////////////////////////////
	// PersistState
	/////////////////////////////////////////////////////////////////////////

	private PersistState _persistState;
	void packPersistState(PersistState persistState) {
		_persistState = persistState;
	}
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage#unpackPersistState()
	 */
	public PersistState unpackPersistState() {
		return _persistState;
	}

	/////////////////////////////////////////////////////////////////////////
	// ResolveState
	/////////////////////////////////////////////////////////////////////////

	private ResolveState _resolveState;
	void packResolveState(ResolveState resolveState) {
		_resolveState = resolveState;
	}
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage#unpackResolveState()
	 */
	public ResolveState unpackResolveState() {
		return _resolveState;
	}

	/////////////////////////////////////////////////////////////////////////
	// Attributes
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Attributes are transported as a list, but read back from a hash built lazily on unpacking.
	 */
	private List<StandardAttributeData> _attributes = new ArrayList<StandardAttributeData>();
	/**
	 * Not serialized; built up lazily when first unpack.
	 */
	private transient Map<String, StandardAttributeData> _attributeDataByName;
	
	void packAttribute(IObjectAttribute attribute) {
		final String name = attribute.getAttribute().getName();
		StandardAttributeData data = new StandardAttributeData(name, attribute.get());
		_attributes.add(data);
	}
	void unpackAttribute(IObjectAttribute attributeToUpdate) {
		if (_attributeDataByName == null) {
			_attributeDataByName = map(_attributes);
		}
		final String name = attributeToUpdate.getAttribute().getName();
		StandardAttributeData data = _attributeDataByName.get(name);
		attributeToUpdate.set(data.getValue());
	}

	/////////////////////////////////////////////////////////////////////////
	// OneToOneReferences
	/////////////////////////////////////////////////////////////////////////
	
	/**
	 * 1:1 references are transported as a list, but read back from a hash built lazily on unpacking.
	 */
	private List<StandardOneToOneReferenceData> _oneToOneReferences = new ArrayList<StandardOneToOneReferenceData>();
	/**
	 * Not serialized; built up lazily when first unpack.
	 */
	private transient Map<String, StandardOneToOneReferenceData> _oneToOneReferenceDataByName;
	
	void packOneToOneReference(IObjectOneToOneReference reference) {
		final String name = reference.getReference().getName();
		final IDomainObject<Object> referencedDomainObject = reference.get();
		if (referencedDomainObject != null) {
			StandardOneToOneReferenceData data = new StandardOneToOneReferenceData(name, referencedDomainObject.getHandle());
			_oneToOneReferences.add(data);
		}
	}
	void unpackOneToOneReference(IObjectOneToOneReference oneToOneReferenceToUpdate, IHandleMap handleMap) {
		if (_oneToOneReferenceDataByName == null) {
			_oneToOneReferenceDataByName = map(_oneToOneReferences);
		}
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
	
	/**
	 * Collection references are transported as a list, but read back from a hash built lazily on unpacking.
	 */
	private List<StandardCollectionReferenceData> _collectionReferences = new ArrayList<StandardCollectionReferenceData>();
	/**
	 * Not serialized; built up lazily when first unpack.
	 */
	private transient Map<String, StandardCollectionReferenceData> _collectionReferenceDataByName;
	
	void packCollectionReference(IObjectCollectionReference reference) {
		final String name = reference.getReference().getName();
		StandardCollectionReferenceData data = new StandardCollectionReferenceData(name);
		for(IDomainObject<?> domainObjectInCollection: reference.getCollection()) {
			data.addHandle(domainObjectInCollection.getHandle());
		}
		_collectionReferences.add(data);
	}
	/**
	 * TODO: just dealing with set semantics here, need to support List too I think.
	 * 
	 * @param collectionReferenceToUpdate
	 * @param handleMap TODO
	 */
	void unpackCollectionReference(IObjectCollectionReference collectionReferenceToUpdate, IHandleMap handleMap) {
		if (_collectionReferenceDataByName == null) {
			_collectionReferenceDataByName = map(_collectionReferences);
		}
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

	
	/////////////////////////////////////////////////////////////////////////
	// Helpers
	/////////////////////////////////////////////////////////////////////////

	private <V extends INamed> Map map(List<V> list) {
		Map map = new HashMap();
		for(INamed namedObject: list) {
			map.put(namedObject.getName(), namedObject); // JAVA5_FIXME
		}
		return map;
	}

}
