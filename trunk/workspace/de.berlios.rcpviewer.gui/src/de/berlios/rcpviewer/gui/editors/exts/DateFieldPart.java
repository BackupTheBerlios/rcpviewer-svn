package de.berlios.rcpviewer.gui.editors.exts;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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

/**
 * Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class DateFieldPart implements IFormPart {
	private Composite _parent;
	private Method _getMethod;
	private Method _setMethod;
	private Object _input;
	private Text _text;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;
	// data format
	private DateFormat _formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);


	public DateFieldPart(Composite parent, Method getMethod,Method setMethod) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null

		_parent= parent;
		_getMethod= getMethod;
		_setMethod= setMethod;
		
		parent.setLayout( new GridLayout( 2, false ) );
		
		// text box
		_text = new Text( parent, SWT.SINGLE );
		_text.setBackground( parent.getBackground() );
		_text.setEditable( false );
		_text.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		_text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
				_managedForm.dirtyStateChanged();
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
                final SWTCalendarDialog cal = new SWTCalendarDialog( _parent.getDisplay() );
                cal.addDateChangedListener(new SWTCalendarListener() {
                    public void dateChanged(SWTCalendarEvent calendarEvent) {
                        _text.setText(_formatter.format(calendarEvent.getCalendar().getTime()));
                    }
                });

                if ( _text.getText() != null && _text.getText().length() > 0) {
                    try {
                        Date d = _formatter.parse(_text.getText());
                        cal.setDate(d);
                    } catch (ParseException pe) {

                    }
                }
                cal.open();

            }
        });
	}
	
	public void commit(boolean pOnSave) {
		try {
			if (_setMethod != null) {
				Date date= _formatter.parse(_text.getText());
				_setMethod.invoke(_input, new Object[] { date });
			}
			setDirty(false);
		} catch (Exception e) {
			Status status= new Status(
					IStatus.WARNING, 
					GuiPlugin.getDefault().getBundle().getSymbolicName(), 
					0, 
					e.getMessage(), 
					e);
			GuiPlugin.getDefault().getLog().log(status);
		} 
	}

	private void setDirty(boolean value) {
		if (_isDirty != value) {
			_isDirty= value;
			_managedForm.dirtyStateChanged();
		}
	}

	public void dispose() {
		// do nothing		
	}

	public void initialize(IManagedForm pForm) {
		_managedForm= pForm;		
	}

	public boolean isDirty() {
		return _isDirty;
	}

	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	public void refresh() {
		if ( _input != null ) {
			try {
			Date date= (Date)_getMethod.invoke(_input);
			_text.setText( _formatter.format( date ) );
			}
			catch (Exception x) {
				throw new RuntimeException(x);
			}
		}
		else
			_text.setText("");
		setDirty(false);
	}

	public void setFocus() {
		_text.setFocus();		
	}

	public boolean setFormInput(Object pInput) {
		_input= pInput;
		refresh();
		return true;		
	}


}
