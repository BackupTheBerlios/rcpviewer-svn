/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.gui.dnd.DomainObjectTransfer;
import de.berlios.rcpviewer.gui.jobs.OpenDomainObjectJob;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Used in one of alternate views of a reference collection
 * @author Mike
 */
class CollectionMasterDetailsBlock extends MasterDetailsBlock {
	
	private final CollectionGuiPage _page;
	private final boolean _allowAdd;
	private final IFormPart _parent;
	private final EReference _ref;
	private final FormToolkit _toolkit;
	private final CollectionChildDetailsPage _details;
	
	CollectionMasterDetailsBlock( 
			CollectionGuiPage page,
			boolean allowAdd,
			IFormPart parent,
			EReference ref, 
			FormToolkit toolkit ) {
		assert page != null;
		assert parent != null;
		assert ref != null;
		assert toolkit != null;
		_page = page;
		_allowAdd = allowAdd;
		_parent = parent;
		_toolkit = toolkit;
		_ref = ref;
		_details = new CollectionChildDetailsPage();
	}
	
//	/**
//	 * Overridden to set sash form proportions
//	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createContent(org.eclipse.ui.forms.IManagedForm)
//	 */
//	@Override
//	public void createContent(IManagedForm managedForm) {
//		super.createContent(managedForm);
//        sashForm.setWeights( new int[]{ 1, 1 } );
//	}



	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createMasterPart(
			final IManagedForm managedForm, Composite parent) {
		
		// configure parent
		_toolkit.adapt( parent );
		_toolkit.paintBordersFor( parent );
		parent.setLayout( new GridLayout() );
		
		// actual instantiation and placement in parent
		ListViewer viewer = new ListViewer( 
				parent, 
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL );
		List list = viewer.getList();
		list.setData( FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER );
		list.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		_toolkit.adapt( list,  true, true );
		
		// label provider - assumes a domain class for now
		final IRuntimeDomainClass<?> collectionDomainType
			= (IRuntimeDomainClass<?>)RuntimeDomain.instance().lookupNoRegister( 
					(Class<?>)_ref.getEType().getInstanceClass() );
		viewer.setLabelProvider( GuiPlugin.getDefault().getLabelProvider() );
		
		// content provider an inner class as quite complicated
		viewer.setContentProvider( new CollectionContentProvider( _parent, _ref ) );

		// open listener - dbl-click opens editor for selected item
		viewer.addOpenListener( new IOpenListener(){
		    public void open( OpenEvent event ){
		    	Object obj =
		    		((StructuredSelection)event.getSelection()).getFirstElement();
		    	// defensive programing!
		    	if ( obj != null && obj instanceof IDomainObject ) {
		    		UIJob job = new OpenDomainObjectJob( (IDomainObject)obj );
		    		job.schedule();
		    	}
		    }
		});
		
		// for sync'ing details page
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged( _details, event.getSelection());
			}
		});
		
		// always a drag source
		final DragSource dragSource = new DragSource( 
				list, 
				DND.DROP_MOVE | DND.DROP_COPY );
		final DomainObjectTransfer transfer = (DomainObjectTransfer)
			DndTransferFactory.getTransfer(  _ref.getEType().getInstanceClass() );
		dragSource.setTransfer( new Transfer[]{ transfer } );
		dragSource.addDragListener ( 
				new CollectionDragSourceListener( viewer ) );

		// drag target if can add:
		if ( _allowAdd ) {
			final DropTarget target = new DropTarget( 
					list, 
					DND.DROP_MOVE | DND.DROP_COPY );
			target.setTransfer( new Transfer[]{ transfer } );
			target.addDropListener ( 
					new CollectionDropTargetAdapter( viewer, _ref, transfer ) );
		}

		// finally record viewer reference
		_page.setViewer( viewer );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage( DomainObject.class, _details ) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// does nowt
	}

}
