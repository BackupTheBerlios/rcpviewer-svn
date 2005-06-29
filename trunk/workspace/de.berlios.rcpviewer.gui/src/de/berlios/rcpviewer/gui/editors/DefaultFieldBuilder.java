package de.berlios.rcpviewer.gui.editors;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

public class DefaultFieldBuilder implements IFieldBuilder {

	public boolean isApplicable(Class clazz, Object value) {
		// default - always applicable
		return true;
	}

	/**
	 * Creates an editable Text box displaying the toString() value.
	 */
	public IFormPart createFormPart(Composite parent, Method getMethod,Method setMethod,Object configuration) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null
		
		return new DefaultFieldPart(parent, getMethod, setMethod);
	}
}
