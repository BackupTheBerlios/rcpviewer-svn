package de.berlios.rcpviewer.gui.editors.exts;

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.gui.editors.IFieldBuilder;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class DateFieldBuilder implements IFieldBuilder {

	/**
	 * Only if the class is a <code>Date</code> or subclass.
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isApplicable(EAttribute attribute) {
		return Date.class.isAssignableFrom(
				attribute.getEType().getInstanceClass() );
	}

	/**
	 * Displays date with button option to change it.
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createFormPart(org.eclipse.swt.widgets.Composite, de.berlios.rcpviewer.session.IDomainObject, org.eclipse.emf.ecore.EAttribute)
	 */
	public IFormPart createFormPart(
			Composite parent, IDomainObject object, EAttribute attribute) {
		return new DateFieldPart( parent, object, attribute );
	}
}
