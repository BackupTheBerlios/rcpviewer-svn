package de.berlios.rcpviewer.gui;

import org.eclipse.jface.viewers.ILabelProvider;

import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Type providing display values for individual instances of 
 * <code>IDomainObject</code>.
 * <br>Implementations are distimguished using the <code>IDomainClass</code>
 * of the objects.
 * <br>Implementations must be stateless as they are repeatedly used.
 * @author Mike
 */
public interface IDomainObjectLabelProvider extends ILabelProvider {
	
	public static final String EXTENSION_POINT_ID
		= "de.berlios.rcpviewer.gui.labelprovider"; //$NON-NLS-1$
	
	/**
	 * Whether this field builder is applicable for the passed class.
	 * @param attribute
	 * @return
	 */
	public boolean isApplicable( IDomainClass clazz );

}
