package org.essentialplatform.louis.celleditors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.LouisPlugin;


/**
 * 'Typesafe' cell editor for primitives so that getters and setter
 * return appropriate types.
 * <br>This is a slightly dubious implementation as its superclass is not meant 
 * to be extended.  However the core implementation is unaffected, with only the 
 * getter and setter functionality decorated.
 * @author Mike
 */
class PrimitiveCellEditor extends TextCellEditor {
	
	private static final Map<Class,Class> WRAPPERS = new HashMap<Class,Class>();
	
	static {
		// nb : does not do boolean 
		WRAPPERS.put( byte.class, Byte.class );
		WRAPPERS.put( char.class, Character.class );
		WRAPPERS.put( short.class, Short.class );
		WRAPPERS.put( int.class, Integer.class );
		WRAPPERS.put( long.class, Long.class );
		WRAPPERS.put( float.class, Float.class );
		WRAPPERS.put( double.class, Double.class );
	}
	
	private final Class _type;
	private final Method _valueOfMethod;
	
	/**
	 * The editor must be attached to the parent control after constrcution
	 * using the <code>create()</code> method.
	 * @see org.eclipse.jface.viewers.CellEditor#create(org.eclipse.swt.widgets.Composite)
	 */
	PrimitiveCellEditor( Class type ) {
		super();
		assert type != null;
		_type = type;
		
		// store reference to valueOf method - char does not have one
		if ( type == char.class ) {
			_valueOfMethod = null;
		}
		else { 
			try {
				_valueOfMethod = WRAPPERS.get( _type ).getMethod( 
						"valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			}
			catch ( NoSuchMethodException nsfe ) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	

	/**
	 * Does not use standardvalidation via <code>ICellEditorValidator</code>.
	 * <br>Instead veryify in the underlying text control.
	 * @see org.eclipse.jface.viewers.TextCellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		final Text text = (Text)super.createControl(parent);
		text.addVerifyListener( new VerifyListener(){
			public void verifyText(VerifyEvent event){
				// build resultant text
				String resultantText;
				String orig = text.getText();
				if ( orig.length() == 0 ) {
					resultantText = event.text;
				}
				else {
					StringBuffer sb = new StringBuffer();
					sb.append( orig.substring( 0, event.start ) );
					sb.append( event.text );
					sb.append( orig.substring( event.end, orig.length() ) );
					resultantText = sb.toString();
				}
				
				// blank string equates to null so OK
				if ( resultantText.length() == 0 ) return;
				
				// char a special case
				if ( _type == char.class ) {
					event.doit = ( resultantText.length() < 2 );
					return;
				}
				
				// check this text value could be converted to type
				try {
					_valueOfMethod.invoke( null, new Object[]{resultantText} );
				}
				catch ( Exception ex ) {
					event.doit = false;
				}
			}
		} );
		return text;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.TextCellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		String s = (String)super.doGetValue();
		if ( s.length() == 0 ) return null;
		
		// char a special case
		if ( _type == char.class ) {
			assert s.length() == 1;
			return s.charAt(0);
		}
		
		try {
			return _valueOfMethod.invoke( null, new Object[]{s} );
		}
		catch ( Exception ex ) {
			assert false;
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.TextCellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		String s;
		if ( value == null ) {
			s = "" ;//$NON-NLS-1$
		}
		else {
			if ( !isApplicableType( value.getClass() ) ) {
				throw new IllegalArgumentException();
			}
			s = LouisPlugin.getText( value );
		}
		super.doSetValue( s );
	}
	
	// whether an applicable type
	private boolean isApplicableType( Class type ) {
		if ( _type == type ) return true;
		if ( WRAPPERS.get( _type ) == type ) return true;
		return false;
	}
}
