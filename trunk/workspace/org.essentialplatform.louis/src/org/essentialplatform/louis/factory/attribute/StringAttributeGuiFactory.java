/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import static org.essentialplatform.louis.util.FontUtil.CharWidthType.SAFE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.FontUtil;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;


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

		GridData gridData = new GridData();

		// multiline
		int textStyle;
		int multiLine = model.getMultiLine();
		if (multiLine != -1) {
			textStyle = SWT.MULTI + SWT.V_SCROLL;
			gridData.heightHint = FontUtil.getCharHeight(parent) * multiLine; 
		} else {
			textStyle = SWT.WRAP;
			gridData.verticalAlignment = SWT.FILL;
			gridData.grabExcessVerticalSpace = true;
		}

		final Text text = new Text( parent, textStyle );
		text.setBackground( parent.getBackground() );

		// field length
		int fieldLength = model.getFieldLengthOf(); 
		if (fieldLength != -1) {
			gridData.widthHint = FontUtil.getCharWidth( parent, SAFE ) * fieldLength;
		} else {
			gridData.horizontalAlignment = SWT.FILL;
			gridData.grabExcessHorizontalSpace = true;
		}

		// min length (applies only if gridData.grabExcessHorizontalSpace = false)
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
					part.setValue( enteredText, false );
				};
			});
			// traverse listener for validating (eg Regex)
			text.addTraverseListener(new TraverseListener() {
				public void keyTraversed(TraverseEvent e) {
					String enteredText = text.getText();
					if (!model.isValid(enteredText)) {
						e.doit = false;
						text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
					} else {
						text.setBackground(parent.getBackground());
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
