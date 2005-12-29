/**
 * 
 */
package org.essentialplatform.louis.factory.reference;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.essentialplatform.core.deployment.IReferenceBinding;
import org.essentialplatform.core.deployment.IReferenceClientBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.ConfigureWidgetFactory;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.jobs.SearchJob;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.util.StringUtil;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Generic form part for single references to <code>IDomainObject</code>'s.
 * @author Mike
 */
public class ReferenceGuiFactory implements IGuiFactory<IDomainClass.IReference> {
	
	/**
	 * Returns <code>true</code> if a single reference
	 * @param model
	 * @param context
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory context) {
		if( model == null ) throw new IllegalArgumentException();
		if ( model instanceof IDomainClass.IReference ) {
			return !((IDomainClass.IReference)model).isMultiple();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IGuiFactory#getDescription()
	 */
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}
	

	/**
	 * @param model
	 * @param mForm
	 * @param parent
	 * @param hints
	 * @return
	 */
	public ReferencePart createGui(
			IDomainClass.IReference model, 
			FormToolkit toolkit, 
			Composite parent, 
			GuiHints hints) {
		if( model == null ) throw new IllegalArgumentException();
		if( toolkit == null ) throw new IllegalArgumentException();
		if( parent == null ) throw new IllegalArgumentException();
		if( hints == null ) throw new IllegalArgumentException();
		
		// section		
		parent.setLayout( new FillLayout() );
		Section section = toolkit.createSection( 
				parent, SECTION_STYLE );
		section.setText(
			StringUtil.padLeft( 
					model.getName(), hints.getMaxLabelLength() ) + ":"  ); //$NON-NLS-1$

		// section header composite
		Composite sectionHeader = toolkit.createComposite( section );
		sectionHeader.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		toolkit.paintBordersFor( sectionHeader );
		section.setTextClient( sectionHeader );

		// section area
		Composite sectionArea = toolkit.createComposite( section ) ;
		sectionArea.setBackground( section.getBackground() );
		section.setClient( sectionArea );
		
		// create attribute list gui
		IDomainClass dClass = model.getReferencedDomainClass();
		IGuiFactory detailsFactory = LouisPlugin.getDefault().getGuiFactory(
				dClass, this );
		IFormPart detailsPart = detailsFactory.createGui( 
				dClass, 
				toolkit,
				sectionArea,
				new GuiHints( 
						GuiHints.INCLUDE_ATTRIBUTES | GuiHints.COMPACT | GuiHints.READ_ONLY, 
						dClass, 
						sectionArea ) );
		
		// main controller 
		ReferencePart part = new ReferencePart( model, detailsPart );
		
		// as it says
		Text field = buildSectionHeaderContents( 
				model,
				toolkit,
				sectionHeader,
				part );
		
		
		return part;
	}
	
	/* private gui generation methids */
	
	private Text buildSectionHeaderContents( 
			IDomainClass.IReference model,
			FormToolkit toolkit,
			Composite parent,
			final ReferencePart part ){
		assert model != null;
		assert toolkit != null;
		assert parent != null;
		assert part != null;
		
		// model data
		final IDomainClass dClass = model.getReferencedDomainClass();
		
		// count columns for layout
		int numColumns = 0;
		
		// type icon
		numColumns++;
		Label label = toolkit.createLabel( parent, "" ); //$NON-NLS-1$
		GridData labelData = new GridData();
		label.setLayoutData( labelData );
		label.setImage( 
				ImageUtil.resize(
					ImageUtil.getImage( dClass ),
				PART_ICON_SIZE ) );	

		// main field
		numColumns++;
		Text field = createField( model, parent, toolkit, part );
		GridData fieldData = new GridData( GridData.FILL_HORIZONTAL );
		fieldData.heightHint = PART_ICON_SIZE.y;
		field.setLayoutData( fieldData );
		
		// add button
		final IReferenceClientBinding referenceBinding = 
			(IReferenceClientBinding)model.getBinding();
		if ( referenceBinding.canAssociate() ) {
			numColumns++;
			Button add = toolkit.createButton( parent, "", SWT.PUSH ); //$NON-NLS-1$
			add.setImage( 
				ImageUtil.resize( 
					ImageUtil.getImage(
							LouisPlugin.getDefault(), 
							"/icons/add.png" ), //$NON-NLS-1$
					PART_ICON_SIZE ) );		
			GridData addData = new GridData();
			addData.widthHint = PART_ICON_SIZE.x;
			addData.heightHint = PART_ICON_SIZE.y;
			add.setLayoutData( addData );
			add.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					Job job = new SearchJob( dClass );
					job.schedule();
					// whatever the result, pass to part.setValue()...
				}
			});
		}
		
		if ( referenceBinding.canDissociate() ) {
			numColumns++;
			final Button remove = toolkit.createButton( 
					parent, "", SWT.PUSH ); //$NON-NLS-1$
			remove.setImage( 
				ImageUtil.resize( 
					ImageUtil.getImage(
							LouisPlugin.getDefault(), 
							"/icons/remove.png" ), //$NON-NLS-1$
					PART_ICON_SIZE ) );	
			GridData removeData = new GridData();
			removeData.widthHint = PART_ICON_SIZE.x;
			removeData.heightHint = PART_ICON_SIZE.y;
			remove.setLayoutData( removeData );
			remove.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					part.setValue( null );
				}
			});
			remove.setEnabled( false );
			part.addDisplayListener( new IReferencePartDisplayListener(){
				public void displayValueChanged(IDomainObject<?> value) {
					remove.setEnabled( value != null );
					remove.setVisible( value != null );
				}
			} );
		}
		
		// configure gui button
		numColumns++;
		ConfigureWidgetFactory.createButton( parent, toolkit, part );

		// finally, layout
		GridLayout layout = new GridLayout( numColumns, false );
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		parent.setLayout(  layout );
		
		return field;
	}
	
	private Text createField( 
			IDomainClass.IReference reference,
			Composite parent, 
			FormToolkit toolkit,
			final ReferencePart part ){
		assert reference != null;
		assert parent != null;
		assert toolkit != null;
		assert part != null;
		
		// create field
		Text field = toolkit.createText( parent, "" ); //$NON-NLS-1$
		field.setEditable( false );
		part.setControl( field );
		
		// DnD - always as a drag source
		final DragSource dragSource = new DragSource( 
				field, 
				DND.DROP_MOVE | DND.DROP_COPY );
		Transfer transfer = LouisPlugin.getTransfer( 
				reference.getReferencedDomainClass().getEClass().getInstanceClass() );
		dragSource.setTransfer( new Transfer[]{ transfer } );
		dragSource.addDragListener (new DragSourceListener () {
			public void dragStart(DragSourceEvent event) {
				event.doit = ( part.getUiValue() != null );
			}
			public void dragSetData (DragSourceEvent event) {
				event.data = part.getUiValue();
			}
			public void dragFinished(DragSourceEvent event) {
				// does nowt
			}
		});
		
		// DnD - drop target?
		final IReferenceClientBinding referenceBinding = 
			(IReferenceClientBinding)reference.getBinding();
		if ( referenceBinding.canAssociate() ) {
			final DropTarget target = new DropTarget( 
					field, 
					DND.DROP_MOVE | DND.DROP_COPY );
			target.setTransfer( new Transfer[]{ transfer } );
			target.addDropListener ( new DropTargetAdapter() {
				// ensure DnD can work
				public void dragEnter(DropTargetEvent event) {
					if ( event.detail == DND.DROP_NONE ) {
						event.detail = DND.DROP_MOVE;
					}
				}
				// adds reference
				public void drop(DropTargetEvent event) {
					if ( event.data != null 
							&& event.data instanceof IDomainObject ) {
						part.setValue( (IDomainObject<?>)event.data ) ;
					}
					else {
						event.detail = DND.DROP_NONE;
					}
				}
			});
		}
		return field;

	}
}
