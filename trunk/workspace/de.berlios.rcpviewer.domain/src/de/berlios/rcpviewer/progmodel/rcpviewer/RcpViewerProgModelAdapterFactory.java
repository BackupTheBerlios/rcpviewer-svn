package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.domain.AbstractAdapterFactory;
import de.berlios.rcpviewer.domain.IAdapterFactory;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedDomainClass;

public class RcpViewerProgModelAdapterFactory<T> extends AbstractAdapterFactory<T> {

	public RcpViewerProgModelAdapterFactory() {}
	public RcpViewerProgModelAdapterFactory(String url) {
		super(url);
	}
	public <V> T createAdapter(IDomainClass<V> adaptedDomainClass, Map<String, String> details) {
		return (T)new RcpViewerDomainClass<V>(adaptedDomainClass, details);
	}

}
