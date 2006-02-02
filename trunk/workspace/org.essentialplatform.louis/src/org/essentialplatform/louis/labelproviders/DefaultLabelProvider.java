package org.essentialplatform.louis.labelproviders;

import java.util.Date;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.LouisPlugin;

/**
 * Can handle any object. See <code>getText()</code> for rules applied.
 * @author Mike
 */
public class DefaultLabelProvider extends LabelProvider implements ILouisLabelProvider{
	
	private Image _missing = null;
	
	public DefaultLabelProvider() {
		super();
	}

	/**
	 * <ul>
	 * <li><code>null</code>'s returned as blank strings
	 * <li><code>Date</code>'s given a common format
	 * <li>all others subject to <code>String.valueof()</code>
	 * <ul>
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ( element == null ) return ""; //$NON-NLS-1$
		if ( element instanceof Date ) {
			return LouisPlugin.DATE_FORMATTER.format( (Date)element );
		}
		else {
			return String.valueOf( element );
		}
	}

	/*
	 * Does nothing.
	 * 
	 * @see org.essentialplatform.louis.labelproviders.ILouisLabelProvider#init()
	 */
	public void init() {
		// does nothing
	}

	/**
	 * Returns the platform's default missing image 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if ( _missing == null ) {
			_missing = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return _missing;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		if ( _missing != null ) {
			_missing.dispose();
			_missing = null;
		}
		super.dispose();
	}

}