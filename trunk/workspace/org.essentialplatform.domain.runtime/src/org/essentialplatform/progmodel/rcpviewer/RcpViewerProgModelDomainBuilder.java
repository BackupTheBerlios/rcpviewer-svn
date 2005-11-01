package org.essentialplatform.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.ecore.EClass;

import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.runtime.RuntimeDeployment.RuntimeClassBinding;

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
