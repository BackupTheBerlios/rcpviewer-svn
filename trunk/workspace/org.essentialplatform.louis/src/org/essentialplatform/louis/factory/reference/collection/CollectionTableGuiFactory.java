package org.essentialplatform.louis.factory.reference.collection;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.essentialplatform.core.deployment.IReferenceBinding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.runtime.client.domain.bindings.IReferenceClientBinding;

public class CollectionTableGuiFactory implements IGuiFactory<IDomainClass.IReference> {

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
		if ( model instanceof IDomainClass.IReference ) {
			return ((IDomainClass.IReference)model).isMultiple();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IGuiFactory#getDescription()
	 */
	public String getDescription() {
		return LouisPlugin.getResourceString( 
				"CollectionTableGuiFactory.Description"); //$NON-NLS-1$
	}
	
	/**
	 * @param model
	 * @param mForm
	 * @param parent
	 * @param hints
	 * @return
	 */
	public CollectionTablePart createGui(
			IDomainClass.IReference model, 
			FormToolkit toolkit,
			Composite parent, 
			GuiHints hints) {
		if( model == null ) throw new IllegalArgumentException();
		if( toolkit == null ) throw new IllegalArgumentException();
		if( parent == null ) throw new IllegalArgumentException();
		if( hints == null ) throw new IllegalArgumentException();
		
		// model data
		IDomainClass collectionDomainType = model.getReferencedDomainClass();
		// TODO: was calling EmfUtil here
		List<IDomainClass.IAttribute> iAttributes = collectionDomainType.iAttributes();

		// layout
		parent.setLayout( new GridLayout() );
		
		// viewer
		final TableViewer viewer = new TableViewer( 
				parent, 
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL ){
			@Override
			public void refresh() {
				// always ensure initial column can be fully read as this
				// column hold the reference name
				super.refresh();
				getTable().getColumn( 0 ).pack();
			}
		};
		final Table table = viewer.getTable();
		table.setData( FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER );
		table.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		toolkit.adapt( table, true, true );
		
		// controller
		CollectionTablePart part = new CollectionTablePart( model, viewer );

		// columns for table - first is fixed
		new TableColumn( table, SWT.LEFT ).setText( 	
			LouisPlugin.getResourceString( 
				"CollectionTableGuiFactory.FirstColumnHeader") ); //$NON-NLS-1$
		if ( hints.styleMatches( GuiHints.INCLUDE_ATTRIBUTES ) ) {
			for ( IDomainClass.IAttribute iAttribute : iAttributes ) { 
				TableColumn column = new TableColumn( table, SWT.LEFT );
				column.setText( iAttribute.getName() );
				part.addAttribute( iAttribute, column );
			}
			table.setLinesVisible( true );
			table.setHeaderVisible( true );
		}

		// label provider 
		viewer.setLabelProvider( 
				new CollectionTableLabelProvider( iAttributes ) );

		// content provider a seperate class as quite complicated
		viewer.setContentProvider( new CollectionTableContentProvider() );

		// open listener - dbl-click opens editor for selected item
		viewer.addOpenListener( new CollectionOpenListener() );
		
		// always a drag source
		final DragSource dragSource = new DragSource( 
				table, 
				DND.DROP_MOVE | DND.DROP_COPY );
		final Transfer transfer = LouisPlugin.getTransfer(  
				model.getReferencedDomainClass().getEClass().getInstanceClass() );
		dragSource.setTransfer( new Transfer[]{ transfer } );
		dragSource.addDragListener ( 
				new CollectionTableDragSourceListener( viewer ) );

		// drag target if can add:
		final IReferenceClientBinding referenceBinding = 
			(IReferenceClientBinding)model.getBinding();
		if ( referenceBinding.canAssociate() ) {
			final DropTarget target = new DropTarget( 
					table, 
					DND.DROP_MOVE | DND.DROP_COPY );
			target.setTransfer( new Transfer[]{ transfer } );
			target.addDropListener ( 
					new CollectionTableDropTargetAdapter( part, model, transfer ) );
		}
		
		return part;
	}
}
