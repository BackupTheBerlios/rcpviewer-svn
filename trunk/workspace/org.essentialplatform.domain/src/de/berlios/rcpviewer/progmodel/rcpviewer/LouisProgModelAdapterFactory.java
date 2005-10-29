package de.berlios.rcpviewer.progmodel.rcpviewer;

import java.util.Map;

import de.berlios.rcpviewer.domain.AbstractAdapterFactory;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.rcpviewer.LouisDomainClass;

/**
 * Creates a {@link LouisDomainClass} for the supplied {@link IDomainClass}.
 * 
 * @author Dan Haywood
 * @param <T>
 */
public class LouisProgModelAdapterFactory<T> extends AbstractAdapterFactory<T> {

	public LouisProgModelAdapterFactory() {}
	public LouisProgModelAdapterFactory(String url) {
		super(url);
	}
	public T createAdapter(IDomainClass adaptedDomainClass) {
		return (T)new LouisDomainClass(adaptedDomainClass);
	}

}
