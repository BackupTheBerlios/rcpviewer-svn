package org.essentialplatform.louis.factory.reference.collection;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.factory.DomainClassGuiFactory;
import org.essentialplatform.louis.factory.DomainClassPart;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener;
import org.essentialplatform.louis.util.EmfUtil;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.session.IDomainObject;

public class CollectionMasterChildGuiFactory implements IGuiFactory<EReference> {
	
	/**
	 * Returns <code>true</code> if a multiple reference and parent is
	 * a <code>CollectionGuiFactory</code>
	 * @param model
	 * @param context
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if( parent == null ) return false;
		if ( !( parent instanceof CollectionGuiFactory ) ) return false;
		if ( model instanceof EReference ) {
			return ((EReference)model).isMany();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.factory.IGuiFactory#getDescription()
	 */
	public String getDescription() {
		return LouisPlugin.getResourceString( 
				"CollectionMasterChildGuiFactory.Description"); //$NON-NLS-1$
	}


	/**
	 * @param model
	 * @param mForm
	 * @param parent
	 * @param hints
	 * @return
	 */
	public CollectionMasterChildPart createGui(
			EReference model, 
			FormToolkit toolkit,
			Composite parent, 
			GuiHints hints) {
		if( model == null ) throw new IllegalArgumentException();
		if( toolkit == null ) throw new IllegalArgumentException();
		if( parent == null ) throw new IllegalArgumentException();
		if( hints == null ) throw new IllegalArgumentException();
		
		// layout
        parent.setLayout( new FillLayout() );
		
		// create sash 
		SashForm sash = new SashForm( parent ,SWT.HORIZONTAL);
		sash.setBackground( parent.getBackground() );
		sash.setLayout( new FillLayout() );
		
		// create 'master' composite
		Composite masterComposite  = new Composite( sash, SWT.NONE );
		masterComposite.setBackground( sash.getBackground() );
		toolkit.paintBordersFor( masterComposite );

		// create 'master' table
		CollectionTableGuiFactory masterFactory = new CollectionTableGuiFactory();
		CollectionTablePart masterPart = masterFactory.createGui(
				model,
				toolkit,
				masterComposite,
				new GuiHints( GuiHints.NONE ) );
		
		// create 'child' composite
		Composite childComposite  = new Composite( sash, SWT.NONE );
		childComposite.setBackground( sash.getBackground() );
		toolkit.paintBordersFor( childComposite );
		childComposite.setLayout( new FillLayout() );
		
		// create 'child' attribute list gui
		final ManagedForm childForm = new ManagedForm( childComposite );
		IRuntimeDomainClass<?> dClass = EmfUtil.getCollectionDomainType( model );
		DomainClassGuiFactory childFactory = new DomainClassGuiFactory();
		DomainClassPart childPart = childFactory.createGui( 
				dClass, 
				toolkit, 
				childForm.getForm().getBody(),
				new GuiHints( 
						GuiHints.INCLUDE_ATTRIBUTES | GuiHints.COMPACT | GuiHints.READ_ONLY, 
						dClass, 
						childForm.getForm().getBody() ) );
		childForm.addPart( childPart );

		// sync master and child
		masterPart.addDisplayListener( new IReferencePartDisplayListener(){
			public void displayValueChanged( IDomainObject<?> value  ){
				if ( value == null ) {
					childForm.setInput( null );
					childForm.getForm().setVisible( false );
				}
				else {
					childForm.setInput( value );
					if ( !childForm.getForm().isVisible() ) {
						childForm.getForm().setVisible( true );
					}
				}
			}
		});
		
		
		// initial state
		childForm.getForm().setVisible( false );
		sash.setWeights( new int[] { 50, 50 } );
		
		return new CollectionMasterChildPart( masterPart, childPart );
	}
}
