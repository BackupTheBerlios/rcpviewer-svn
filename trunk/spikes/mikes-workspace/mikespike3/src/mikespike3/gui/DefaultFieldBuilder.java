package mikespike3.gui;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DefaultFieldBuilder implements IFieldBuilder {


	/**
	 * Creates a label based on method's name and an editable Text box
	 * displaying the value.
	 */
	public void createGui(Composite parent, Method method, Object instance) {
		if ( parent == null ) throw new IllegalArgumentException();
		if ( method == null ) throw new IllegalArgumentException();
		if ( instance == null ) throw new IllegalArgumentException();
		
		parent.setLayout( new GridLayout( 2, false ) );
		
		// add label
		Label label = new Label( parent, SWT.NONE );
		label.setLayoutData( new GridData() );
		assert method.getName().startsWith( "get" );
		label.setText( method.getName().substring( 3 ) + ":" );
		label.setBackground( parent.getBackground() );
		
		// add text
		Text text = new Text( parent, SWT.WRAP );
		text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL) );
		
		// set text diplay as value.toString();
		try {
			assert method.getParameterTypes().length == 0;
			Object value = method.invoke( instance, (Object[])null );
			text.setText( value.toString() );
		}
		catch ( Exception ex ) {
			text.setText( ex.toString() );
		}
		
	}
	
	

}
