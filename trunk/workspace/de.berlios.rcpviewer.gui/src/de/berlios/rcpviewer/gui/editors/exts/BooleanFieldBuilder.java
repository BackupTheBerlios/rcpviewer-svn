package de.berlios.rcpviewer.gui.editors.exts;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.gui.editors.IFieldBuilder;

public class BooleanFieldBuilder implements IFieldBuilder {

	
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#isApplicable(java.lang.Class, java.lang.Object)
	 */
	public boolean isApplicable(Class clazz, Object value) {
		return Boolean.class ==  clazz;
	}


	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createFormPart(org.eclipse.swt.widgets.Composite, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object)
	 */
	public IFormPart createFormPart(Composite parent,Method getMethod,Method setMethod,Object configuration) {
		return new BooleanFieldPart(parent, getMethod, setMethod);
	}

}
