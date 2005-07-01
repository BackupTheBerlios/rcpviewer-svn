package de.berlios.rcpviewer.gui.editors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Used when no other available <code>IFieldBuilder</code> is applicable.
 * @author Mike

 */
class DefaultFieldBuilder implements IFieldBuilder {

	/**
	 * Always returns <code>true</code> - always applicable
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isApplicable(EAttribute attribute) {
		return true;
	}

	/**
	 * Creates a Text box displaying the toString() value.
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createFormPart(org.eclipse.swt.widgets.Composite, de.berlios.rcpviewer.session.IDomainObject, org.eclipse.emf.ecore.EAttribute)
	 */
	public IFormPart createFormPart(
			Composite parent, IDomainObject object, EAttribute attribute) {
		return new DefaultFieldPart(parent, object, attribute );
	}
}
