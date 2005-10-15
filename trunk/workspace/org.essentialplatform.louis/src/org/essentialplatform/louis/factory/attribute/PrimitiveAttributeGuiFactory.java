/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import static org.essentialplatform.louis.util.FontUtil.CharWidthType.SAFE;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.FontUtil;
import org.essentialplatform.louis.util.PrimitiveUtil;
import org.essentialplatform.louis.util.SWTUtil;


/**
 * For all primitive types except <code>Boolean</code> and <code>Character</code>.
 * @author Mike
 */
public class PrimitiveAttributeGuiFactory<T> extends AbstractAttributeGuiFactory<T,Text> {

	/**
	 * Returns <code>true</code> if model is an attribute of class 
	 * <code>Character</code> or <code>char</code>
	 * @param model
	 * @param parent
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof EAttribute ) {
			Class attributeClass = ((EAttribute)model).getEType().getInstanceClass();
			if ( attributeClass.isPrimitive() 
					|| PrimitiveUtil.isWrapperClass( attributeClass )) {
				return PrimitiveUtil.hasValueOfMethod( attributeClass );
			}
		}
		return false;
	}
	

	/**
	 * Defines size of field and adds a verify listener
	 * @see org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory#createMainControl(org.eclipse.swt.widgets.Composite, AbstractAttributeFormPart, EAttribute, GuiHints)
	 */
	@Override
	protected Text createMainControl(
			Composite parent, 
			final AbstractAttributeFormPart<T, Text> part, 
			EAttribute model, 
			GuiHints hints) {
		assert parent != null;
		assert model != null;
		
		// decide width & style of text box
		Class type = model.getEType().getInstanceClass();
		GridData data;
		int style = Integer.MIN_VALUE;
		if ( type == byte.class || type == Byte.class ) {
			data = new GridData();
			data.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 5;
			style = SWT.CENTER;
		}
		else if ( type == short.class || type == Short.class ) {
			data = new GridData();
			data.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 4;
			style = SWT.LEFT;
		}
		else if ( type == int.class || type == Integer.class ) {
			data = new GridData();
			data.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 10;
			style = SWT.LEFT;
		}
		else if ( type == long.class || type == Long.class ) {
			data = new GridData();
			data.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 19;
			style = SWT.LEFT;
		}
		else if ( type == float.class || type == Float.class ) {
			data = new GridData( GridData.FILL_HORIZONTAL );
			style = SWT.LEFT;
		}
		else {
			assert ( type == double.class || type == Double.class );
			data = new GridData( GridData.FILL_HORIZONTAL );
			style = SWT.LEFT;
		}
		assert style != Integer.MIN_VALUE;
		
		// field
		final Text text = new Text( parent, style );
		text.setBackground( parent.getBackground() );
		text.setLayoutData( data );
		
		// editable behaviour
		boolean editable = isEditable( model, hints );
		text.setEnabled( editable );
		if ( editable ) {
			
			// get valueof method
			final Method valueOfMethod = PrimitiveUtil.getValueOfMethod( type );
			assert valueOfMethod != null;
			
			// modify
			text.addModifyListener( new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					String s = text.getText().trim();
					T value;
					if ( s.length() == 0 ) {
						value =  null;
					}
					else {
						try {
							value = (T)valueOfMethod.invoke( null, new Object[]{s} );
						}
						catch( Exception ex ) {
							assert false;
							value = null;
						}
					}
					part.setValue( value, false );
				};
			});
			
			// verify
			text.addVerifyListener( new VerifyListener(){
				public void verifyText(VerifyEvent event){
					String sVal = SWTUtil.buildResultantText( text, event );
					if ( sVal.length() == 0 ) return;
					// check this text value could be converted to type
					try {
						valueOfMethod.invoke( null, new Object[]{sVal} );
					}
					catch ( Exception ex ) {
						event.doit = false;
					}
				}
			} );
		}


		
		return text;
	}
	
	@Override
	protected AbstractAttributeFormPart<T, Text> createFormPart(
			EAttribute model) {
		return new PrimitiveAttributeFormPart<T>( model );
	}
}
