package de.berlios.rcpviewer.gui;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.berlios.rcpviewer.gui.util.EmfUtil;


/**
 * Used when no other available <code>IFieldBuilder</code> is applicable.
 * <br>Can handle DnD operations but only as text.
 * @author Mike

 */
class DefaultFieldBuilder implements IFieldBuilder {


	/**
	 * Always true.
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, ETypedElement element, IFieldListener listener) {
		if( parent == null ) throw new IllegalArgumentException();
		if( element == null ) throw new IllegalArgumentException();
		if( listener == null ) throw new IllegalArgumentException();
		return new DefaultField( parent, element, listener );
	}
	
	private class DefaultField implements IField {
		
		private final Text _text;
		
		private Object _obj = null;
		
		DefaultField( Composite parent, 
				      ETypedElement element,
				      final IFieldListener listener ) {
			parent.setLayout( new GridLayout() );
			_text = new Text( parent, SWT.WRAP );
			_text.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			if ( EmfUtil.isModifiable( element ) ) {
				_text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						listener.fieldModified( DefaultField.this );
					};
				});
				addDnD( _text, true );
			}
			else {
				_text.setEditable( false );
				addDnD( _text, false );
			}
			
		}

		/* (non-Javadoc)
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

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setGuiValue(java.lang.Object)
		 */
		public void setGuiValue(Object obj) {
			_obj = obj;
			if ( _obj == null ) {
				_text.setText( "" ); //$NON-NLS-1$
			}
			else {
				_text.setText( String.valueOf( obj ) );
			}
			
		}
		
		// add DnD functionality
		private void addDnD( final Text text, boolean editable ) {
			assert text != null;
			
			Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
			int operations = DND.DROP_MOVE | DND.DROP_COPY ;
			
			final DragSource source = new DragSource (text, operations);
			source.setTransfer(types);
			source.addDragListener (new DragSourceListener () {
				public void dragStart(DragSourceEvent event) {
					event.doit = (text.getText ().length () != 0);
				}
				public void dragSetData (DragSourceEvent event) {
					event.data = text.getText ();
				}
				public void dragFinished(DragSourceEvent event) {
					// does nowt
				}
			});

			if ( editable ) {
				DropTarget target = new DropTarget(text, operations);
				target.setTransfer(types);
				target.addDropListener (new DropTargetAdapter() {
					public void drop(DropTargetEvent event) {
						if (event.data == null) {
							event.detail = DND.DROP_NONE;
							return;
						}
						// note that all DnD ops converted to copy's 
						event.detail = DND.DROP_COPY;
						text.setText ((String) event.data );
					}
				});
			}
		}
		
	}










	
}
