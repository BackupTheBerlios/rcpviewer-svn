package org.essentialplatform.runtime.progmodel.rcpviewer;

import org.eclipse.emf.ecore.EClass;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.louis.core.domain.LouisDomainClass;
import org.essentialplatform.progmodel.louis.core.domain.adapters.LouisProgModelAdapterFactory;
import org.essentialplatform.progmodel.louis.core.emf.LouisProgModelSemanticsEmfSerializer;
import org.essentialplatform.progmodel.rcpviewer.ImageUrlAt;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment.RuntimeClassBinding;

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
public class RcpViewerProgModelDomainBuilder implements IDomainBuilder {

	private final LouisProgModelSemanticsEmfSerializer serializer = new LouisProgModelSemanticsEmfSerializer();

	/**
	 * TODO: use parameters to downcast?
	 */
	public void build(IDomainClass domainClass) {
		Class<?> javaClass = ((RuntimeClassBinding)domainClass.getBinding()).getJavaClass();
		EClass eClass = domainClass.getEClass();
		
		// Install adapter object 
		LouisProgModelAdapterFactory<LouisDomainClass> adapterFactory = 
			new LouisProgModelAdapterFactory<LouisDomainClass>();
		domainClass.setAdapterFactory(LouisDomainClass.class, adapterFactory);


		// imageUrlAt
		serializer.setClassImageUrlAt(eClass, javaClass.getAnnotation(ImageUrlAt.class));
	}
}