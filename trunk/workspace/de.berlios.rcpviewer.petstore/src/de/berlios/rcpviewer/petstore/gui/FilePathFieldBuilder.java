package de.berlios.rcpviewer.petstore.gui;

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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.berlios.rcpviewer.gui.editors.parts.IGuiPart;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.gui.util.FontUtil;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;
import de.berlios.rcpviewer.petstore.PetstorePlugin;
import de.berlios.rcpviewer.petstore.domain.FilePath;

/**
 * Used for <code>FilePath</code>'s.
 * <br>Can handle DnD operations but only within the app.
 * <br>Currently ignores filter concepts of <code>FilePath</code>.
 * @ref http://swtcalendar.sourceforge.net/
 * @author Mike
 *
 */
public class FilePathFieldBuilder implements IFieldBuilder {

	/**
	 * Only if the class is a <code>FilePath</code>.
	 * @see de.berlios.rcpviewer.gui.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return FilePath.class.equals( element.getEType().getInstanceClass() );
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
		return new FilePathField( parent, element, listener, columnWidths );
	}

	private class FilePathField implements IField {
		
		private final Text _text;
		
		FilePathField( 
				final Composite parent, 
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
				    PlatformUI.getWorkbench().getSharedImages().getImage(
				    		ISharedImages.IMG_OBJ_FILE ),
					IGuiPart.PART_ICON_SIZE ) );
			icon.setLayoutData( iconData );
			icon.setToolTipText( 
					PetstorePlugin.getResourceString( 
							"FilePathFieldBuilder.IconToolTip" ) ); //$NON-NLS-1$
			
			// text box
			GridData textData = new GridData(  GridData.FILL_HORIZONTAL );
			if ( columnWidths.length == 3 
						&& columnWidths[2] != 0 ) {
				textData.widthHint = columnWidths[2];
			}
			_text = new Text( parent, SWT.SINGLE | SWT.LEFT );
			_text.setBackground( parent.getBackground() );
			_text.setEditable( false );
			_text.setLayoutData( textData );
			
			if ( !EmfUtil.isModifiable( element ) ) {
				_text.setEditable( false );
				addDnD( _text, false );
			}
			else {
				layout.numColumns = 4;
			
				if ( listener != null ) {
					_text.addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent e) {
							listener.fieldModified( FilePathField.this );
						};
					});
				}
				
				// change date via calendar widget
		        Button change = new Button( parent, SWT.PUSH | SWT.FLAT );
				change.setText( "..."); //$NON-NLS-1$
				GridData buttonData = new GridData();
				buttonData.heightHint = _text.getLineHeight() ;
				change.setLayoutData( buttonData );
				change.addSelectionListener( new DefaultSelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						FileDialog dialog = new FileDialog( 
								parent.getShell(),
								SWT.OPEN );
						dialog.setFileName( _text.getText() );
						String path = dialog.open();
						if ( path != null ) {
							_text.setText( path );
						}
					}
					
				} );
				addDnD( _text, true );
			}
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#getGuiValue()
		 */
		public Object getGuiValue() {
			String s = _text.getText();
			if ( s.length() == 0 ) return null;
			FilePath fPath = new FilePath();
			fPath.init( (String[])null );
			assert fPath.isValidString( s );
			fPath.parseString( s );
			return fPath;
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
				if ( !(obj instanceof FilePath) ) {
					throw new IllegalArgumentException();
				}
				_text.setText( ((FilePath)obj).getPath() );
			}
		}
		
		// add DnD functionality
		private void addDnD( final Text text, boolean editable ) {
			assert text != null;
			
			Transfer[] types = new Transfer[] {
					FilePathTransfer.getInstance() };
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
						if ( !FilePathTransfer.getInstance().isSupportedType(
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
						setGuiValue( (FilePath)event.data );
					}
				});
			}
		}
	}
}
