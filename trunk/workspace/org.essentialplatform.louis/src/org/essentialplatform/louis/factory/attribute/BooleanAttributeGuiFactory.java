package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

public class BooleanAttributeGuiFactory 
	extends AbstractAttributeGuiFactory<Boolean, Button> {
	
	 /**
	 * Returns <code>true</code> if model is an attribute of class
	 * <code>Boolean</code> or <code>boolean</code>.
	 * @param model
	 * @param parent
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof IDomainClass.IAttribute ) {
			Class attributeClass = ((IDomainClass.IAttribute)model).getEAttribute().getEType().getInstanceClass();
			if ( boolean.class == attributeClass ) return true;
			if ( Boolean.class == attributeClass ) return true;
		}
		return false;
	}

	/*
	 * @see org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory#createFormPart(org.essentialplatform.core.domain.IDomainClass.IAttribute)
	 */
	@Override
	protected AbstractAttributeFormPart<Boolean, Button> createFormPart(
			IDomainClass.IAttribute model) {
		return new BooleanAttributeFormPart( model );
	}
	
	@Override
	protected Button createMainControl(
			Composite parent, 
			final AbstractAttributeFormPart<Boolean, Button> part, 
			IDomainClass.IAttribute model, 
			GuiHints hints) {
		
		final Button button= new Button( parent, SWT.CHECK );
		button.setBackground( parent.getBackground() );

		if ( isEditable( model, hints ) ) {
			button.addSelectionListener( new DefaultSelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					part.setValue( button.getSelection(), false );
				}
			});
		}
		else {
			button.setEnabled( false );
		}
		
		return button;
	}



	

}
