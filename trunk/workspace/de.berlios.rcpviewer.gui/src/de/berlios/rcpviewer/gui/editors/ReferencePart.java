/**
 * 
 */
package de.berlios.rcpviewer.gui.editors;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EReference;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.gui.dnd.DomainObjectTransfer;
import de.berlios.rcpviewer.gui.jobs.AddReferenceJob;
import de.berlios.rcpviewer.gui.jobs.RemoveReferenceJob;
import de.berlios.rcpviewer.gui.jobs.SearchJob;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.gui.util.StatusUtil;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.ISession;

/**
 * Generic form part for single references to <code>IDomainObject</code>'s.
 * @author Mike
 */
class ReferencePart implements IFormPart {
	
	private final IDomainObject<?> _parent;
	private final EReference _ref;
	private final Text _text;
	private final IDomainObjectReferenceListener _listener;
	private final Button _removeButton;
	
	private IDomainObject<?> _refValue = null;
	
	private boolean _isDirty = false;


	/**
	 * Gui creation done here rather than in initialize() method
	 * as this results in less state to hold onto
	 * @param object - cannot be <code>null</code>
	 * @param ref - cannot be <code>null</code> and must represent a single reference
	 * @param parent - cannot be <code>null</code>
	 * @param toolkit - cannot be <code>null</code>
	 * @param columnwidths - can be <code>null</code>
	 */
	ReferencePart( IDomainObject<?> object,
				   EReference ref,
				   Composite parent, 
				   FormToolkit toolkit,
				   int[] columnWidths ) {
		assert object != null;
		assert ref != null;
		assert !ref.isMany();
		assert parent != null;
		assert toolkit != null;
		// column widths can be null
		
		// metadata and fields for gui creation
		boolean allowAdd = ( 
				object.getDomainClass().getAssociatorFor( ref ) != null );
		boolean allowRemove = ( 
				object.getDomainClass().getDissociatorFor( ref ) != null );
		Class<?> refPojoType = ref.getEType().getInstanceClass();
		final IRuntimeDomainClass<?> refDomainType = (IRuntimeDomainClass<?>)
			  RuntimeDomain.instance().lookupNoRegister( refPojoType );		
		
		// layout
		int numCols = 2;
		if ( allowAdd ) numCols++;
		if ( allowRemove ) numCols++;
		GridLayout parentLayout = new GridLayout( numCols, false );
		parent.setLayout( parentLayout );
		
		// label:
		GridData labelData = new GridData( GridData.HORIZONTAL_ALIGN_END );
		if ( columnWidths != null 
				&& columnWidths.length == 2 
					&& columnWidths[0] != 0 ) {
			labelData.widthHint = columnWidths[0];
		}
		Label label = toolkit.createLabel( parent, ref.getName() + ":" ); //$NON-NLS-1$
		label.setLayoutData( labelData );
		
		// main field text
		_text = toolkit.createText( parent, "" ); //$NON-NLS-1$
		_text.setEditable( false );
		_text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		
		
		// DnD - always as a drag source
		final DragSource dragSource = new DragSource( 
				_text, 
				DND.DROP_MOVE | DND.DROP_COPY );
		final DomainObjectTransfer transfer = (DomainObjectTransfer)
			DndTransferFactory.getTransfer( ref.getEType().getInstanceClass() );
		dragSource.setTransfer( new Transfer[]{ transfer } );
		dragSource.addDragListener (new DragSourceListener () {
			public void dragStart(DragSourceEvent event) {
				event.doit = ( _refValue != null );
			}
			public void dragSetData (DragSourceEvent event) {
				event.data = _refValue;
			}
			public void dragFinished(DragSourceEvent event) {
				// does nowt
			}
		});
		
		// add functionality
		if ( allowAdd ) {
			
			// a DnD drop target
			final DropTarget target = new DropTarget( 
					_text, 
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
						Job job = new AddReferenceJob( 
								_parent, 
								_ref, 
								(IDomainObject<?>)event.data ) ;
						job.schedule();
						// listener ensures refresh();
					}
					else {
						event.detail = DND.DROP_NONE;
					}
				}
			});
			
			// and display an add button
			Button add = toolkit.createButton( parent, "", SWT.PUSH ); //$NON-NLS-1$
			add.setImage( ImageUtil.getImage(
		   			GuiPlugin.getDefault(), "/icons/add.png" ) ) ; //$NON-NLS-1$
			add.setLayoutData( new GridData() );
			add.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					Job job = new SearchJob( refDomainType );
					job.schedule();
				}
			});
		}
		
		// remove functionality
		if ( allowRemove ) {
			_removeButton = toolkit.createButton( parent, "", SWT.PUSH ); //$NON-NLS-1$
			_removeButton.setImage( ImageUtil.getImage(
		   			GuiPlugin.getDefault(), "/icons/remove.png" ) ); //$NON-NLS-1$ );
			_removeButton.setLayoutData( new GridData() );
			_removeButton.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					assert _refValue != null;
					Job job = new RemoveReferenceJob( _parent, _ref, _refValue ) ;
					job.schedule();
					// listener will cause refresh
				}	
			});
		}
		else {
			_removeButton = null;
		}
		
		// add listener that updates field whenever domain object updated
		_listener = new IDomainObjectReferenceListener(){
			public void collectionAddedTo(DomainObjectReferenceEvent event) {
				// does nowt
			}
			public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
				// does nowt
			}
			public void referenceChanged(DomainObjectReferenceEvent event) {
				refresh();
			}
		};
		object.getReference( ref ).addDomainObjectReferenceListener( _listener );

		// finally set fields
	    _parent = object;
		_ref = ref;
	}
	
	
	/* IFormPart contract */

	/**
	 * Stores form reference only
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
//		_parent.getReference( _ref ).removeDomainObjectReferenceListener( _listener );
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
		_isDirty = false;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		assert input == _parent;
		refresh();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_text.setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		return _isDirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		Method accessor = _parent.getDomainClass().getAccessorFor( _ref );
		try {
			assert accessor.getParameterTypes().length == 0;
			Object pojo = accessor.invoke( _parent.getPojo(), (Object[])null );
			if ( pojo == null ) {
				_refValue = null;
				_text.setText( "" ); //$NON-NLS-1$
				if ( _removeButton != null ) {
					_removeButton.setEnabled( false );
					_removeButton.setVisible( false );
				}
			}
			else {
				assert ( pojo.getClass().equals( _ref.getEType().getInstanceClass() ) );
				final ISession session
					= RuntimePlugin.getDefault().getSessionManager().get(
						_parent.getSessionId() );
				_refValue = session.getDomainObjectFor( 
						pojo, 
						(Class<?>)_ref.getEType().getInstanceClass() );
				_text.setText(
						GuiPlugin.getDefault()
								 .getLabelProvider( _refValue )
								 .getText( _refValue ) );
				if ( _removeButton != null ) {
					_removeButton.setEnabled( true );
					_removeButton.setVisible( true );
				}
			}
		}
		catch ( Exception ex ) {
			IStatus status;
			if ( ex instanceof CoreException ) {
				status = ((CoreException)ex).getStatus();
				GuiPlugin.getDefault().getLog().log( status );
			}
			else {
				status = StatusUtil.createError(
						GuiPlugin.getDefault(),
						"ReferencePart.AccessError", //$NON-NLS-1$
						ex ) ;
				// automatically logged by util class
			}
			_refValue = null;
			_text.setText( status.getMessage() );
			_text.setForeground( 
					_text.getDisplay().getSystemColor( 
							SWT.COLOR_DARK_RED ) );
			if ( _removeButton != null ) {
				_removeButton.setEnabled( false );
				_removeButton.setVisible( false );
			}
		}

	}

}
