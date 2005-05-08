package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.metamodel.IAdapterFactory;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IMetaModelExtension;

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
public class RcpViewerExtension implements IMetaModelExtension {

	public void analyze(IDomainClass<?> domainClass) {
		Class<?> javaClass = domainClass.getJavaClass();
		final ImageUrlAt imageUrlAt = javaClass.getAnnotation(ImageUrlAt.class);
		if (imageUrlAt != null) {
			try {
				// check URL is ok
				final URL imageUrl = new URL(imageUrlAt.value());
		
				domainClass.setAdapterFactory(ImageDescriptor.class, 
						new ImageDescriptorAdapterFactory<ImageDescriptor>(imageUrlAt.value()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block - should log.
				e.printStackTrace();
			}
		}
	}
}
