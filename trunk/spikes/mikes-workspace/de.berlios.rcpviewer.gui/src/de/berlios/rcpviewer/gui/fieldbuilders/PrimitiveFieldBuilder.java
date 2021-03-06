package de.berlios.rcpviewer.gui.fieldbuilders;

import static de.berlios.rcpviewer.gui.util.FontUtil.CharWidthType.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.gui.editors.parts.IGuiPart;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.gui.util.FontUtil;
import de.berlios.rcpviewer.gui.util.ImageUtil;


/**
 * Abstract parent for all primitive type field builders except 
 * boolean and character
 * @author Mike
 */
class PrimitiveFieldBuilder implements IFieldBuilder {
	
	private static final Map<Class,Class> WRAPPERS = new HashMap<Class,Class>();
	
	static {
		// nb : does not do boolean
		WRAPPERS.put( byte.class, Byte.class );
		WRAPPERS.put( char.class, Character.class );
		WRAPPERS.put( short.class, Short.class );
		WRAPPERS.put( int.class, Integer.class );
		WRAPPERS.put( long.class, Long.class );
		WRAPPERS.put( float.class, Float.class );
		WRAPPERS.put( double.class, Double.class );
	}

	private final Class _type;
	
	/**
	 * Constructor passed type.
	 * @param type
	 */
	PrimitiveFieldBuilder( Class type ) {
		if( type == null ) throw new IllegalArgumentException();
		if( !type.isPrimitive() ) throw new IllegalArgumentException();
		_type = type;
	}


	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return isApplicableType( element.getEType().getInstanceClass() );
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, ETypedElement element, IFieldListener listener, int[] columnWidths) {
		if( parent == null ) throw new IllegalArgumentException();
		if( element == null ) throw new IllegalArgumentException();
		// listener can be null
		if( columnWidths == null ) throw new IllegalArgumentException();
		return new PrimitiveField( _type, parent, element, listener, columnWidths );
	}
	
	// whether an applicable type
	private boolean isApplicableType( Class type ) {
		if ( _type == type ) return true;
		if ( WRAPPERS.get( _type ) == type ) return true;
		return false;
	}
	
	/* private classes */
	
	private class PrimitiveField implements IField {
		
		private final Text _text;
		private final Method _valueOfMethod;
		
		PrimitiveField( Class type,
				        Composite parent, 
				        ETypedElement element,
				        final IFieldListener listener,
				        int[] columnWidths ) {
			assert type != null;
			assert parent != null;
			assert element != null;
			
			// store reference to valueOf method - char does not have one 
			if ( _type == char.class ) {
				_valueOfMethod = null;
			}
			else {
				try {
					_valueOfMethod = WRAPPERS.get( _type ).getMethod( 
							"valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
				}
				catch ( NoSuchMethodException nsfe ) {
					throw new IllegalArgumentException();
				}
			}
			
			// read-only gui
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
			
			// decide width & style of text box, and icon values
			GridData textData;
			String iconPath;
			String iconToolTip;
			int style = Integer.MIN_VALUE;
			if ( _type == byte.class ) {
				textData = new GridData();
				textData.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 5;
				iconPath = "icons/byte.png"; //$NON-NLS-1$
				iconToolTip = GuiPlugin.getResourceString(
						"PrimitiveFieldBuilder.ByteToolTip" ) ; //$NON-NLS-1$
				style = SWT.CENTER;
			}
			else if ( _type == char.class ) {
				textData = new GridData();
				textData.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 3;
				iconPath="icons/char.png"; //$NON-NLS-1$
				iconToolTip = GuiPlugin.getResourceString(
					"PrimitiveFieldBuilder.CharToolTip" ) ; //$NON-NLS-1$
				style = SWT.CENTER;
			}
			else if ( _type == short.class ) {
				textData = new GridData();
				textData.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 4;
				iconPath="icons/short.png"; //$NON-NLS-1$
				iconToolTip = GuiPlugin.getResourceString(
					"PrimitiveFieldBuilder.ShortToolTip" ) ; //$NON-NLS-1$
				style = SWT.LEFT;
			}
			else if ( _type == int.class ) {
				textData = new GridData();
				textData.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 10;
				iconPath="icons/int.png"; //$NON-NLS-1$
				iconToolTip = GuiPlugin.getResourceString(
					"PrimitiveFieldBuilder.IntToolTip" ) ; //$NON-NLS-1$
				style = SWT.LEFT;
			}
			else if ( _type == long.class ) {
				textData = new GridData();
				textData.widthHint = FontUtil.getCharWidth( parent, SAFE ) * 19;
				iconPath="icons/long.png"; //$NON-NLS-1$
				iconToolTip = GuiPlugin.getResourceString(
					"PrimitiveFieldBuilder.LongToolTip" ) ; //$NON-NLS-1$
				style = SWT.LEFT;
			}
			else {
				textData = new GridData( GridData.FILL_HORIZONTAL );
				if ( columnWidths.length == 3 
							&& columnWidths[2] != 0 ) {
					textData.widthHint = columnWidths[2];
				}
				if( _type == float.class ) {
					iconPath="icons/float.png"; //$NON-NLS-1$
					iconToolTip = GuiPlugin.getResourceString(
						"PrimitiveFieldBuilder.FloatToolTip" ) ; //$NON-NLS-1$
				}
				else {
					iconPath="icons/double.png"; //$NON-NLS-1$
					iconToolTip = GuiPlugin.getResourceString(
						"PrimitiveFieldBuilder.DoubleToolTip" ) ; //$NON-NLS-1$
				}
				style = SWT.LEFT;
			}
			assert style != Integer.MIN_VALUE;

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
					GuiPlugin.getDefault(), iconPath ), //$NON-NLS-1$
					IGuiPart.PART_ICON_SIZE ) );
			icon.setLayoutData( iconData );
			icon.setToolTipText( iconToolTip );
			
			// text
			_text = new Text( parent, style );
			_text.setLayoutData( textData );
			_text.setEditable( false );
			
			// read only DnD
			final Transfer transferType = DndTransferFactory.getTransfer( type );
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
						String sVal = SWTUtils.buildResultantText( _text, event );
						
						// blank string equates to null so OK
						if ( sVal.length() == 0 ) return;
						
						// char a special case
						if ( _type == char.class ) {
							event.doit = ( sVal.length() < 2 );
							return;
						}
						
						// check this text value could be converted to type
						try {
							_valueOfMethod.invoke( null, new Object[]{sVal} );
						}
						catch ( Exception ex ) {
							event.doit = false;
						}
					}
				} );
				
				if ( listener != null ) {
					_text.addModifyListener( new ModifyListener() {
						public void modifyText(ModifyEvent e) {
							listener.fieldModified( PrimitiveField.this );
						};
					});
				}
				
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
			if ( s.length() == 0 ) return null;
			
			// char a special case
			if ( _type == char.class ) {
				assert s.length() == 1;
				return s.charAt(0);
			}
			
			try {
				return _valueOfMethod.invoke( null, new Object[]{s} );
			}
			catch ( Exception ex ) {
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
				if ( !isApplicableType( obj.getClass() ) ) {
					throw new IllegalArgumentException();
				}
				_text.setText( GuiPlugin.getDefault()
										.getLabelProvider()
										.getText( obj ) );
			}
			
		}
	}
}
