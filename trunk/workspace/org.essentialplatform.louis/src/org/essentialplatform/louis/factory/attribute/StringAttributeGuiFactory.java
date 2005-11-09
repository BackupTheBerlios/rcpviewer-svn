/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;


/**
 * @author Mike
 *
 */
public class StringAttributeGuiFactory extends AbstractAttributeGuiFactory<String,Text> {

	/**
	 * Returns <code>true</code> if model is an attribute of class 
	 * <code>String</code>.
	 * @param model
	 * @param parent
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof IDomainClass.IAttribute ) {
			return String.class.equals( 
					((IDomainClass.IAttribute)model).getEAttribute().getEType().getInstanceClass() );
		}
		return false;
	}
	

	/**
	 * Adds a <code>SWT.WRAP</code> field that fills the area.
	 */
	@Override
	protected Text createMainControl(
			Composite parent, 
			final AbstractAttributeFormPart<String, Text> part, 
			IDomainClass.IAttribute model, 
			GuiHints hints) {
		assert parent != null;
		final Text text = new Text( parent, SWT.WRAP );
		text.setBackground( parent.getBackground() );
		text.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		// editable behaviour
		boolean editable = isEditable( model, hints );
		text.setEnabled( editable );
		if ( editable ) {
			text.addModifyListener( new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					part.setValue( text.getText(), false );
				};
			});
		}
		
		return text;
	}
	
	@Override
	protected AbstractAttributeFormPart<String, Text> createFormPart(
			IDomainClass.IAttribute model) {
		return new StringAttributeFormPart( model );
	}
}
