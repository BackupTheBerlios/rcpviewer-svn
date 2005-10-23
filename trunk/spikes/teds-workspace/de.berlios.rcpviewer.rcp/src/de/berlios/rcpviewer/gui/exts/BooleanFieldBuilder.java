package de.berlios.rcpviewer.gui.exts;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.gui.IFieldBuilder;

public class BooleanFieldBuilder implements IFieldBuilder {

	
	public boolean isApplicable(Class clazz, Object value) {
		return Boolean.class ==  clazz;
	}

	/**
	 * Generates a label and a checkbox.
	 */
	public IFormPart createFormPart(Composite parent,Method getMethod,Method setMethod,Object configuration) {
		return new BooleanFieldPart(parent, getMethod, setMethod);
	}

}
