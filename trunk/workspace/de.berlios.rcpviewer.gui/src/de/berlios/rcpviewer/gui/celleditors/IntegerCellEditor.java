package de.berlios.rcpviewer.gui.celleditors;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * 'Typesafe' cell editor for <code>Integer</code>'s so that getters and setter
 * return <code>Integer</code> types.
 * <br>This is a slightly dubious implementation as its superclass is not meant 
 * to be extended.  However the core implementation is unaffected, with only the 
 * getter and setter functionality decorated.
 * @author Mike
 */
class IntegerCellEditor extends TextCellEditor {

	/**
	 * The editor must be attached to the parent control after constrcution
	 * using the <code>create()</code> method.
	 * @see org.eclipse.jface.viewers.CellEditor#create(org.eclipse.swt.widgets.Composite)
	 */
	IntegerCellEditor() {
		super();
		// use standard validator
		setValidator( new ICellEditorValidator(){
			public String isValid(Object value) {
				if ( value == null ) return null;
				if ( value instanceof Integer ) return null;
				try {
					new Integer( (String)value );
					return null;
				}
				catch ( NumberFormatException nfe ) {
					return "error"; //$NON-NLS-1$
				}
			}
		} );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.TextCellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		String s = (String)super.doGetValue();
		if( s == null ) return null;
		if( s.trim().length() == 0 ) return null;
		return new Integer( s );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.TextCellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		String s;
		if ( value == null ) {
			s = ""; //$NON-NLS-1$
		}
		else {
			s = String.valueOf( value );
		}
		super.doSetValue( s );
	}
}
