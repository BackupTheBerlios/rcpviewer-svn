/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.gui.dnd.DomainObjectTransfer;
import de.berlios.rcpviewer.gui.jobs.AddReferenceJob;
import de.berlios.rcpviewer.gui.jobs.OpenDomainObjectJob;
import de.berlios.rcpviewer.gui.jobs.RemoveReferenceJob;
import de.berlios.rcpviewer.gui.jobs.SearchJob;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.gui.util.StringUtil;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;
import de.berlios.rcpviewer.gui.widgets.ErrorInput;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.ISession;

/**
 * Generic form part for <code>isMany() EReference's</code> (i.e. collections)
 *  of <code>IDomainObject</code>'s.
 * @author Mike
 */
class CollectionPart implements IReferencePart {
	
	private final ListViewer _viewer;
	
	private boolean _isDirty = false;


	/**
	 * Gui creation done here rather than in initialize() method
	 * as this results in less state to hold onto
	 * @param object - cannot be <code>null</code>
	 * @param ref - cannot be <code>null</code> and must represent a multiple reference
	 * @param parent - cannot be <code>null</code>
	 * @param toolkit - cannot be <code>null</code>
	 * @param columnwidths - can be <code>null</code>
	 */
	CollectionPart( IDomainObject<?> object,
				   final EReference ref,
				   Composite sectionParent, 
				   FormToolkit toolkit,
				   int[] columnWidths,
				   int maxLabelLength ) {
		assert object != null;
		assert ref != null;
		assert ref.isMany();
		assert sectionParent != null;
		assert toolkit != null;
		// column widths can be null
		assert maxLabelLength != 0;
		
		// section		
		sectionParent.setLayout( new FillLayout() );
		Section section = toolkit.createSection( sectionParent, EXPANDABLE_STYLE );
		section.setText( 
				StringUtil.padLeft( ref.getName(), maxLabelLength ) + ":"  ); //$NON-NLS-1$
		
		// section area
		Composite parent = toolkit.createComposite( section ) ;
		section.setClient( parent );
		toolkit.paintBordersFor( parent );
		
		// fetch all required metadata
		boolean allowAdd = ( 
				object.getDomainClass().getAssociatorFor( ref ) != null );
		boolean allowRemove = ( 
				object.getDomainClass().getDissociatorFor( ref ) != null );
		final Class<?> collectionPojoType = ref.getEType().getInstanceClass();
		final IRuntimeDomainClass<?> collectionDomainType
			= (IRuntimeDomainClass<?>)RuntimeDomain.instance().lookupNoRegister( 
					collectionPojoType );
		assert collectionPojoType != null;
		assert collectionDomainType != null;
		
		// layout
		toolkit.adapt( parent );
		GridLayout layout = new GridLayout( 1, false );
		if ( allowAdd || allowRemove ) layout.numColumns = 2;
		parent.setLayout( layout );
		
		// list viewer - offloaded to private method for tidiness
		_viewer = createViewer( allowAdd, 
							    allowRemove,  
							    ref,
								collectionDomainType, 
							    toolkit, 
							    parent );
				
		// buttons - offloaded to private method for tidiness
		createButtons( allowAdd, 
					   allowRemove, 
					   ref, 
					   collectionDomainType, 
					   toolkit, 
					   parent,
					   _viewer );
	}

	/* IFormPart contract */
	
	/**
	 * Creates GUI.
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return _isDirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		if ( onSave ) _isDirty = false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		_viewer.setInput( input );
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_viewer.getList().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		assert _viewer != null;
		_viewer.refresh();
	}
	
	/* private methods */
	
	// just to tidy constructor code
	private ListViewer createViewer(
			boolean allowAdd,
			boolean allowRemove,
			final EReference ref,
			final IRuntimeDomainClass collectionDomainType,
			FormToolkit toolkit,
			Composite parent ) {
		
		// actual instantiation and placement in parent
		ListViewer viewer = new ListViewer( 
				parent, 
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL );
		List list = viewer.getList();
		list.setData( FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER );
		toolkit.adapt( list,  true, true );
		GridData listData = new GridData( GridData.FILL_BOTH );
		if ( allowAdd && allowRemove ) listData.verticalSpan = 2;
		list.setLayoutData( listData  ) ;
		
		// label provider - assumes a domain class for now
		viewer.setLabelProvider( 
				GuiPlugin.getDefault().getLabelProvider( collectionDomainType) );
		
		// content provider an inner class as quite complicated
		viewer.setContentProvider( new InnerContentProvider( ref ) );

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
		
		// always a drag source
		final DragSource dragSource = new DragSource( 
				list, 
				DND.DROP_MOVE | DND.DROP_COPY );
		final DomainObjectTransfer transfer = (DomainObjectTransfer)
			DndTransferFactory.getTransfer(  ref.getEType().getInstanceClass() );
		dragSource.setTransfer( new Transfer[]{ transfer } );
		dragSource.addDragListener ( new InnerDragSourceListener( viewer ) );

		// drag target if can add:
		if ( allowAdd ) {
			final DropTarget target = new DropTarget( 
					list, 
					DND.DROP_MOVE | DND.DROP_COPY );
			target.setTransfer( new Transfer[]{ transfer } );
			target.addDropListener ( new InnerDropTargetAdapter( ref, transfer ) );
		}
		
		return viewer;
	}
	
	
	// just to tidy constructor code
	private void createButtons(
			boolean allowAdd,
			boolean allowRemove,
			final EReference ref,
			final IRuntimeDomainClass collectionDomainType,
			FormToolkit toolkit,
			Composite parent,
			final Viewer viewer ) {
		if ( allowAdd ) {
			Button add = toolkit.createButton( parent, "", SWT.PUSH ); //$NON-NLS-1$
			add.setImage( ImageUtil.getImage(
		   			GuiPlugin.getDefault(), "/icons/add.png" ) ) ; //$NON-NLS-1$
			add.setLayoutData(
					new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
			add.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					Job job = new SearchJob( collectionDomainType );
					job.schedule();
				}
			});
		}
		if ( allowRemove ) {
			final Button remove = toolkit.createButton( parent, "", SWT.PUSH ); //$NON-NLS-1$
			remove.setLayoutData(
					new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
			remove.setImage( ImageUtil.getImage(
		   			GuiPlugin.getDefault(), "/icons/remove.png" ) ); //$NON-NLS-1$ );
			remove.setEnabled( false );
			remove.setVisible( false );
			viewer.addSelectionChangedListener( new ISelectionChangedListener(){
				public void selectionChanged(SelectionChangedEvent event){
					boolean enabled = !event.getSelection().isEmpty();
					remove.setEnabled( enabled );
					remove.setVisible( enabled );
				}
			});
			remove.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					ISelection selection = viewer.getSelection();
					assert !selection.isEmpty(); 
					// single selection viewer so
					Object selected
						= ((StructuredSelection)selection).getFirstElement();
					if ( selected instanceof IDomainObject ) {
						Job job = new RemoveReferenceJob(
								((IDomainObject<?>)viewer.getInput()),
								ref,
								(IDomainObject<?>)selected );
						job.schedule();
					}
				}	
			});
		}
	}
	
	/* private classes - where too big to be clear as anonymous */
	
	/**
	 * Extracts pojo collection contents and wraps in IDomainObjects 
	 * - does exlicitly - does not rely on aspect introduction
	 * Note this is also used to tie a listener to the object that updates 
	 * the display whenever domain object updated
	 */
	private class InnerContentProvider implements IStructuredContentProvider {
		
		private final EReference _ref;
		private final IDomainObjectReferenceListener _refListener;

		InnerContentProvider( EReference ref ) {
			assert ref != null;
			_ref = ref;
			_refListener = new IDomainObjectReferenceListener(){
				public void collectionAddedTo(DomainObjectReferenceEvent event) {
					refresh();
				}
				public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
					refresh();
				}
				public void referenceChanged(DomainObjectReferenceEvent event) {
					// does nowt
				}
			};
		}
		
		public Object[] getElements(Object obj) {
			if ( obj == null ) return new Object[0];
			assert obj instanceof IDomainObject;
			try {
				IDomainObject<?> dObj = (IDomainObject)obj;
				final ISession session
					= RuntimePlugin.getDefault().getSessionManager().get(
						dObj.getSessionId() );
				Collection<IDomainObject> elements
					= new ArrayList<IDomainObject>();
				
				Class<?> collectionPojoType = _ref.getEType().getInstanceClass();
				for( Object pojo : dObj.getReference( _ref ).getCollection() ) {
					IDomainObject dElem = session.getDomainObjectFor( 
							pojo, collectionPojoType );
					assert dElem != null;
					elements.add( dElem );	
				}
				return elements.toArray();
			}
			catch ( CoreException ce ) {
				GuiPlugin.getDefault().getLog().log( ce.getStatus() );
				return new Object[] { new ErrorInput() } ;
			}
		}
		public void dispose() {
			// does nowt
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// note this handles disposal of the listener too as the viewer's
			// input is set to null
			if ( oldInput != null && oldInput instanceof IDomainObject ) {
				((IDomainObject<?>)oldInput)
					.getReference( _ref )
					.removeDomainObjectReferenceListener( _refListener );
			}
			if ( newInput != null && newInput instanceof IDomainObject ) {
				((IDomainObject<?>)newInput)
					.getReference( _ref )
					.addDomainObjectReferenceListener( _refListener );
			}
			
		}
	}
	
	private class InnerDragSourceListener implements DragSourceListener {
		
		private final ListViewer _dsViewer;
		
		InnerDragSourceListener( ListViewer viewer ) {
			assert viewer != null;
			_dsViewer = viewer;
		}
		
		public void dragStart(DragSourceEvent event) {
			event.doit = false;
			ISelection selection = _dsViewer.getSelection();
			if ( !selection.isEmpty() ) {
				Object obj = ((StructuredSelection)selection).getFirstElement();
				if ( obj instanceof IDomainObject ) {
					event.doit = true;
				}
			}
		}
		public void dragSetData (DragSourceEvent event) {
			Object obj = ((StructuredSelection)_viewer.getSelection())
													  .getFirstElement();
			assert obj instanceof IDomainObject;
			event.data = obj;
		}
		public void dragFinished(DragSourceEvent event) {
			// does nowt
		}
	}
	
	private class InnerDropTargetAdapter extends DropTargetAdapter {
		
		private final EReference _ref;
		private final DomainObjectTransfer _transfer;
		private boolean _checkUnique = false;
		
		InnerDropTargetAdapter( EReference ref, DomainObjectTransfer transfer ) {
			super();
			assert ref != null;
			assert transfer != null;
			_ref = ref;
			_transfer = transfer;
			_checkUnique = _ref.isUnique();
		}
		
		// ensure DnD can work
		public void dragEnter(DropTargetEvent event) {
			if ( event.detail == DND.DROP_NONE ) {
				event.detail = DND.DROP_MOVE;
			}
		}
		
		// if possible, visually indicate unique constraints
		public void dragOver(DropTargetEvent event){		
			if ( _checkUnique ) {
				// this only works on some platforms...
				IDomainObject<?> dObj = (IDomainObject<?>)
					_transfer.nativeToJava(event.currentDataType);
				// ...so if returns null don't bother any more
				if ( dObj == null ) {
					_checkUnique = false;
				}
				else {
					if ( getCollection().contains( dObj.getPojo() ) ) {
						event.detail = DND.DROP_NONE;
					}
					else {
						event.detail = DND.DROP_MOVE;
					}
				}
			}
		}	
		
		// adds reference
		public void drop(DropTargetEvent event) {
			if ( event.data != null 
					&& event.data instanceof IDomainObject ) {
				IDomainObject<?> dObj = (IDomainObject<?>)event.data;
				// if cannot check uniqueness in dragOver, do now
				if ( _ref.isUnique() && !_checkUnique ) {
					if( getCollection().contains( dObj.getPojo() ) ) {
						MessageDialog.openError(
								null,
								GuiPlugin.getResourceString( 
										"CollectionPart.UniqueTitle" ), //$NON-NLS-1$
								GuiPlugin.getResourceString( 
										"CollectionPart.UniqueMsg" ) ); //$NON-NLS-1$
						return;
					}
				}
				// ok - add
				Job job = new AddReferenceJob(
						(IDomainObject<?>)_viewer.getInput(),
						_ref,
						dObj );
				job.schedule();
			}
			else {
				event.detail = DND.DROP_NONE;
			}
		}
		
		private Collection<?> getCollection(){
			IDomainObject<?> parent= (IDomainObject<?>)_viewer.getInput();
			return parent.getReference( _ref ).getCollection();
		}
	}
}
