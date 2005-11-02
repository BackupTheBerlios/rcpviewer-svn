/**
 * 
 */
package org.essentialplatform.gui.acme.factory.exts;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.factory.attribute.AbstractAttributeFormPart;
import org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

/**
 * @author Mike
 *
 */
public class ColourAttributeGuiFactory extends AbstractAttributeGuiFactory<Color,Text> {


	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.IGuiFactory#isApplicable(java.lang.Object, org.essentialplatform.louis.factory.IGuiFactory)
	 */
	public boolean isApplicable(Object model, IGuiFactory parent) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof EAttribute ) {
			return Color.class.equals( 
					((EAttribute)model).getEType().getInstanceClass() );
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory#createFormPart(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected AbstractAttributeFormPart<Color, Text> createFormPart(EAttribute model) {
		return new ColourAttributeFormPart( model );
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory#createMainControl(org.eclipse.swt.widgets.Composite, org.essentialplatform.louis.factory.attribute.AbstractAttributeFormPart, org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected Text createMainControl(
			Composite parent, 
			AbstractAttributeFormPart<Color, Text> part, 
			EAttribute model, 
			GuiHints hints) {
		Text text = new Text( parent, SWT.NONE );
		text.setBackground( parent.getBackground() );
		text.setEditable( false );
		text.setEnabled( isEditable( model, hints ) );
		return text;
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.attribute.AbstractAttributeGuiFactory#createAdditionalEditControls(org.eclipse.swt.widgets.Composite, org.essentialplatform.louis.factory.attribute.AbstractAttributeFormPart, org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected Control[] createAdditionalEditControls(
			final Composite parent, 
			final AbstractAttributeFormPart<Color, Text> part, 
			EAttribute model, 
			GuiHints hints) {

		assert parent != null;
		assert part != null;
        Button change = new Button( parent, SWT.PUSH | SWT.FLAT );
		change.setText( "..."); //$NON-NLS-1$
		GridData buttonData = new GridData();
		buttonData.heightHint = part.getControl().getLineHeight() ;
		change.setLayoutData( buttonData );
		change.addSelectionListener( new DefaultSelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ColorDialog dialog = new ColorDialog( parent.getShell() );
				if ( part.getValue() != null ) {
					dialog.setRGB( part.getValue().getRGB() );
				}
				RGB rgb = dialog.open();
				if ( rgb != null ) {
					final Color color = new Color( parent.getDisplay(), rgb );
					parent.addDisposeListener( new DisposeListener(){
						public void widgetDisposed(DisposeEvent e) {
							color.dispose();
						}
					});
					part.setValue( color, true );
				}
			}
		} );
		
		return new Control[]{ change };
	}
}
