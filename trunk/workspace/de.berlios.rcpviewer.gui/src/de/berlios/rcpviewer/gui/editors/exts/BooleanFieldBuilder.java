package de.berlios.rcpviewer.gui.editors.exts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.berlios.rcpviewer.gui.editors.IFieldBuilder;

public class BooleanFieldBuilder implements IFieldBuilder {

	
	public boolean isApplicable(Class clazz, Object value) {
		return Boolean.class ==  clazz;
	}

	/**
	 * Generates a label and a checkbox.
	 */
	public void createGui(Composite parent, Object value ) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null;
		
		parent.setLayout( new GridLayout() );
		Button checkbox = new Button( parent, SWT.CHECK );
		checkbox.setLayoutData( new GridData() );
		
		// does not handle null values very well
		if ( value != null ) {
			assert value instanceof Boolean;
			checkbox.setSelection( ((Boolean)value).booleanValue() );
		}


	}

}
