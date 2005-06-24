package de.berlios.rcpviewer.gui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


public class DefaultFieldBuilder implements IFieldBuilder {

	public boolean isApplicable(Class clazz, Object value) {
		// default - always applicable
		return true;
	}

	/**
	 * Creates an editable Text box displaying the toString() value.
	 */
	public void createGui(Composite parent, Object value ) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null
		
		parent.setLayout( new GridLayout() );
		Text text = new Text( parent, SWT.WRAP );
		text.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		String display = null;
		if ( value == null ) {
			display = "null";
		}
		else {
			display = value.toString();
		}
		text.setText( display );
	}
}
