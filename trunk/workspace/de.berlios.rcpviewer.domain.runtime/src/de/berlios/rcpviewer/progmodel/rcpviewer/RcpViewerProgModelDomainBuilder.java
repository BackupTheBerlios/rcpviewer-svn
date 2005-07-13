package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;

import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

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

	/**
	 * TODO: use parameters to downcast?
	 */
	public <V> void build(IDomainClass<V> domainClass) {
		build((IRuntimeDomainClass<V>)domainClass);
	}

	private <V> void build(IRuntimeDomainClass<V> domainClass) {
		Class<V> javaClass = domainClass.getJavaClass();
		final ImageUrlAt imageUrlAt = javaClass.getAnnotation(ImageUrlAt.class);
		if (imageUrlAt != null) {
			try {
				// check URL is ok
				new URL(imageUrlAt.value());
		
				domainClass.setAdapterFactory(RcpViewerDomainClass.class, 
						new RcpViewerProgModelAdapterFactory<RcpViewerDomainClass>(imageUrlAt.value()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block - should log.
				e.printStackTrace();
			}
		}
	}
}
