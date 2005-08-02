package de.berlios.rcpviewer.gui.fieldbuilders.exts;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
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

import de.berlios.rcpviewer.gui.dnd.DateTransfer;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.util.EmfUtil;

/**
 * Used for dates.
 * <br>Can handle DnD operations but only within the app.
 * <br>Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class DateFieldBuilder implements IFieldBuilder {
	
	private static final DateFormat FORMATTER
		= DateFormat.getDateInstance(DateFormat.SHORT);


	/**
	 * Only if the class is a <code>Date</code> or subclass.
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return Date.class.isAssignableFrom(
				element.getEType().getInstanceClass() );
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, ETypedElement element, IFieldListener listener) {
		if( parent == null ) throw new IllegalArgumentException();
		if( element == null ) throw new IllegalArgumentException();
		if( listener == null ) throw new IllegalArgumentException();
		return new DateField( parent, element, listener );
	}

	private class DateField implements IField {
		
		private final Text _text;
		
		DateField( final Composite parent, 
				   ETypedElement element,
				   final IFieldListener listener ) {
			
			GridLayout layout = new GridLayout();
			parent.setLayout( layout );
			
			// text box
			_text = new Text( parent, SWT.SINGLE );
			_text.setBackground( parent.getBackground() );
			_text.setEditable( false );
			_text.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
			
			if ( !EmfUtil.isModifiable( element ) ) {
				_text.setEditable( false );
				addDnD( _text, false );
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
				addDnD( _text, true );
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
			if( obj == null ) {
				_text.setText( "" ); //$NON-NLS-1$
			}
			else {
				if ( !(obj instanceof Date) ) {
					throw new IllegalArgumentException();
				}
				_text.setText( FORMATTER.format( (Date)obj ) );
			}
		}
		
		// add DnD functionality
		private void addDnD( final Text text, boolean editable ) {
			assert text != null;
			
			Transfer[] types = new Transfer[] {
					DateTransfer.getInstance() };
			int operations = DND.DROP_MOVE | DND.DROP_COPY ;
			
			final DragSource source = new DragSource (text, operations);
			source.setTransfer(types);
			source.addDragListener (new DragSourceListener () {
				public void dragStart(DragSourceEvent event) {
					try {
						FORMATTER.parse( text.getText () );
						event.doit = true;
					}
					catch (ParseException pe ) {
						event.doit = false;
					}
				}
				public void dragSetData (DragSourceEvent event) {
					try {
						event.data = FORMATTER.parse( text.getText () );
					}
					catch (ParseException pe ) {
						event.doit = false;
					}
				}
				public void dragFinished(DragSourceEvent event) {
					// does nowt
				}
			});

			if ( editable ) {
				DropTarget target = new DropTarget(text, operations);
				target.setTransfer(types);
				target.addDropListener (new DropTargetAdapter() {
					public void dragEnter(DropTargetEvent event){
						if ( !DateTransfer.getInstance().isSupportedType(
								event.currentDataType ) ) {
							event.detail = DND.DROP_NONE;
						}
					}
					public void drop(DropTargetEvent event) {
						if ( event.data == null) {
							event.detail = DND.DROP_NONE;
							return;
						}
						// note that all DnD ops converted to copy's 
						event.detail = DND.DROP_COPY;
						text.setText( FORMATTER.format( (Date)event.data  ) );
					}
				});
			}
		}
	}
}
