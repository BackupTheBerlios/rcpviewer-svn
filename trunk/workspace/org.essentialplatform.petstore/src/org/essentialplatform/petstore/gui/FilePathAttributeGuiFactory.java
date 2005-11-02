/**
 * 
 */
package org.essentialplatform.petstore.gui;

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.factory.attribute.AbstractAttributeFormPart;
import org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

import org.essentialplatform.petstore.domain.FilePath;


/**
 * @author Mike
 *
 */
public class FilePathAttributeGuiFactory
		extends AbstractAttributeGuiFactory<FilePath,Text> {

	/**
	 * Returns <code>true</code> if model is an attribute of class 
	 * <code>String</code>.
	 * @param model
	 * @param parent
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof EAttribute ) {
			return Date.class.isAssignableFrom( 
					((EAttribute)model).getEType().getInstanceClass() );
		}
		return false;
	}
	
	@Override
	protected Text createMainControl(
			Composite parent, 
			AbstractAttributeFormPart<FilePath, Text> part, 
			EAttribute model,
			GuiHints hints ) {
		assert parent != null;
		
		final Text text = new Text( parent, SWT.CENTER );
		text.setEnabled( isEditable( model, hints ) );
		text.setEditable( false );
		text.setBackground( parent.getBackground() );
		return text;
	}
	
	@Override
	protected AbstractAttributeFormPart<FilePath, Text> createFormPart(
			EAttribute model) {
		return new FilePathAttributeFormPart( model );
	}
	
	@Override
	protected Control[] createAdditionalEditControls(
			final Composite parent, 
			final AbstractAttributeFormPart<FilePath, Text> part, 
			EAttribute model,
			GuiHints hints ) {
		assert parent != null;
		assert part != null;
        Button change = new Button( parent, SWT.PUSH | SWT.FLAT );
		change.setText( "..."); //$NON-NLS-1$
		GridData buttonData = new GridData();
		buttonData.heightHint = part.getControl().getLineHeight() ;
		change.setLayoutData( buttonData );
		change.addSelectionListener( new DefaultSelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog( 
						parent.getShell(),
						SWT.OPEN );
				dialog.setFileName( part.getControl().getText() );
				String path = dialog.open();
				if ( path != null ) {
					FilePath fp = new FilePath();
					fp.parseString( path );
					part.setValue( fp, true );
				}
			}
			
		} );
		
		return new Control[]{ change };
	}
}
