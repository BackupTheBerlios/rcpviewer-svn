package com.acme.gui.exts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import mikespike3.gui.IEditorContentBuilder;
import mikespike3.model.AnotherEasyBean;
import mikespike3.model.EasyBean;

public class BlueFormBuilder implements IEditorContentBuilder {

	/* (non-Javadoc)
	 * @see mikespike3.gui.IEditorContentBuilder#isApplicable(java.lang.Class)
	 */
	public boolean isApplicable(Class clazz ) {
		if ( EasyBean.class.equals( clazz ) ) return true;
		if ( AnotherEasyBean.class.equals( clazz ) ) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see mikespike3.gui.IEditorContentBuilder#getDisplay()
	 */
	public String getDisplay() {
		return "Big Blue Form";
	}

	/* (non-Javadoc)
	 * @see mikespike3.gui.IEditorContentBuilder#createGui(org.eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	public void createGui(Composite parent, Object instance) {
		assert parent != null;
		parent.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );
	}

}
