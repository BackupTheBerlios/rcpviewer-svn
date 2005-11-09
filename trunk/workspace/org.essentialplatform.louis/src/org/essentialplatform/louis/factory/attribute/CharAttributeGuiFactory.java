/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import static org.essentialplatform.louis.util.FontUtil.CharWidthType.SAFE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.FontUtil;
import org.essentialplatform.louis.util.SWTUtil;


/**
 * @author Mike
 */
public class CharAttributeGuiFactory extends AbstractAttributeGuiFactory<Character,Text> {

	/**
	 * Returns <code>true</code> if model is an attribute of class 
	 * <code>Character</code> or <code>char</code>
	 * @param model
	 * @param parent
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof IDomainClass.IAttribute ) {
			Class attributeClass = ((IDomainClass.IAttribute)model).getEAttribute().getEType().getInstanceClass();
			if ( char.class == attributeClass ) return true;
			if ( Character.class == attributeClass ) return true;
		}
		return false;
	}
	

	@Override
	protected Text createMainControl(
			Composite parent, 
			final AbstractAttributeFormPart<Character, Text> part, 
			IDomainClass.IAttribute model, 
			GuiHints hints) {
		assert parent != null;
		
		// field
		final Text text = new Text( parent, SWT.CENTER );
		text.setBackground( parent.getBackground() );
		
		// data
		GridData data = new GridData();
		data.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 3;
		text.setLayoutData( data );
		
		// editable behaviour
		boolean editable = isEditable( model, hints );
		text.setEnabled( editable );
		if ( editable ) {
			text.addModifyListener( new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					Character value;
					if ( text.getText().length() == 0 ) {
						value = null;
					}
					else {
						value = text.getText().charAt( 0 );
					}
					part.setValue( value, false );
				};
			});
			
			// verify
			text.addVerifyListener( new VerifyListener(){
				public void verifyText(VerifyEvent event){
					String sVal = SWTUtil.buildResultantText( text, event );
					event.doit = ( sVal.length() < 2 );
				}
			} );
		}

		return text;
	}
	
	@Override
	protected AbstractAttributeFormPart<Character, Text> createFormPart(
			IDomainClass.IAttribute model) {
		return new CharAttributeFormPart( model );
	}
}
