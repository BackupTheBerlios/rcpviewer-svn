package mikespike3.gui.exts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mikespike3.gui.IFieldBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

/**
 * Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class DateFieldBuilder implements IFieldBuilder {

	
	public boolean isApplicable(Class clazz, Object value) {
		return Date.class ==  clazz;
	}

	/**
	 * Generates a label and a checkbox.
	 */
	public void createGui( final Composite parent, Object value ) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null;
		
		parent.setLayout( new GridLayout( 2, false ) );
		
		// data format
		final SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");

		// text box
		final Text text = new Text( parent, SWT.SINGLE );
		text.setBackground( parent.getBackground() );
		text.setEditable( false );
		text.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		if ( value != null ) {
			assert value instanceof Date;
			text.setText( formatter.format( (Date)value ) );
		}
		
		// change date via calendar widget
        Button change = new Button( parent, SWT.PUSH | SWT.FLAT );
		change.setText( "...");
		GridData buttonData = new GridData();
		buttonData.heightHint = text.getLineHeight() ;
		change.setLayoutData( buttonData );
		change.addListener( SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                final SWTCalendarDialog cal = new SWTCalendarDialog( parent.getDisplay() );
                cal.addDateChangedListener(new SWTCalendarListener() {
                    public void dateChanged(SWTCalendarEvent calendarEvent) {
                        text.setText(formatter.format(calendarEvent.getCalendar().getTime()));
                    }
                });

                if ( text.getText() != null && text.getText().length() > 0) {
                    try {
                        Date d = formatter.parse(text.getText());
                        cal.setDate(d);
                    } catch (ParseException pe) {

                    }
                }
                cal.open();

            }
        });


	}

}
