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
	
	public static final String EXTENSION_POINT_ID
		= "de.berlios.rcpviewer.gui.transferprovider"; //$NON-NLS-1$

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
