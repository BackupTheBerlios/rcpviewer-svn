package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.domain.AbstractDomainClassAdapter;
import de.berlios.rcpviewer.domain.IDomainClass;


/**
 * Extension of {@link IDomainClass} that supports semantics of the
 * rcpviewer programming model.
 * 
 * <p>
 * Typical usage:
 * <pre>
 * IDomainClass<T> someDC = ...
 * RcpViewerDomainClass someRcpViewerDC = someDC.getAdapter(RcpViewerDomainClass.class);
 * </pre>
 * 
 * @author Dan Haywood
 *
 */
public final class RcpViewerDomainClass<T> extends AbstractDomainClassAdapter<T> {

	private final RcpViewerProgModelSemanticsEmfSerializer serializer = new RcpViewerProgModelSemanticsEmfSerializer();

	public RcpViewerDomainClass(IDomainClass<T> adaptedDomainClass) {
		super(adaptedDomainClass);
	}

	/**
	 * To support the ImageDescriptor annotation.
	 * 
	 * @return
	 */
	public ImageDescriptor imageDescriptor() {

		EClass eClass = adapts().getEClass();
		ImageUrlAt imageUrlAt = serializer.getClassImageUrlAt(eClass);
		if (imageUrlAt == null) return null;
		String url = imageUrlAt.value();
		URL imageUrl;
		try {
			imageUrl = new URL(url);
			return ImageDescriptor.createFromURL(imageUrl);
		} catch (MalformedURLException e) {
			return null;
		}
	}

}
