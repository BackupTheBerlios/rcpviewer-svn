package org.essentialplatform.louis.dnd;

import org.eclipse.swt.dnd.Transfer;

/**
 * Equivalent to <code>ILabelProvider</code> but returns <code>Transfer</code>
 * objects.
 * @author Mike
 * @see org.eclipse.jface.viewers.ILabelProvider
 * @see org.eclipse.swt.dnd.Transfer
 */
public interface IDndTransferProvider {
	
	/**
	 * Initialize.
	 * 
	 * <p>
	 * Separates out the reading of the information from the creating of the
	 * <tt>Transfer</tt> objects.
	 *
	 */
	public void init();
	
	/**
	 * 
	 * @param element
	 * @return
	 */
    public Transfer getTransfer( Class clazz );
    
    /**
     * As it says
     * @return
     */
    public Transfer[] getAllTransfers();
}
