package de.berlios.rcpviewer.gui.fieldbuilders;

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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.gui.editors.parts.IGuiPart;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.gui.util.FontUtil;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;

/**
 * Used for booleans.
 * @author Mike
 */
class BooleanFieldBuilder implements IFieldBuilder {

	/**
	 * Only if class is a <code>Boolean</code> or a <code>boolean</code>
	 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		Class attributeClass = element.getEType().getInstanceClass();
		if ( boolean.class == attributeClass ) return true;
		if ( Boolean.class == attributeClass ) return true;
		return false;
	}

	/**
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, ETypeElement, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener, int[])
	 */
	public IField createField(
			Composite parent, 
			ETypedElement element, 
			IFieldListener listener, 
			int[] columnWidths) {
		if( parent == null ) throw new IllegalArgumentException();
		if( element == null ) throw new IllegalArgumentException();
		// listener can be null
		if( columnWidths == null ) throw new IllegalArgumentException();
		return new BooleanField( parent, element, listener, columnWidths );
	}
	
	private class BooleanField implements IField {
		
		private final Button _button;
		
		BooleanField( Composite parent, 
					  ETypedElement element, 
				      final IFieldListener listener,
				      int[] columnWidths ) {
			parent.setLayout( new GridLayout( 3, false ) );
			
			// label
			GridData labelData = new GridData();
			if ( columnWidths.length == 3 
						&& columnWidths[0] != 0 ) {
				labelData.widthHint = columnWidths[0];
			}
			Label label = new Label( parent, SWT.RIGHT );
			label.setLayoutData( labelData );
			label.setBackground( parent.getBackground() );
			label.setText( element.getName() + ":" ); //$NON-NLS-1$
			label.setFont( FontUtil.getLabelFont() );
			
			// icon
			GridData iconData = new GridData();
			if ( columnWidths.length == 3 
						&& columnWidths[1] != 0 ) {
				iconData.widthHint = columnWidths[1];
			}
			else {
				iconData.widthHint = IGuiPart.PART_ICON_SIZE.x;
			}
			Label icon = new Label( parent, SWT.NONE );
			icon.setImage( 
				ImageUtil.resize(
					ImageUtil.getImage(
					GuiPlugin.getDefault(), "icons/boolean.png" ), //$NON-NLS-1$
					IGuiPart.PART_ICON_SIZE ) );
			icon.setLayoutData( iconData );
			icon.setToolTipText( 
					GuiPlugin.getResourceString( 
							"BooleanFieldBuilder.IconToolTip" ) ); //$NON-NLS-1$

			// check-box
			GridData buttonData = new GridData();
			if ( columnWidths.length == 3 
						&& columnWidths[2] != 0 ) {
				buttonData.widthHint = columnWidths[2];
			}
			_button= new Button( parent, SWT.CHECK );
			_button.setLayoutData( buttonData);
			
			if ( EmfUtil.isModifiable( element ) ) {
				if ( listener != null ) {
					_button.addSelectionListener( new DefaultSelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							listener.fieldModified( BooleanField.this );
						}
					});
				}
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
			if ( obj == null ) {
				_button.setSelection( false );
			}
			else {
				if ( !(obj instanceof Boolean) ) {
					throw new IllegalArgumentException();
				}
				_button.setSelection( (Boolean)obj );
			}
		}
		
		// add DnD functionality
		private void addDnD( final Button button, boolean editable ) {
			assert button != null;
			
			final Transfer transferType
				= DndTransferFactory.getTransfer( boolean.class );
			assert transferType != null;
			int operations = DND.DROP_MOVE | DND.DROP_COPY ;
			
			final DragSource source = new DragSource (button, operations);
			source.setTransfer( new Transfer[]{ transferType } );
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
				target.setTransfer( new Transfer[]{ transferType } );
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
						button.setSelection( (Boolean)event.data );
					}
				});
			}
		}
	}
}
