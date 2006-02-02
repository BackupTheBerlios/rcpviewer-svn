package org.essentialplatform.louis.labelproviders;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Tagging interface that indicates a change in semantics from the parent 
 * interface's contract.
 * <br>See method descriptions for details.
 */
public interface ILouisLabelProvider extends ILabelProvider {
	
	/**
	 * Initialized by Essential.
	 *
	 */
	public void init();
	
	/**
	 * As parent interface but a <code>null</code> indicates that this 
	 * label provider cannot handle the passed element.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element);
	
	/**
	 * As parent interface but a <code>null</code> indicates that this 
	 * label provider cannot handle the passed element.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element);

}
