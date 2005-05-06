package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IMetaModelExtension;
import de.berlios.rcpviewer.progmodel.standard.ImageUrlAt;

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
		ImageUrlAt imageUrlAt = javaClass.getAnnotation(ImageUrlAt.class);
		if (imageUrlAt != null) {
			URL imageUrl;
			try {
				imageUrl = new URL(imageUrlAt.value());
				domainClass.setAdapter(ImageDescriptor.class, 
				        ImageDescriptor.createFromURL(imageUrl));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block - should log.
				e.printStackTrace();
			}
		}
	}

}
