package org.essentialplatform.runtime.server.domain.bindings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IDomainBinding;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.progmodel.ProgrammingModelException;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Prerequisites;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;
import org.essentialplatform.progmodel.louis.core.emf.LouisProgModelSemanticsEmfSerializer;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.server.persistence.IPersistenceIdAssigner;
import org.essentialplatform.runtime.server.persistence.IdSemanticsPersistenceIdAssigner;
import org.essentialplatform.runtime.shared.AbstractRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.PersistenceId;
import org.osgi.framework.Bundle;

/**
 * A binding of {@link IDomainClass} for the server environment.
 * 
 * @author Dan Haywood
 */
public final class RuntimeServerBinding extends AbstractRuntimeBinding {
	
	private static Logger _logger = Logger.getLogger(RuntimeServerBinding.class);
	protected Logger getLogger() {
		return _logger;
	}



	//////////////////////////////////////////////////////////////////////

	@Override
	public Bundle getBundle() {
		return Platform.getBundle("org.essentialplatform.domain.runtime");
	}


	/**
	 * Saves the primary builder, and sets up a sequential persistence Id assigner.
	 *
	 * <p>
	 * TODO: at some point, anticipate that the IPersistenceIdAssigner will be 
	 * injected.
	 * 
	 * @throws RuntimeException if a binding has already been set.
	 */
	public RuntimeServerBinding(IDomainBuilder primaryBuilder) {
		super(primaryBuilder);
	}


	@Override
	public IDomainBinding bindingFor(IDomain domain) {
		return new RuntimeServerDomainBinding(domain);
	}
	@Override
	public IDomainClassServerBinding bind(IDomainClass domainClass, Object classRepresentation) {
		IDomainClassServerBinding binding = bind(domainClass, (Class<?>)classRepresentation);
		return binding;
	}
	private <V> IDomainClassServerBinding<V> bind(IDomainClass domainClass, Class<V> javaClass) {
		return new RuntimeServerClassBinding<V>(domainClass, javaClass, _sequentialPersistenceIdAssigner);
	}
	
	@Override
	public IAttributeServerBinding bindingFor(IDomainClass.IAttribute attribute) {
		return new RuntimeServerAttributeBinding(attribute);
	}
	@Override
	public IOneToOneReferenceServerBinding bindingFor(IDomainClass.IOneToOneReference oneToOneReference) {
		return new RuntimeServerOneToOneReferenceBinding(oneToOneReference);
	}
	@Override
	public ICollectionReferenceServerBinding bindingFor(IDomainClass.ICollectionReference collectionReference) {
		return new RuntimeServerCollectionReferenceBinding(collectionReference);
	}
	@Override
	public IOperationServerBinding bindingFor(IDomainClass.IOperation operation) {
		return new RuntimeServerOperationBinding(operation);
	}

	//////////////////////////////////////////////////////////////////////

	/**
	 * Injected in server-side.
	 */
	private IPersistenceIdAssigner _sequentialPersistenceIdAssigner; 
	
	/**
	 * Injected.
	 * 
	 * @param assigner
	 */
	public void setPersistenceIdAssigner(IPersistenceIdAssigner assigner) {
		_sequentialPersistenceIdAssigner = assigner;
	}
	
	//////////////////////////////////////////////////////////////////////
	

	public final class RuntimeServerDomainBinding extends AbstractRuntimeDomainBinding implements IDomainBinding {

		
		public RuntimeServerDomainBinding(IDomain domain) {
			super(domain);
		}
		


	}
	
	public final class RuntimeServerClassBinding<T> extends AbstractRuntimeClassBinding<T> implements IDomainClassServerBinding<T> {

		
		/**
		 * Delegates either to a composite persistence Id assigner or a
		 * sequential persistence Id assigner dependent on the semantics of the
		 * <tt>AssignmentType</tt> of the domain class.
		 * 
		 * @param domainClass
		 * @param javaClass
		 * @param delegatePersistenceIdAssigner
		 */
		public RuntimeServerClassBinding(IDomainClass domainClass, Class<T> javaClass, IPersistenceIdAssigner delegatePersistenceIdAssigner) {
			super(domainClass, javaClass);
			_persistenceIdAssigner = new IdSemanticsPersistenceIdAssigner(domainClass, delegatePersistenceIdAssigner);
		}

		private final IPersistenceIdAssigner _persistenceIdAssigner; 

		/*
		 * @see org.essentialplatform.runtime.shared.persistence.IPersistenceIdAssigner#assignPersistenceIdFor(IDomainObject)
		 */
		public <V> PersistenceId assignPersistenceIdFor(IDomainObject<V> domainObject) {
			return _persistenceIdAssigner.assignPersistenceIdFor(domainObject);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.persistence.IPersistenceIdAssigner#nextAssigner()
		 */
		public IPersistenceIdAssigner nextAssigner() {
			return null;
		}
		

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerAttributeBinding extends AbstractRuntimeAttributeBinding implements IAttributeServerBinding {

		public RuntimeServerAttributeBinding(IDomainClass.IAttribute attribute) {
			super(attribute);
		}
		
	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class DelegateRuntimeServerReferenceBinding extends AbstractRuntimeReferenceBinding {
		
		public DelegateRuntimeServerReferenceBinding(IDomainClass.IReference reference) {
			super(reference);
		}
		

	}
	
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerOneToOneReferenceBinding extends AbstractRuntimeOneToOneReferenceBinding implements IOneToOneReferenceServerBinding {

		private final DelegateRuntimeServerReferenceBinding _delegateBinding; 

		public RuntimeServerOneToOneReferenceBinding(IDomainClass.IOneToOneReference oneToOneReference) {
			super(oneToOneReference);
			_delegateBinding = new DelegateRuntimeServerReferenceBinding(oneToOneReference);
		}

		

	}
		
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerCollectionReferenceBinding extends AbstractRuntimeCollectionReferenceBinding implements ICollectionReferenceServerBinding {

		private final DelegateRuntimeServerReferenceBinding _delegateBinding; 

		public RuntimeServerCollectionReferenceBinding(IDomainClass.ICollectionReference collectionReference) {
			super(collectionReference);
			_delegateBinding = new DelegateRuntimeServerReferenceBinding(collectionReference);
		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerOperationBinding extends AbstractRuntimeOperationBinding implements IOperationServerBinding {

		
		public RuntimeServerOperationBinding(IDomainClass.IOperation operation) {
			super(operation);
		}
	}


}
