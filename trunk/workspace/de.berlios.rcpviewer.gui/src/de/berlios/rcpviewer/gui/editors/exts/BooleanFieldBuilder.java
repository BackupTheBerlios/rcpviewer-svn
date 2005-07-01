package de.berlios.rcpviewer.gui.editors.exts;

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.gui.editors.IFieldBuilder;
import de.berlios.rcpviewer.session.IDomainObject;

public class BooleanFieldBuilder implements IFieldBuilder {

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isApplicable(EAttribute attribute) {
		return Boolean.class == attribute.getEType().getInstanceClass();
	}
	
	/**
	 * Creates a check box
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createFormPart(org.eclipse.swt.widgets.Composite, de.berlios.rcpviewer.session.IDomainObject, org.eclipse.emf.ecore.EAttribute)
	 */
	public IFormPart createFormPart(
			Composite parent, IDomainObject object, EAttribute attribute) {
		return new BooleanFieldPart(parent, object, attribute );
	}

}
