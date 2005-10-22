package de.berlios.rcpviewer.progmodel.standard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EReferenceImpl;

import de.berlios.rcpviewer.domain.AbstractDomainClass;
import de.berlios.rcpviewer.domain.Emf;
import de.berlios.rcpviewer.domain.IAdapterFactory;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClassAdapter;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClassAdapter;
import de.berlios.rcpviewer.domain.LinkSemanticsType;
import de.berlios.rcpviewer.domain.MethodNameHelper;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.runtime.IRuntimeDomain;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;


/**
 * Represents a class in the meta model, akin to {@link java.lang.Class} and
 * wrapping an underlying EMF EClass.
 * 
 * <p>
 * TODO: should factor much up into a Generic implementation; the standard
 * programming model should be a user of getAdapter() for Eclipse-specific 
 * things.
 *  
 * <p>
 * JAVA5_FIXME: need to sort out generic parameterization. 
 * 
 * @author Dan Haywood
 */
public class RuntimeDomainClass<T> 
		extends AbstractDomainClass<T> 
		implements IRuntimeDomainClass<T> {
	
	private final Class<T> _javaClass;

	/**
	 * To deserialize semantics from EMF metamodel.
	 */
	private StandardProgModelSemanticsEmfSerializer serializer = new StandardProgModelSemanticsEmfSerializer();

	public RuntimeDomainClass(final IDomain domain, final EClass eClass, final Class<T> javaClass) {
		super(domain, eClass);
		this._javaClass = javaClass;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.AbstractDomainClass#loadClass(java.lang.String)
	 */
	@Override
	protected Class<IAdapterFactory> loadClass(String adapterFactoryName) throws ClassNotFoundException {
		
		try {
			return (Class<IAdapterFactory>)Class.forName(adapterFactoryName);
		} catch (ClassNotFoundException ex) {
			// do nothing
		}
		
		return (Class<IAdapterFactory>)Platform.getBundle("de.berlios.rcpviewer.domain.runtime").loadClass(adapterFactoryName);
		//return (Class<IAdapterFactory>)DomainPlugin.getInstance().getBundle().loadClass(adapterFactoryName);
	}


	public Class<T> getJavaClass() {
		return _javaClass;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getName()
	 */
	@Override
	public String getName() {
		Named named = serializer.getNamed(getEClass());
		if (named == null) {
			return getEClassName();
		}
		return named.value();
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAccessorFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getAccessorFor(EAttribute attribute) {
		return serializer.getAttributeAccessorMethod(attribute);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getMutatorFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getMutatorFor(EAttribute eAttribute) {
		return serializer.getAttributeMutatorMethod(eAttribute);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAccessorOrMutatorFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getAccessorOrMutatorFor(EAttribute eAttribute) {
		Method accessorMethod = getAccessorFor(eAttribute);
		if (accessorMethod != null) {
			return accessorMethod;
		}
		return getMutatorFor(eAttribute);
	}

	public boolean containsAttribute(EAttribute eAttribute) {
		return this._eClass.getEAllAttributes().contains(eAttribute);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#authorizationConstraintFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public IPrerequisites authorizationConstraintFor(EAttribute attribute) {
		IRuntimeDomain rd = (IRuntimeDomain)this.getDomain();
		return rd.getAuthorizationManager().preconditionsFor(getAttribute(attribute).attributeIdFor());
	}
	
	// ATTRIBUTES SUPPORT: END


	// REFERENCES SUPPORT: START

	/**
	 * Horrible hack, see {@link #oppRefState}.
	 * 
	 */
	static enum OppRefState {
		stillBuilding,
		onceMore,
		neverAgain;
	}

	/**
	 * HACK: This is a horrible hack to allow 
	 * {@link OppositeReferencesIdentifier#identify()} to be called once more,
	 * in {@link RuntimeDomain#lookup(Class)}.
	 */
	protected OppRefState oppRefState = OppRefState.stillBuilding;

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAccessorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getAccessorFor(EReference eReference) {
		return serializer.getReferenceAccessor(eReference);
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getMutatorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getMutatorFor(EReference reference) {
		return serializer.getReferenceMutator(reference);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAssociatorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getAssociatorFor(EReference eReference) {
		if (eReference.isMany()) {
			return serializer.getReferenceCollectionAssociator(eReference);
		} else {
			return serializer.getReferenceOneToOneAssociator(eReference);	
		}
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getDissociatorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getDissociatorFor(EReference eReference) {
		if (eReference.isMany()) {
			return serializer.getReferenceCollectionDissociator(eReference);
		} else {
			return serializer.getReferenceOneToOneDissociator(eReference);	
		}
	}
	

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#authorizationConstraintFor(org.eclipse.emf.ecore.EReference)
	 */
	public IPrerequisites authorizationConstraintFor(EReference reference) {
		IRuntimeDomain rd = (IRuntimeDomain)this.getDomain();
		return rd.getAuthorizationManager().preconditionsFor(getReference(reference).referenceIdFor());
	}

	// REFERENCE SUPPORT: END

	// OPERATIONS SUPPORT: START

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getInvokerFor(org.eclipse.emf.ecore.EOperation)
	 */
	public Method getInvokerFor(EOperation eOperation) {
		return serializer.getOperationMethod(eOperation);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#authorizationConstraintFor(org.eclipse.emf.ecore.EOperation)
	 */
	public IPrerequisites authorizationConstraintFor(EOperation operation) {
		IRuntimeDomain rd = (IRuntimeDomain)this.getDomain();
		return rd.getAuthorizationManager().preconditionsFor(getOperation(operation).operationIdFor());
	}

	// OPERATION SUPPORT: END


	/**
	 * Setting up for AspectJ introduction of logging infrastructure.
	 * @param message
	 */
	private void logWarning(String message) {}


	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return "DomainClass.javaClass = " + _javaClass ; }

}
