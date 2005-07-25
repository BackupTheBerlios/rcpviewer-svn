package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.IFieldBuilder;
import de.berlios.rcpviewer.gui.IFieldBuilder.IField;
import de.berlios.rcpviewer.gui.IFieldBuilder.IFieldListener;
import de.berlios.rcpviewer.gui.widgets.AbstractFormDisplay;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Displays / edits arguments for an action on a domain object. 
 * @author Mike
 *
 */
class ActionArgsDisplay extends AbstractFormDisplay {

	private final IDomainObject _object;
	private final EOperation _op;
	private final Object[] _args;
	
	/**
	 * @param obj
	 * @param op
	 * @param args - might have <code>null</code> values but must be of
	 * corect size for the operation.
	 */
	ActionArgsDisplay( 
			IDomainObject obj, 
			EOperation op,
			Object[] args ) {
		super( new Shell( SWT.ON_TOP | SWT.MODELESS )  );
		if ( obj == null ) throw new IllegalArgumentException();
		if ( op == null ) throw new IllegalArgumentException();
		if ( args == null ) throw new IllegalArgumentException();
		if ( args.length != op.getEParameters().size() ) {
			throw new IllegalArgumentException();
		}
		
		// fields
		_object = obj;
		_op = op;
		_args = args;

		// size
		setMinSize( new Point( 100, 100 ) );
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.widgets.AbstractFormDisplay#open()
	 */
	@Override
	public int open() {
		
		// ok button
		final Button ok = addOKButton( ADD_NO_BEHAVIOUR );
		ok.addSelectionListener( new DefaultSelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				setCloseCode( SWT.OK );
				ok.getShell().close();
				new RunOperationJob( _object, _op, _args ).schedule();
			}
			
		});
		
		// cancel button
		addCancelButton( ADD_DEFAULT_BEHAVIOUR );
		
		// title
		getForm().setText( GuiPlugin.getResourceString( 
				"ActionArgsDisplay.Title" ) ); //$NON-NLS-1$
		
		// create main gui
		Composite body = getForm().getBody();
		body.setLayout( new GridLayout( 2, false ) );
		
		// loop through each parameter add gui field
		int index = 0;
		for ( Object obj : _op.getEParameters() ) {
			final int finalIndex = index++;
			EParameter param = (EParameter)obj;
			Label label = getFormToolkit().createLabel( 
					body, 
					param.getName() + ":" ) ; //$NON-NLS-1$
			label.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
			label.setToolTipText(
					_object.getDomainClass()   // JAVA_5_FIXME
					       .getDescriptionFor( _op, finalIndex ) ) ;
			
			Composite fieldComposite = getFormToolkit().createComposite( body );
			fieldComposite.setLayoutData( 
					new GridData( GridData.FILL_HORIZONTAL ) );
			getFormToolkit().paintBordersFor( fieldComposite );
			IFieldBuilder fieldBuilder
				= GuiPlugin.getDefault().getFieldBuilder( param );
			IField field = fieldBuilder.createField(
					fieldComposite,
					param,
					new IFieldListener(){
						public void fieldModified( IField field ){
							Object guiValue = field.getGuiValue();
							if ( guiValue instanceof IDomainObject ) {
								guiValue = ((IDomainObject)guiValue).getPojo();
							}
							_args[finalIndex] = guiValue;
							setOKEnablement( ok );
						}
					} );
			field.setGuiValue( _args[finalIndex] );
		}
		
		setOKEnablement( ok );
		return super.open();
	}
	
	private void setOKEnablement( Button ok ) {
		assert ok != null;
		// eventually have to take into account null values
		boolean enabled = true;
		for ( Object obj : _args ) {
			if ( obj == null ) {
				enabled = false;
				break;
			}
		}
		ok.setEnabled( enabled );
	}

}
