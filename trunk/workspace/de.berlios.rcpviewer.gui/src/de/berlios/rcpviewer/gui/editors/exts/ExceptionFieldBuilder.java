package de.berlios.rcpviewer.gui.editors.exts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import de.berlios.rcpviewer.gui.editors.DefaultFieldBuilder;
import de.berlios.rcpviewer.gui.editors.IFieldBuilder;

/**
 * As it says.
 * @author Mike
 */
public class ExceptionFieldBuilder implements IFieldBuilder {

	private final DefaultFieldBuilder delegate = new DefaultFieldBuilder();
	
	/**
	 * Only if the class is an <code>Exception</code> or subclass.
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#isApplicable(java.lang.Class, java.lang.Object)
	 */
	public boolean isApplicable(Class clazz, Object value) {
		return Exception.class.isAssignableFrom( clazz );
	}

	/* *
	 * Delegates to the default field builder.
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createFormPart(org.eclipse.swt.widgets.Composite, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object)
	 */
	public IFormPart createFormPart(Composite parent,java.lang.reflect.Method getMethod,java.lang.reflect.Method setMethod,Object value) {
		return delegate.createFormPart( parent, getMethod, setMethod, value );
	}

}
