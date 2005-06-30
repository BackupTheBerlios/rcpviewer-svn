package de.berlios.rcpviewer.gui.editors;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

/**
 * Used when no other available <code>IFieldBuilder</code> is applicable.
 * <br>Public as may be subclassed / delegated to.
 * @author Mike

 */
public class DefaultFieldBuilder implements IFieldBuilder {

	/**
	 * Always returns <code>true</code> - always applicable
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#isApplicable(java.lang.Class, java.lang.Object)
	 */
	public boolean isApplicable(Class clazz, Object value) {
		return true;
	}


	/**
	 * Creates an editable Text box displaying the toString() value.
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createFormPart(org.eclipse.swt.widgets.Composite, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object)
	 */
	public IFormPart createFormPart(Composite parent, Method getMethod,Method setMethod,Object configuration) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null
		
		return new DefaultFieldPart(parent, getMethod, setMethod);
	}
}
