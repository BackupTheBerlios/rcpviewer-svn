package de.berlios.rcpviewer.gui.editors.exts;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.berlios.rcpviewer.gui.IFieldBuilder;
import de.berlios.rcpviewer.gui.dnd.BooleanTransfer;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;

/**
 * Used for booleans.
 * <br>Can handle DnD operations but only within the app.
 * @author Mike
 */
public class BooleanFieldBuilder implements IFieldBuilder {

	/**
	 * Only if class is a <code>Boolean</code>
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return Boolean.class == element.getEType().getInstanceClass();
	}

	/**
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, ETypeElement, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(
			Composite parent, 
			ETypedElement element, 
			IFieldListener listener) {
		if( parent == null ) throw new IllegalArgumentException();
		if( element == null ) throw new IllegalArgumentException();
		if( listener == null ) throw new IllegalArgumentException();
		return new BooleanField( parent, element, listener );
	}
	
	private class BooleanField implements IField {
		
		private final Button _button;
		
		BooleanField( Composite parent, 
					  ETypedElement element, 
				      final IFieldListener listener ) {
			parent.setLayout( new GridLayout() );

			parent.setLayout( new GridLayout() );
			_button= new Button( parent, SWT.CHECK );
			_button.setLayoutData( new GridData() );
			_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent pE) {
					listener.fieldModified( BooleanField.this );
				}
			});
			
			
			if ( EmfUtil.isModifiable( element ) ) {
				_button.addSelectionListener( new DefaultSelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent pE) {
						listener.fieldModified( BooleanField.this );
					}
				});
				addDnD( _button, true );
			}
			else {
				_button.setEnabled( false );
				addDnD( _button, false );
			}
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#getGuiValue()
		 */
		public Object getGuiValue() {
			return _button.getSelection();
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setFocus()
		 */
		public void setFocus() {
			_button.setFocus();
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setGuiValue(java.lang.Object)
		 */
		public void setGuiValue(Object obj) {
			if ( !(obj instanceof Boolean) ) throw new IllegalArgumentException();
			_button.setSelection( (Boolean)obj );
			
		}
		
		// add DnD functionality
		private void addDnD( final Button button, boolean editable ) {
			assert button != null;
			
			Transfer[] types = new Transfer[] {
					BooleanTransfer.getInstance() };
			int operations = DND.DROP_MOVE | DND.DROP_COPY ;
			
			final DragSource source = new DragSource (button, operations);
			source.setTransfer(types);
			source.addDragListener (new DragSourceListener () {
				public void dragStart(DragSourceEvent event) {
					event.doit = true;
				}
				public void dragSetData (DragSourceEvent event) {
					event.data = button.getSelection();
				}
				public void dragFinished(DragSourceEvent event) {
					// does nowt
				}
			});

			if ( editable ) {
				DropTarget target = new DropTarget(button, operations);
				target.setTransfer(types);
				target.addDropListener (new DropTargetAdapter() {
					public void dragEnter(DropTargetEvent event){
						if ( !BooleanTransfer.getInstance().isSupportedType(
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
						button.setSelection( (Boolean)event.data );
					}
				});
			}
		}
	}
}
