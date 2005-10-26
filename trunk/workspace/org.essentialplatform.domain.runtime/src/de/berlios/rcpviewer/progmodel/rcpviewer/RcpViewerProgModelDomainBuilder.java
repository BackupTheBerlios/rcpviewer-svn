package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.ecore.EClass;

import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.RuntimeClassBinding;

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

	private final RcpViewerProgModelSemanticsEmfSerializer serializer = new RcpViewerProgModelSemanticsEmfSerializer();

	/**
	 * TODO: use parameters to downcast?
	 */
	public void build(IDomainClass domainClass) {
		Class<?> javaClass = ((RuntimeClassBinding)domainClass.getBinding()).getJavaClass();
		EClass eClass = domainClass.getEClass();
		
		// Install adapter object 
		RcpViewerProgModelAdapterFactory<RcpViewerDomainClass> adapterFactory = 
			new RcpViewerProgModelAdapterFactory<RcpViewerDomainClass>();
		domainClass.setAdapterFactory(RcpViewerDomainClass.class, adapterFactory);


		// imageUrlAt
		serializer.setClassImageUrlAt(eClass, javaClass.getAnnotation(ImageUrlAt.class));
	}
}
