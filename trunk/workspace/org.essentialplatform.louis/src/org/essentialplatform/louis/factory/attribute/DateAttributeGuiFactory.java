/**
 * 
 */
package org.essentialplatform.louis.factory.attribute;

import static org.essentialplatform.louis.LouisPlugin.DATE_FORMATTER;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.FontUtil;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;


/**
 * @author Mike
 *
 */
public class DateAttributeGuiFactory extends AbstractAttributeGuiFactory<Date,Text> {

	/**
	 * Returns <code>true</code> if model is an attribute of class 
	 * <code>String</code>.
	 * @param model
	 * @param parent
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof IDomainClass.IAttribute ) {
			return Date.class.isAssignableFrom( 
					((IDomainClass.IAttribute)model).getEAttribute().getEType().getInstanceClass() );
		}
		return false;
	}
	
	@Override
	protected Text createMainControl(
			Composite parent, 
			AbstractAttributeFormPart<Date, Text> part, 
			IDomainClass.IAttribute model, 
			GuiHints hints) {
		assert parent != null;
		
		final Text text = new Text( parent, SWT.CENTER );
		text.setBackground( parent.getBackground() );
		int numChars = DATE_FORMATTER.format( new Date() ).length();
		int charWidth = FontUtil.getCharWidth( 
				parent, FontUtil.CharWidthType.SAFE );
		GridData data = new GridData();
		data.widthHint = numChars * charWidth;
		text.setLayoutData( data );
		text.setEditable( false );
		text.setEnabled( isEditable( model, hints ) );
		return text;
	}
	
	@Override
	protected AbstractAttributeFormPart<Date, Text> createFormPart(
			IDomainClass.IAttribute model) {
		return new DateAttributeFormPart( model );
	}
	
	@Override
	protected Control[] createAdditionalEditControls(
			final Composite parent, 
			final AbstractAttributeFormPart<Date, Text> part, IDomainClass.IAttribute model, GuiHints hints) {
		assert parent != null;
		assert part != null;
		// change date via calendar widget
        Button button = new Button( parent, SWT.PUSH | SWT.FLAT );
		button.setText( "..."); //$NON-NLS-1$
		GridData data = new GridData();
		data.heightHint = part.getControl().getLineHeight() ;
		button.setLayoutData( data );
		button.addSelectionListener( new DefaultSelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
                final SWTCalendarDialog cal
                	= new SWTCalendarDialog( parent.getDisplay() );
                cal.addDateChangedListener(new SWTCalendarListener() {
                    public void dateChanged(SWTCalendarEvent calendarEvent) {
                        part.setValue( calendarEvent.getCalendar().getTime(), true );
                    }
                });
                if ( part.getUiValue() != null ) {
                	cal.setDate( part.getUiValue() );
                }
                cal.open();
            }
        });
		
		return new Control[]{ button };
	}
}
