package org.essentialplatform.louis.labelproviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.labelproviders.BaseClassLabelProvider;
import org.essentialplatform.louis.labelproviders.DefaultDomainObjectLabelProvider;
import org.essentialplatform.louis.labelproviders.DefaultLabelProvider;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.louis.util.ConfigElementSorter;

/**
 * A global {@link ILouisLabelProvider} that can handle any object.
 * 
 * <p>
 * Subclasses should obtain additional (specific) <code>ILouisLabelProvider</code>'s and
 * register them using {@link #addLabelProvider(ILouisLabelProvider)}.  This
 * implementation additionally (in {@link #init()}) adds 3 further label
 * provider implementations that act as a "catch-all".
 * 
 * <p>
 * The main API ({@link #getImage(Object)}, {@link #getText(Object)}) delegates
 * through all registered lable providers using the first one that can handle
 * the passed object.  If none registered can handle the object, then one of
 * the "catch-all"s will.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractLabelProviderChain extends LabelProvider implements ILouisLabelProvider {


	/**
	 * Adds 3 additional default {@link ILouisLabelProvider}s.
	 * 
	 * <p>
	 * Assumes that all the main label providers have been added in the
	 * constructor (by calling {@link #addLabelProvider}).
	 * 
	 * @see org.essentialplatform.louis.labelproviders.ILouisLabelProvider#init()
	 */
	public void init() {
		addLabelProvider(new DefaultDomainObjectLabelProvider());
		addLabelProvider(new BaseClassLabelProvider());
		addLabelProvider(new DefaultLabelProvider());
	}

	private final List<ILouisLabelProvider> _labelProviders = new ArrayList<ILouisLabelProvider>();
	/**
	 * Subclasses should call in their constructor.
	 * 
	 * @param labelProvider
	 */
	protected final void addLabelProvider(ILouisLabelProvider labelProvider) {
		_labelProviders.add(labelProvider);
	}

	/*
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public final String getText(Object element) {
		for (ILabelProvider provider : _labelProviders) {
			String s = provider.getText( element );
			if ( s != null ) return s;
		}
		// should never get here as last in array is default label provider
		assert false;
		return null;
	}
	
	/*
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public final Image getImage(Object element) {
		for (ILabelProvider provider : _labelProviders) {
			Image i = provider.getImage( element );
			if ( i != null ) return i;
		}
		// should never get here as last in array is default label provider
		assert false;
		return null;
	}

}
