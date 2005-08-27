package de.berlios.rcpviewer.gui.fieldbuilders;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.util.FontUtil;


/**
 * Used when no other available <code>IFieldBuilder</code> is applicable.
 * <br>Simply displays an error message.
 * @author Mike

 */
class DefaultFieldBuilder implements IFieldBuilder {


	/**
	 * Always true.
	 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.ETypedElement)
	 */
	public boolean isApplicable(ETypedElement element) {
		return true;
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
		
		private Object _obj = null;
		
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
			
			// error message
			GridData msgData = new GridData( GridData.FILL_HORIZONTAL );
			if ( columnWidths != null 
					&& columnWidths.length == 2 
						&& columnWidths[1] != 0 ) {
				msgData.widthHint = columnWidths[1];
			}
			Label msg = new Label( parent, SWT.LEFT );
			msg.setLayoutData( msgData );
			msg.setForeground( 
					parent.getDisplay().getSystemColor( SWT.COLOR_DARK_RED ) );
			msg.setBackground( parent.getBackground() );
			msg.setText( 
					GuiPlugin.getResourceString( "DefaultFieldBuilder.Msg" ) ); //$NON-NLS-1$
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
			// does nowt
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder.IField#setGuiValue(java.lang.Object)
		 */
		public void setGuiValue(Object obj) {
			_obj = obj;
		}		
	}
}
