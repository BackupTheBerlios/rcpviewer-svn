package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.util.Map;

import de.berlios.rcpviewer.domain.AbstractAdapterFactory;
import de.berlios.rcpviewer.domain.IDomainClass;

public class RcpViewerProgModelAdapterFactory<T> extends AbstractAdapterFactory<T> {

	public RcpViewerProgModelAdapterFactory() {}
	public RcpViewerProgModelAdapterFactory(String url) {
		super(url);
	}
	public <V> T createAdapter(IDomainClass<V> adaptedDomainClass, Map<String, String> details) {
		return (T)new RcpViewerDomainClass<V>(adaptedDomainClass, details);
	}

}
