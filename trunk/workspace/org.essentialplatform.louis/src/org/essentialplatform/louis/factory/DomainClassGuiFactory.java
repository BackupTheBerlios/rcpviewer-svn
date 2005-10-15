package org.essentialplatform.louis.factory;

import static org.essentialplatform.louis.util.EmfUtil.SortType.ALPHABETICAL;
import static org.essentialplatform.louis.util.EmfUtil.SortType.ANNOTATION;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.EmfUtil;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

public class DomainClassGuiFactory implements IGuiFactory<IRuntimeDomainClass<?>> {
	
	/**
	 * Returns <code>true</code> if model is an instance of 
	 * <code>IRuntimeDomainClass</code>
	 * @param model
	 * @param context
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory context) {
		if( model == null ) throw new IllegalArgumentException();
		return ( model instanceof IRuntimeDomainClass<?> );
	}
	
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.factory.IGuiFactory#getDescription()
	 */
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * @param model
	 * @param mForm
	 * @param parent
	 * @param hints
	 */
	public DomainClassPart createGui(
			IRuntimeDomainClass<?> model, 
			FormToolkit toolkit, 
			Composite parent,
			GuiHints hints) {
		if( model == null ) throw new IllegalArgumentException();
		if( toolkit == null ) throw new IllegalArgumentException();
		if( parent == null ) throw new IllegalArgumentException();
		if( hints == null ) throw new IllegalArgumentException();
		
		// layout - compact if necessary
		GridLayout layout = new GridLayout();
		if ( hints.styleMatches( GuiHints.COMPACT ) ) {
			layout.marginBottom = 0;
			layout.marginHeight = 0;
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.marginTop = 0;
			layout.marginWidth = 0;
			layout.verticalSpacing = 0;
		}
		parent.setLayout( layout );

		// override passed hints if a dummy
		if ( GuiHints.DUMMY == hints ) {
			hints = new GuiHints( GuiHints.ALL, model, parent );
		}
		
		// controller
		DomainClassPart part = new DomainClassPart( 
				model,
				!hints.styleMatches( GuiHints.READ_ONLY ) );
		
		// for error message
		boolean childrenAdded = false;
		
		if ( hints.styleMatches( GuiHints.INCLUDE_ATTRIBUTES ) ) {
			// loop through all attributes
			for ( EAttribute attribute : EmfUtil.sort( model.attributes(), ANNOTATION ) ) {       
	
				// composite 
				Composite composite = toolkit.createComposite( parent);
				composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				toolkit.paintBordersFor( composite );
				
				// call factory method
				IGuiFactory factory = LouisPlugin.getDefault().getGuiFactory(
						attribute, this );
				IFormPart attributePart = factory.createGui( 
						attribute, toolkit, composite, hints ) ;
				part.addChildPart( attributePart );
				part.addAttribute( attribute, composite );
				childrenAdded = true;
			}
		}
		
		if ( hints.styleMatches( GuiHints.INCLUDE_REFERENCES ) ) {
			// loop through all references
			for ( EReference reference : EmfUtil.sort( 
					model.references(), ALPHABETICAL ) ) {       
		
				// composite
				Composite composite = toolkit.createComposite( parent );
				composite.setLayoutData( 
						new GridData( GridData.FILL_HORIZONTAL ) );
				toolkit.paintBordersFor( composite );
				
				// call factory method
				IGuiFactory factory = LouisPlugin.getDefault().getGuiFactory(
						reference, this );
				IFormPart referencePart = factory.createGui( 
						reference, toolkit, composite, hints );
				part.addChildPart( referencePart );
				part.addReference( reference, composite );
				childrenAdded = true;
			}
		}
		
		if ( !childrenAdded ) {
			Label error = toolkit.createLabel( 
					parent, 
					LouisPlugin.getResourceString( 
							"DomainClassGuiFactory.NoChildren"), //$NON-NLS-1$
					SWT.WRAP );
			error.setForeground( parent.getDisplay().getSystemColor( SWT.COLOR_RED ) );
		}

		return part;
	}
}
