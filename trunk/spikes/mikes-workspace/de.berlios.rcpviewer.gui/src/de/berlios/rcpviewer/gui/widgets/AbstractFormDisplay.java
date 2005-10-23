package de.berlios.rcpviewer.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Base class for displays that use a form gui
 */
public class AbstractFormDisplay {
	
	protected static final boolean ADD_DEFAULT_BEHAVIOUR = true;
	protected static final boolean ADD_NO_BEHAVIOUR = false;
	
	private final Shell _shell;
	
	// access via lazy instantiators
	private FormToolkit _toolkit = null;
	private ScrolledForm _form = null;
	private Composite _buttonsArea = null;
	
	// modifed by subclasses via protected methods
	private int _buttonCount = 0;
	private Point _minSize = null;
	private int _closeCode = SWT.CANCEL;
	

	/**
	 * @param parent
	 */
	public AbstractFormDisplay( Shell parent ) {
		if ( parent == null ) throw new IllegalArgumentException();
		_shell = parent;
	}
	
	/** 
	 * Main method - will create and open the form.
	 * Returns <code>SWT.CANCEL</code> or whatever subclasses set
	 * via<code>setCloseCode()</code>
	 * @return
	 */
	public int open() {	
		
		// layout
		GridLayout layout = new GridLayout();
        layout.horizontalSpacing= 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
		getShell().setLayout( layout );
		getShell().setBackground( getFormToolkit().getColors().getBackground() );
		
		
		// maybe a preferred size
		if ( _minSize != null ) {
			// default sizing looks daft so this is worth it
			Point preferredSize = getShell().computeSize( SWT.DEFAULT, SWT.DEFAULT );
			if ( preferredSize.x < _minSize.x ) preferredSize.x = _minSize.x;
			if ( preferredSize.y < _minSize.y ) preferredSize.y = _minSize.y;
			getShell().setSize( preferredSize );
		}
		
		// position in middle of screen
		Rectangle displayArea
			= getShell().getDisplay().getPrimaryMonitor().getClientArea();
		getShell().setLocation( 
				displayArea.width/2 - getShell().getSize().x/2, 
				displayArea.height/2 - getShell().getSize().y/2);
		
		getShell().open();
		Display display = getShell().getDisplay();
		while (!getShell().isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return _closeCode;
	}
	
	/**
	 * Accessor to toolkit
	 * @return
	 */
	protected FormToolkit getFormToolkit() {
		if ( _toolkit == null ) {
			_toolkit = new FormToolkit( getShell().getDisplay() );
			getShell().addDisposeListener( new DisposeListener(){
				public void widgetDisposed(DisposeEvent e) {
					_toolkit.dispose();
				}			
			});	
		}
		assert _toolkit != null;
		return _toolkit;
	}
	
	/**
	 * Accessor to form - does <b>not</b> set layout for this.
	 * @return
	 */
	protected ScrolledForm getForm() {
		if ( _form == null ) {
	    	_form = getFormToolkit().createScrolledForm( getShell()  );
			_toolkit.paintBordersFor( _form.getBody() );
			_form.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}
		assert _form != null;
		return _form;
	}
	
	/**
	 * Add button with standard 'OK' label
	 * <br>param indicates whether to apply default close behaviour to the
	 * button
	 * @return
	 */
	protected Button addOKButton( boolean addDefaultBehaviour ) {
		final Button button =  addButton( 
				GuiPlugin.getResourceString( "OK" ) ) ; //$NON-NLS-1$
		if ( addDefaultBehaviour ) {
			button.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent e) {
					setCloseCode( SWT.OK );
					button.getShell().close();
				}
			});
		}
		return button;
	}
	
	/**
	 * Add button with standard 'Cancel' label
	 * @return
	 */
	protected Button addCancelButton(  boolean addDefaultBehaviour ) {
		final Button button =  addButton( 
				GuiPlugin.getResourceString( "Cancel" ) ) ; //$NON-NLS-1$
		if ( addDefaultBehaviour ) {
			button.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent e) {
					setCloseCode( SWT.CANCEL );
					button.getShell().close();
				}
			});
		}
		return button;
	}
	
	/**
	 * Add button with text label to buttons area
	 * @param text
	 * @return
	 */
	protected Button addButton( String text ) {
		assert text != null;
		Composite parent = getButtonsArea();
		Button button = getFormToolkit().createButton( parent, text, SWT.CENTER );
		button.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL  ) );
		parent.setLayout( new GridLayout( ++_buttonCount, true ) ) ;
		return button;
	}
	
	/**
	 * Allow subclasses to set the return code for the
	 * <code>open</code> method
	 * @param i
	 */
	protected void setCloseCode( int i ) {
		_closeCode = i;
	}
	
	/**
	 * If subclasses are fussy about their size.
	 * @param size
	 */
	protected void setMinSize( Point size ) {
		assert size != null;
		_minSize = size;
	}
	
	/**
	 * Accessor
	 * @return
	 */
	protected Shell getShell() {
		return _shell;
	}
	
	// lazily built
	private Composite getButtonsArea() {
		if ( _form == null ) getForm();
		if ( _buttonsArea == null ) {
			_buttonsArea = getFormToolkit().createComposite( getShell() );
			getFormToolkit().paintBordersFor( _buttonsArea );
			_buttonsArea.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		}
		assert _buttonsArea != null;
		return _buttonsArea;
	}
}