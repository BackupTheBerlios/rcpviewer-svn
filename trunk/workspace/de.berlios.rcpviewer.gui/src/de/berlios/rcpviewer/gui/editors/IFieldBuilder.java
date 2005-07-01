package de.berlios.rcpviewer.gui.editors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Type for building individual fields within a gui.
 * @author Mike
 */
public interface IFieldBuilder {
	
	public static final String EXTENSION_POINT_ID
		= "de.berlios.rcpviewer.gui.fieldbuilder";
	
	/**
	 * Whether this field builder is applicable for the passed attribute.
	 * @param attribute
	 * @return
	 */
	public boolean isApplicable( EAttribute attribute );

	/**
	 * Create the gui within the supplied parent composite for the passed
	 * attribute of the passed domain object.
	 * @param parent
	 * @param object
	 * @param attribute
	 * @return
	 */
	public IFormPart createFormPart( 
			Composite parent, 
			IDomainObject object,
			EAttribute attribute );
}
