package de.berlios.rcpviewer.gui.fieldbuilders.exts;

import java.math.BigDecimal;

import net.sf.plugins.utils.SWTUtils;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.exts.BigDecimalTransfer;
import de.berlios.rcpviewer.gui.editors.parts.IGuiPart;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.gui.util.FontUtil;
import de.berlios.rcpviewer.gui.util.ImageUtil;

/**
 * Used for dates.
 * <br>Can handle DnD operations but only within the app.
 * <br>Uses third party library swtcalendar - with thanks.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class BigDecimalFieldBuilder implements IFieldBuilder {

	/**
	 * Only if the class is a <code>BigDecimal</code> or subclass.
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return BigDecimal.class.isAssignableFrom(
				element.getEType().getInstanceClass() );
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
		if( columnWidths == null ) throw new IllegalArgumentException();
		return new BigDecimalField( parent, element, listener, columnWidths );
	}

	private class BigDecimalField implements IField {
		
		private final Text _text;
		
		BigDecimalField( final Composite parent, 
				   ETypedElement element,
				   final IFieldListener listener,
				   int[] columnWidths ) {
			
			GridLayout layout = new GridLayout( 3, false );
			parent.setLayout( layout );
			
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
					GuiPlugin.getDefault(), "icons/bigdecimal.png" ), //$NON-NLS-1$
					IGuiPart.PART_ICON_SIZE ) );
			icon.setLayoutData( iconData );
			icon.setToolTipText( 
					GuiPlugin.getResourceString( 
							"BigDecimalFieldBuilder.IconToolTip" ) ); //$NON-NLS-1$
			
			// text box
			GridData textData = new GridData();
			if ( columnWidths.length == 3 
						&& columnWidths[2] != 0 ) {
				textData.widthHint = columnWidths[2];
			}
			_text = new Text( parent, SWT.SINGLE | SWT.LEFT );
			_text.setBackground( parent.getBackground() );
			_text.setLayoutData( textData );
			
			if ( !EmfUtil.isModifiable( element ) ) {
				_text.setEditable( false );
				addDnD( _text, false );
			}
			else {	
				// verify input
				_text.addVerifyListener( new VerifyListener(){
					public void verifyText(VerifyEvent event){
						// build resultant text
						String sVal = SWTUtils.buildResultantText( _text, event );
						
						// blank string equates to null so OK
						if ( sVal.length() == 0 ) return;
						
						// check this text value could be converted to type
						try {
							new BigDecimal( sVal );
						}
						catch ( NumberFormatException nfe ) {
							event.doit = false;
						}
					}
				} );
				
				// external listener?
				if ( listener != null ) {
					_text.addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent e) {
							listener.fieldModified( BigDecimalField.this );
						};
					});
				}
				addDnD( _text, true );
			}
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#getGuiValue()
		 */
		public Object getGuiValue() {
			try {
				String s = _text.getText().trim();
				if ( s.length() == 0 ) return null;
				BigDecimal bd = new BigDecimal( s );
				return bd;
			}
			catch ( NumberFormatException nfe ) {
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
			if( obj == null ) {
				_text.setText( "" ); //$NON-NLS-1$
			}
			else {
				if ( !(obj instanceof BigDecimal ) ) {
					throw new IllegalArgumentException();
				}
				_text.setText( obj.toString() );
			}
		}
		
		// add DnD functionality
		private void addDnD( final Text text, boolean editable ) {
			assert text != null;
			
			Transfer[] types = new Transfer[] {
					BigDecimalTransfer.getInstance() };
			int operations = DND.DROP_MOVE | DND.DROP_COPY ;
			
			final DragSource source = new DragSource (text, operations);
			source.setTransfer(types);
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

			if ( editable ) {
				DropTarget target = new DropTarget(text, operations);
				target.setTransfer(types);
				target.addDropListener (new DropTargetAdapter() {
					public void dragEnter(DropTargetEvent event){
						if ( !BigDecimalTransfer.getInstance().isSupportedType(
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
						setGuiValue( (BigDecimal)event.data );
					}
				});
			}
		}
	}
}
