package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainAnalyzer;

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
public class RcpViewerExtension implements IDomainAnalyzer {

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
