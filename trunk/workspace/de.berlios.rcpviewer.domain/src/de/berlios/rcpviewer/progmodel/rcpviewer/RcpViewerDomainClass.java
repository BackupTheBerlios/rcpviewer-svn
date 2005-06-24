package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.domain.AbstractDomainClassAdapter;
import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants;
import de.berlios.rcpviewer.progmodel.standard.InDomain;


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

	
	private Map<String,String> details;

	public RcpViewerDomainClass(IDomainClass<T> adaptedDomainClass, final Map<String,String> details) {
		super(adaptedDomainClass);
		this.details = details;
	}

	/**
	 * To support the ImageDescriptor annotation.
	 * 
	 * @return
	 */
	public ImageDescriptor imageDescriptor() {

		String url = details.get("url");
		URL imageUrl;
		try {
			imageUrl = new URL(url);
			return ImageDescriptor.createFromURL(imageUrl);
		} catch (MalformedURLException e) {
			// TODO should log?
			return null;
		}
	}

}