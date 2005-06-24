package de.berlios.rcpviewer.gui.editors;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import de.berlios.rcpviewer.gui.GuiPlugin;


/**
 * Creates a scrolled form with a column of labels and a column of 
 * fields as presented by the <code>FieldBuilderFactory</code>.
 */
public class DefaultEditorContentBuilder implements IEditorContentBuilder {

	
	/* (non-Javadoc)
	 * @see mikespike3.gui.IEditorContentBuilder#isApplicable(java.lang.Class)
	 */
	public boolean isApplicable( Class clazz ) {
		// default - always applicable
		return true;
	}
	
	

	/* (non-Javadoc)
	 * @see mikespike3.gui.IEditorContentBuilder#getDisplay()
	 */
	public String getDisplay() {
		return "Default Form";
	}

	/**
	 * Currently does model stuff do.
	 */
	public void createGui(Composite parent, Object instance ) {
		if ( parent == null ) throw new IllegalArgumentException();
		if ( instance == null ) throw new IllegalArgumentException();
		
		// create helper for form creation
		final FormToolkit toolkit = new FormToolkit( parent.getDisplay() );
		parent.addDisposeListener( new DisposeListener(){
			public void widgetDisposed(DisposeEvent e){
				toolkit.dispose();
			}
		});
		
		// create form with two columns - one for labels, second for fields
		ScrolledForm form = toolkit.createScrolledForm( parent );
		form.setText( instance.getClass().getName() );
		Composite body = form.getBody();
		body.setLayout( new GridLayout( 2, false ) );

		// discover methods - stuff the model should do
		Method[] methods = instance.getClass().getMethods();
		
		// add a field for each getter method:
		for ( int i=0, num = methods.length; i < num ; i++ ) {
			String name = methods[i].getName();
			if ( name.startsWith( "get" ) ) {
				
				// label
				Label label = new Label( body, SWT.NONE );
				label.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
				label.setText( name.substring( 3 ) + ":" );
				label.setBackground( body.getBackground() );
				
				// create parent composite for field
				Composite fieldComposite = new Composite( body, SWT.NONE );
				fieldComposite.setLayoutData( 
						new GridData( GridData.FILL_HORIZONTAL ) );
				fieldComposite.setBackground( body.getBackground() );
				toolkit.paintBordersFor( fieldComposite );
				
				// get value and create approriate gui
				Object value = null;
				try {
					assert methods[i].getParameterTypes().length == 0;
					value = methods[i].invoke( instance, (Object[])null );
					// create correct field builder
					IFieldBuilder fieldBuilder
						= GuiPlugin.getDefault().getFieldBuilderFactory().getInstance( 
								methods[i].getReturnType(), value  );
					fieldBuilder.createGui( fieldComposite,value );
				}
				catch ( Exception ex ) {
					// exceptions must have specialist displays...
					IFieldBuilder fieldBuilder
						= GuiPlugin.getDefault().getFieldBuilderFactory().getInstance( 
								ex.getClass(), ex  );
					fieldBuilder.createGui( fieldComposite, ex );
				}	
			}
		}
		
		
	}

}
