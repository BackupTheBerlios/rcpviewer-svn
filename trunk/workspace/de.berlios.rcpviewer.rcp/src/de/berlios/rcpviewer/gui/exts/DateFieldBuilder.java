package de.berlios.rcpviewer.gui.exts;

import java.lang.reflect.Method;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.gui.IFieldBuilder;

/**
 * Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class DateFieldBuilder implements IFieldBuilder {

	
	public boolean isApplicable(Class clazz, Object value) {
		return Date.class ==  clazz;
	}

	/**
	 * Generates a label and a checkbox.
	 */
	
	public IFormPart createFormPart(Composite parent, Method getMethod, Method setMethod,Object configuration) {
		if ( parent == null ) throw new IllegalArgumentException();
		return new DateFieldPart(parent, getMethod, setMethod);
	}

}
