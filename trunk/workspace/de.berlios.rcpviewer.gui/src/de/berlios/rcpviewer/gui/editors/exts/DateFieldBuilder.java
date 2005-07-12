package de.berlios.rcpviewer.gui.editors.exts;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.emf.ecore.ETypedElement;
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
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

import de.berlios.rcpviewer.gui.fields.IFieldBuilder;

/**
 * Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class DateFieldBuilder implements IFieldBuilder {
	
	private static final DateFormat FORMATTER
		= DateFormat.getDateInstance(DateFormat.SHORT);


	/**
	 * Only if the class is a <code>Date</code> or subclass.
	 * @see de.berlios.rcpviewer.gui.fields.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return Date.class.isAssignableFrom(
				element.getEType().getInstanceClass() );
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, boolean editable, IFieldListener listener) {
		return new DateField( parent, editable, listener );
	}

	private class DateField implements IField {
		
		private final Text _text;
		
		DateField( final Composite parent, 
				   boolean editable,
				   final IFieldListener listener ) {
			
			GridLayout layout = new GridLayout();
			parent.setLayout( layout );
			
			// text box
			_text = new Text( parent, SWT.SINGLE );
			_text.setBackground( parent.getBackground() );
			_text.setEditable( false );
			_text.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
			
			if ( !editable ) {
				_text.setEditable( false );
			}
			else {
				layout.makeColumnsEqualWidth = false;
				layout.numColumns = 2;
			
				_text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						listener.fieldModified( DateField.this );
					};
				});
				
				// change date via calendar widget
		        Button change = new Button( parent, SWT.PUSH | SWT.FLAT );
				change.setText( "..."); //$NON-NLS-1$
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
										FORMATTER.format(
											calendarEvent.getCalendar().getTime()));
		                    }
		                });
						String s = _text.getText();
		                if ( s!= null && s.length() > 0) {
		                    try {
		                        Date d = FORMATTER.parse( s );
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
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#getGuiValue()
		 */
		public Object getGuiValue() {
			try {
				Date date = FORMATTER.parse(_text.getText());
				return date;
			}
			catch ( ParseException pe ) {
				return null;
			}
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setFocus()
		 */
		public void setFocus() {
			_text.setFocus();
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setGuiValue(java.lang.Object)
		 */
		public void setGuiValue(Object obj) {
			if ( !(obj instanceof Date) ) throw new IllegalArgumentException();
			_text.setText( FORMATTER.format( (Date)obj ) );
			
		}
	}
}
