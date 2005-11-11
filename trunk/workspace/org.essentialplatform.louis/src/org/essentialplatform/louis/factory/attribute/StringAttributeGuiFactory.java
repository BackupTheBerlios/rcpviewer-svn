/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import static org.essentialplatform.louis.util.FontUtil.CharWidthType.SAFE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.FontUtil;


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
			final Composite parent, 
			final AbstractAttributeFormPart<String, Text> part, 
			final IDomainClass.IAttribute model, 
			final GuiHints hints) {
		assert parent != null;
		
		final Text text = new Text( parent, SWT.WRAP );
		text.setBackground( parent.getBackground() );
		GridData gridData = new GridData( GridData.FILL_VERTICAL);

		// field length
		int fieldLength = model.getFieldLengthOf(); 
		if (fieldLength != -1) {
			gridData.widthHint = FontUtil.getCharWidth( parent, SAFE ) * fieldLength;
		} else {
			gridData.grabExcessHorizontalSpace = true;
		}
		
		// min length (applies only if not grabbing excess horizontal space)
		int minLength = model.getMinLengthOf();
		if (minLength != -1) {
			gridData.minimumWidth = FontUtil.getCharWidth( parent, SAFE ) * minLength;
		}
		
		text.setLayoutData( gridData );
		
		// editable behaviour
		boolean editable = isEditable( model, hints );
		text.setEnabled( editable );
		// max length
		if (model.getMaxLengthOf() != -1) {
			text.setTextLimit(model.getMaxLengthOf());
		}
		if ( editable ) {
			text.addModifyListener( new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					String enteredText = text.getText();
					// validate (eg Regex)
					// TODO: should really move to a TraverseEvent: don't want the field going yellow immediately...!
					if (model.isValid(enteredText)) {
						part.setValue( enteredText, false );
						text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
					} else {
						text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
					}
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
