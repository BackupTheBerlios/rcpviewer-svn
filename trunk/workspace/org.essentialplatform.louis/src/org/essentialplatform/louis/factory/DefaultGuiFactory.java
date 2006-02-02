/**
 * 
 */
package org.essentialplatform.louis.factory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.StringUtil;


/**
 * Used when no other factory can be found - displays an error message.
 * 
 * @author Mike
 */
public class DefaultGuiFactory implements IGuiFactory<Object> {
	
	/**
	 * Always <code>true</code>.
	 * @param model
	 * @param context
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory context) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IGuiFactory#getDescription()
	 */
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}
	

	/**
	 * @param model
	 * @param toolkit
	 * @param parent
	 * @param hints
	 * @return
	 */
	public IFormPart createGui(
			Object model, 
			FormToolkit toolkit, 
			Composite parent, 
			GuiHints hints) {
		if( model == null ) throw new IllegalArgumentException();
		if( toolkit == null ) throw new IllegalArgumentException();
		if( parent == null ) throw new IllegalArgumentException();

		parent.setLayout( new FillLayout() );
		Text text = toolkit.createText(
				parent,
				StringUtil.printf(
					LouisPlugin.getResourceString( "DefaultGuiFactory.Msg" ), //$NON-NLS-1$	
					model.toString() ),
				SWT.WRAP );
		text.setEditable( false );
		text.setForeground( parent.getDisplay().getSystemColor( SWT.COLOR_RED ) );
		
		return new DummyFormPart();
	}
	
	private class DummyFormPart extends AbstractFormPart {
		
		// does nowt
	}
	
}
