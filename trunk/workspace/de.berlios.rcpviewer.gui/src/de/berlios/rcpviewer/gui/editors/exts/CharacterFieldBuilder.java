/**
 * 
 */
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.berlios.rcpviewer.gui.IFieldBuilder;
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.gui.util.EmfUtil;

/**
 * Used for <code>char</code> and <code>Character</code>.
 * @author Mike
 */
public class CharacterFieldBuilder implements IFieldBuilder {

	/**
	 * Only if class is a <code>Character</code> or a <code>char</code>
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		Class attributeClass = element.getEType().getInstanceClass();
		if ( char.class == attributeClass ) return true;
		if ( Character.class == attributeClass ) return true;
		return false;
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
		return new CharacterField( parent, element, listener );
	}
	
	/* private classes */
	
	private class CharacterField implements IField {
		
		private final Text _text;
		
		CharacterField( Composite parent, 
				        ETypedElement element,
				        final IFieldListener listener ) {
			assert parent != null;
			assert element != null;
			assert listener != null;
			
			// read-only gui
			parent.setLayout( new GridLayout() );
			_text = new Text( parent, SWT.WRAP );
			_text.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			_text.setEditable( false );
			
			// read only DnD
			final Transfer transferType
				= DndTransferFactory.getTransfer( char.class );
			assert transferType != null;
			int operations = DND.DROP_MOVE | DND.DROP_COPY ;
			
			final DragSource source = new DragSource ( _text, operations);
			source.setTransfer( new Transfer[]{ transferType } );
			source.addDragListener (new DragSourceListener () {
				public void dragStart(DragSourceEvent event) {
					event.doit = ( getGuiValue() != null );
				}
				public void dragSetData (DragSourceEvent event) {
					event.data = getGuiValue();
				}
				public void dragFinished(DragSourceEvent event) {
					// does nowt
				}
			});
			
			// if read-write:
			if ( EmfUtil.isModifiable( element ) ) {
				
				// write functionality
				_text.setEditable( true );
				_text.addVerifyListener( new VerifyListener(){
					public void verifyText(VerifyEvent event){
						// build resultant text
						String resultantText;
						String orig = _text.getText();
						if ( orig.length() == 0 ) {
							resultantText = event.text;
						}
						else {
							StringBuffer sb = new StringBuffer();
							sb.append( orig.substring( 0, event.start ) );
							sb.append( event.text );
							sb.append( orig.substring( event.end, orig.length() ) );
							resultantText = sb.toString();
						}
						event.doit = ( resultantText.length() < 2 );
					}
				} );
				_text.addModifyListener( new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						listener.fieldModified( CharacterField.this );
					};
				});
				
				// write DnD
				DropTarget target = new DropTarget( _text, operations);
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
						setGuiValue( event.data );
					}
				});
			}
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#getGuiValue()
		 */
		public Object getGuiValue() {
			String s = _text.getText();
			switch( s.length() ) {
				case 0:
					return null;
				case 1:
					return s.charAt(0);
			    default:
			    	assert false;
			    	return null;
			}
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
				_text.setText( "" ); //$NON-NLS-1$
			}
			else {
				if ( !(obj instanceof Character) ) {
					throw new IllegalArgumentException();
				}
				_text.setText( String.valueOf( obj ) );
			}
			
		}
	}
}
