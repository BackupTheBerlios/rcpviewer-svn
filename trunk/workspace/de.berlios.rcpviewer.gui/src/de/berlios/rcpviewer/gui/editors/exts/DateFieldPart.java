package de.berlios.rcpviewer.gui.editors.exts;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.jobs.SetAttributeJob;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
class DateFieldPart implements IFormPart {
	
	private final Text _text;
	private final DateFormat _formatter;
	private final EAttribute _attribute;
	
	private IDomainObject _object;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	
	/**
	 * @param parent
	 * @param object
	 * @param attribute
	 */
	DateFieldPart( final Composite parent, 
				   IDomainObject object, 
				   EAttribute attribute) {
		assert parent != null;
		assert object != null;
		assert attribute != null;
		
		_object = object;
		_attribute = attribute;
		_formatter = DateFormat.getDateInstance(DateFormat.SHORT);
		
		GridLayout layout = new GridLayout();
		parent.setLayout( layout );
		
		// text box
		_text = new Text( parent, SWT.SINGLE );
		_text.setBackground( parent.getBackground() );
		_text.setEditable( false );
		_text.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		
		if ( !attribute.isChangeable() ) {
			_text.setEditable( false );
		}
		else {
			layout.makeColumnsEqualWidth = false;
			layout.numColumns = 2;
		
			_text.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				};
			});
			
			// change date via calendar widget
	        Button change = new Button( parent, SWT.PUSH | SWT.FLAT );
			change.setText( "...");
			GridData buttonData = new GridData();
			buttonData.heightHint = _text.getLineHeight() ;
			change.setLayoutData( buttonData );
			change.addListener( SWT.Selection, new Listener() {
	            public void handleEvent(Event event) {
	                final SWTCalendarDialog cal
	                	= new SWTCalendarDialog( parent.getDisplay() );
	                cal.addDateChangedListener(new SWTCalendarListener() {
	                    public void dateChanged(SWTCalendarEvent calendarEvent) {
	                        _text.setText(
								_formatter.format(
										calendarEvent.getCalendar().getTime()));
	                    }
	                });

	                if ( _text.getText() != null && _text.getText().length() > 0) {
	                    try {
	                        Date d = _formatter.parse(_text.getText());
	                        cal.setDate(d);
	                    } 
						catch (ParseException pe) {
							// do nowt
	                    }
	                }
	                cal.open();

	            }
	        });
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		try {
			if ( _attribute.isChangeable() ) {
				Date date = _formatter.parse(_text.getText());
				new SetAttributeJob( _object, _attribute, date ).schedule();
			}
			setDirty(false);
		} 
		catch ( ParseException e) {
			Status status= new Status(
					IStatus.WARNING, 
					GuiPlugin.getDefault().getBundle().getSymbolicName(), 
					0, 
					e.getMessage(), 
					e);
			GuiPlugin.getDefault().getLog().log(status);
		} 
	}

	/**
	 * @param value
	 */
	private void setDirty(boolean value) {
		if (_isDirty != value) {
			_isDirty= value;
			_managedForm.dirtyStateChanged();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		// do nothing		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm pForm) {
		_managedForm= pForm;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return _isDirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		Date date = (Date)_object.get( _attribute );
		_text.setText( _formatter.format( date ) );
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_text.setFocus();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object pInput) {
		if ( !(pInput instanceof IDomainObject ) ) {
			throw new IllegalArgumentException();
		}
		_object = (IDomainObject)pInput;
		refresh();
		return true;		
	}


}
