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
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.gui.util.FontUtil;


/**
 * Used for <code>String</code>'s.
 * <br>Can handle DnD operations but only as text.
 * <br>Later must extend to better handle <code>null</code>s.
 * @author Mike

 */
public class StringFieldBuilder implements IFieldBuilder {


	/**
	 * Where element is a <code>String</code>
	 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return String.class.equals( element.getEType().getInstanceClass() );
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(
			Composite parent, 
			ETypedElement element, 
			IFieldListener listener, 
			int[] columnWidths) {
		if( parent == null ) throw new IllegalArgumentException();
		if( element == null ) throw new IllegalArgumentException();
		// listener can be null
		// column widths can be null
		return new DefaultField( parent, element, listener, columnWidths );
	}
	
	private class DefaultField implements IField {
		
		private boolean isNull = false;
		private final Text _text;
		
		DefaultField( Composite parent, 
				      ETypedElement element,
				      final IFieldListener listener,
				      int[] columnWidths ) {
			parent.setLayout( new GridLayout( 2, false ) );
			
			// label
			GridData labelData = new GridData();
			if ( columnWidths != null 
					&& columnWidths.length == 2 
						&& columnWidths[0] != 0 ) {
				labelData.widthHint = columnWidths[0];
			}
			Label label = new Label( parent, SWT.RIGHT );
			label.setLayoutData( labelData );
			label.setBackground( parent.getBackground() );
			label.setText( element.getName() + ":" ); //$NON-NLS-1$
			label.setFont( FontUtil.getLabelFont() );
			
			// text box
			GridData textData = new GridData( GridData.FILL_BOTH );
			if ( columnWidths != null 
					&& columnWidths.length == 2 
						&& columnWidths[1] != 0 ) {
				textData.widthHint = columnWidths[1];
			}
			_text = new Text( parent, SWT.WRAP );
			_text.setLayoutData( textData );
			if ( EmfUtil.isModifiable( element ) ) {
				if ( listener != null ) {
					_text.addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent e) {
							listener.fieldModified( DefaultField.this );
						};
					});
				}
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
			if ( isNull ) return null;
			return _text.getText();
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
			if ( obj == null ) {
				isNull = true;
				_text.setText( "" ); //$NON-NLS-1$
			}
			else {
				if ( !(obj instanceof String) ) throw new IllegalArgumentException();
				isNull = false;
				_text.setText( (String)obj );
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
