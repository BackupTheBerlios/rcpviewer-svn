package org.essentialplatform.progmodel.louis.runtime;

import org.eclipse.emf.ecore.EClass;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IClassLoader;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.louis.app.ImageUrlAt;
import org.essentialplatform.progmodel.louis.core.domain.LouisDomainClass;
import org.essentialplatform.progmodel.louis.core.domain.adapters.LouisProgModelAdapterFactory;
import org.essentialplatform.progmodel.louis.core.emf.LouisProgModelSemanticsEmfSerializer;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

/**
 * Adds annotations specific to the RCPViewer.
 * 
 * <p>
 * Specifically, these are:
 * <ul>
 * <li>ImageDescriptor.
 * </ul>
 * 
 * @author Dan Haywood
 *
 */
public class LouisProgModelRuntimeBuilder implements IDomainBuilder {

	private final LouisProgModelSemanticsEmfSerializer serializer = new LouisProgModelSemanticsEmfSerializer();

	/**
	 * TODO: use parameters to downcast?
	 */
	public void build(IDomainClass domainClass) {
		Class<?> javaClass = ((IDomainClassRuntimeBinding)domainClass.getBinding()).getJavaClass();
		EClass eClass = domainClass.getEClass();
		
		// Install adapter object 
		LouisProgModelAdapterFactory<LouisDomainClass> adapterFactory = 
			new LouisProgModelAdapterFactory<LouisDomainClass>();
		domainClass.setAdapterFactory(LouisDomainClass.class, adapterFactory);


		// imageUrlAt
		serializer.setClassImageUrlAt(eClass, javaClass.getAnnotation(ImageUrlAt.class));
	}

	/*
	 * @see org.essentialplatform.core.domain.builders.IDomainBuilder#identifyOppositeReferencesFor(org.essentialplatform.core.domain.IDomainClass)
	 */
	public void identifyOppositeReferencesFor(IDomainClass domainClass) {
		// does nothing
	}
	
	////////////////////////////////////////////////////////////////////////
	// ClassLoader
	// (either dependency injected, or propagated by composite). 
	////////////////////////////////////////////////////////////////////////

	private IClassLoader _classLoader;
	public void setClassLoader(IClassLoader classLoader) {
		_classLoader = classLoader;
	}


}
