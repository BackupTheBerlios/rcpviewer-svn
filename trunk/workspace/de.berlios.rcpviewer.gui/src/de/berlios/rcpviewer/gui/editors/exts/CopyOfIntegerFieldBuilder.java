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
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.berlios.rcpviewer.gui.fields.IFieldBuilder;


/**
 * Used for integers.
 * <br>Can handle DnD operations.
 * @author Mike
 */
public class CopyOfIntegerFieldBuilder implements IFieldBuilder {


	/**
	 * Only if the class is an <code>Integer</code>
	 * @see de.berlios.rcpviewer.gui.fields.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		Class attributeClass = element.getEType().getInstanceClass();
		if ( int.class == attributeClass ) return true;
		if ( Integer.class == attributeClass ) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, boolean editable, IFieldListener listener) {
		return new IntegerField( parent, editable, listener );
	}
	
	private class IntegerField implements IField {
		
		private final Text _text;
		
		IntegerField( Composite parent, 
				      boolean editable,
				      final IFieldListener listener ) {
			parent.setLayout( new GridLayout() );
			_text = new Text( parent, SWT.WRAP );
			_text.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			if ( editable ) {
				_text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						listener.fieldModified( IntegerField.this );
					};
				});
				_text.addKeyListener( new KeyAdapter(){
		            public void keyPressed(KeyEvent e) {
		            	switch( e.character ) {
		                case SWT.DEL:
		                	e.doit = true;
		                	break;
		                case SWT.BS:
		                	e.doit = true;
		                	break;
		                default:
		                	e.doit = Character.isDigit( e.character );
		            	}
		            }
		        } );
			}
			else {
				_text.setEditable( false );
			}
			addDnD( _text, editable );
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#getGuiValue()
		 */
		public Object getGuiValue() {
			return Integer.valueOf( _text.getText() ) ;
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
			_text.setText( String.valueOf( obj ) );
			
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
						if ( event.data == null) {
							event.detail = DND.DROP_NONE;
						}
						else {
							try {
								Integer.valueOf( (String)event.data );
								// note that all DnD ops converted to copy's 
								event.detail = DND.DROP_COPY;
								text.setText ((String) event.data );
							}
							catch ( NumberFormatException nfe ) {
								event.detail = DND.DROP_NONE;
							}
						}
					}
				});
			}
		}
		
	}










	
}
