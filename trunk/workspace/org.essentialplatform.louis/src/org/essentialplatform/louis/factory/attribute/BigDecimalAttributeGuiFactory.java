/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.EmfUtil;
import org.essentialplatform.louis.util.SWTUtil;


/**
 * @author Mike
 */
public class BigDecimalAttributeGuiFactory
	extends AbstractAttributeGuiFactory<BigDecimal,Text> {

	/**
	 * Returns <code>true</code> if model is an attribute of class 
	 * <code>BigDecimal</code> or one of its subclasses.
	 * @param model
	 * @param parent
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof EAttribute ) {
			return BigDecimal.class.isAssignableFrom( 
					((EAttribute)model).getEType().getInstanceClass() );
		}
		return false;
	}
	

	/**
	 * Adds input verification to standard functionality.
	 * @see org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory#createMainControl(org.eclipse.swt.widgets.Composite, AbstractAttributeFormPart, EAttribute, GuiHints)
	 */
	@Override
	protected Text createMainControl(
			Composite parent, 
			final AbstractAttributeFormPart<BigDecimal, Text> part, 
			EAttribute model, 
			GuiHints hints) {
		assert parent != null;
		final Text text = new Text( parent, SWT.NONE );
		text.setBackground( parent.getBackground() );
		boolean editable = isEditable( model, hints );
		text.setEnabled( editable );
		if ( editable ) {
			text.addModifyListener( new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					String s = text.getText();
					BigDecimal value;
					if ( s.trim().length() == 0 ) {
						value = null;
					}
					else {
						value = new BigDecimal( s );
					}
					part.setValue( value, false );
				};
			} ) ;
			text.addVerifyListener( new VerifyListener(){
				public void verifyText(VerifyEvent event){
					// build resultant text
					String sVal = SWTUtil.buildResultantText( text, event );
					
					// blank string equates to null so OK
					if ( sVal.length() == 0 ) return;
					
					// check this text value could be converted to type
					try {
						new BigDecimal( sVal );
					}
					catch ( NumberFormatException nfe ) {
						event.doit = false;
					}
				}
			} );
		}
		return text;
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.attribute.AttributeGuiFactory#createFormPart(org.eclipse.emf.ecore.EAttribute, org.eclipse.swt.widgets.Text)
	 */
	@Override
	protected AbstractAttributeFormPart<BigDecimal,Text> createFormPart(
			EAttribute model) {
		return new BigDecimalAttributeFormPart( model );
	}


}
