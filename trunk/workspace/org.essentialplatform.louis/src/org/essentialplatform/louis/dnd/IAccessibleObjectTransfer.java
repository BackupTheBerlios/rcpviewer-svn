package org.essentialplatform.louis.dnd;

import org.eclipse.swt.dnd.TransferData;

/**
 * Tags any transfer that can has a public <code>nativeToJava</code> method.
 * @author Mike
 */
public interface IAccessibleObjectTransfer {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	public Object nativeToJava(TransferData transferData);
}
