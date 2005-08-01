package de.berlios.rcpviewer.gui.fieldbuilders.exts;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DomainObjectTransfer;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.jobs.SearchJob;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Default field builder for displaying domain objects.
 * @author Mike
 *
 */
public class DomainObjectFieldBuilder implements IFieldBuilder {
	

	/**
	 * Only if the element is for a type recognised as a domain class.
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return ( RuntimeDomain.instance().lookupNoRegister( 
					(Class<?>)element.getEType().getInstanceClass() ) != null ); 
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, ETypedElement element, IFieldListener listener) {
		return new DomainObjectField( parent, element, listener );
	}

	private class DomainObjectField implements IField {
		
		private final Text _text;
		
		private IDomainObject _obj = null;
		
		DomainObjectField( 
				final Composite parent, 
				ETypedElement element,
				final IFieldListener listener ) {
			
			 final IDomainClass domainClass
			 	= RuntimeDomain.instance().lookupNoRegister( 
			 			(Class<?>)element.getEType().getInstanceClass() ); 
			if ( domainClass == null ) throw new IllegalArgumentException();
			
			GridLayout layout = new GridLayout();
			parent.setLayout( layout );
			
			// text box
			_text = new Text( parent, SWT.SINGLE );
			_text.setBackground( parent.getBackground() );
			_text.setEditable( false );
			_text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL) );
			_text.setEditable( false );
			
			// DnD drag source
			final DomainObjectTransfer transferType
					= DomainObjectTransfer.getInstance( domainClass );
			Transfer[] types = new Transfer[] { transferType };
			int operations = DND.DROP_MOVE | DND.DROP_COPY ;
			final DragSource source = new DragSource ( _text, operations);
			source.setTransfer(types);
			source.addDragListener (new DragSourceListener () {
				public void dragStart(DragSourceEvent event) {
					event.doit = ( _obj != null );
				}
				public void dragSetData (DragSourceEvent event) {
					event.data = _obj;
				}
				public void dragFinished(DragSourceEvent event) {
					// does nowt
				}
			});
			
			
			if ( EmfUtil.isModifiable( element ) ) {
				layout.makeColumnsEqualWidth = false;
				layout.numColumns = 2;
				
				// can change object via search op ...
		        Button change = new Button( parent, SWT.PUSH | SWT.FLAT );
				change.setText( GuiPlugin.getResourceString( "SearchJob.Name" ) ); //$NON-NLS-1$
				GridData buttonData = new GridData();
				buttonData.heightHint = _text.getLineHeight() ;
				change.setLayoutData( buttonData );
				change.addListener( SWT.Selection, new Listener() {
		            public void handleEvent(Event event) {
		            	new SearchJob( (IRuntimeDomainClass)domainClass ).schedule();
		            }
		        });
				
				// or DnD...
				DropTarget target = new DropTarget( _text, operations);
				target.setTransfer(types);
				target.addDropListener (new DropTargetAdapter() {
					public void dragEnter(DropTargetEvent event){
						if ( !transferType.isSupportedType(
								event.currentDataType ) ) {
							event.detail = DND.DROP_NONE;
						}
					}
					public void drop(DropTargetEvent event) {
						if ( event.data == null) {
							event.detail = DND.DROP_NONE;
							return;
						}
						// note that all DnD ops converted to copy's 
						event.detail = DND.DROP_COPY;
						setGuiValue( event.data );
					}
				});
				
				// either way text display changed
				_text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						listener.fieldModified( DomainObjectField.this );
					};
				});
				
			}

		}

		/**
		 * Be warned that this returna a <code>IDomainObject</code> that will
		 * have to interogated for its pojo.
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#getGuiValue()
		 */
		public Object getGuiValue() {
			return _obj;
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setFocus()
		 */
		public void setFocus() {
			_text.setFocus();
		}

		/**
		 * This expects an instance of <code>IDomainObject</code> wrapping the
		 * domain pojo.
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setGuiValue(java.lang.Object)
		 */
		public void setGuiValue(Object obj) {
			if( obj == null ) {
				_obj = null;
				_text.setText( "" ); //$NON-NLS-1$
			}
			else {
				if ( !(obj instanceof IDomainObject) ) { // JAVA_5_FIXME
					throw new IllegalArgumentException();
				}
				_obj = (IDomainObject)obj;
				_text.setText( GuiPlugin.getDefault()
						 			    .getLabelProvider( _obj )
						 			    .getText( _obj ) ) ;
			}
		}
	}
}
