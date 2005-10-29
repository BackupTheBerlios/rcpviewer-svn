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
 * programming model for the <i>Loius</i> viewing mechanism.
 * 
 * <p>
 * Typical usage:
 * <pre>
 * IDomainClass someDC = ...
 * RcpViewerDomainClass someRcpViewerDC = someDC.getAdapter(RcpViewerDomainClass.class);
 * </pre>
 * 
 * @author Dan Haywood
 *
 */
public final class LouisDomainClass extends AbstractDomainClassAdapter {

	private final LouisProgModelSemanticsEmfSerializer serializer = new LouisProgModelSemanticsEmfSerializer();

	public LouisDomainClass(IDomainClass adaptedDomainClass) {
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
