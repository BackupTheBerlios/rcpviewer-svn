package de.berlios.rcpviewer.gui.editors.exts;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
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
 * @author Mike
 */
public class IntegerFieldBuilder implements IFieldBuilder {

	/**
	 * Only if the class is an <code>Integer</code>
	 * @see de.berlios.rcpviewer.gui.fields.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isApplicable(EAttribute attribute) {
		Class attributeClass = attribute.getEType().getInstanceClass();
		if ( int.class == attributeClass ) return true;
		if ( Integer.class == attributeClass ) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, boolean editable, IFieldListener listener) {
		return new DefaultField( parent, editable, listener );
	}
	
	private class DefaultField implements IField {
		
		private final Text _text;
		
		DefaultField( Composite parent, 
				      boolean editable,
				      final IFieldListener listener ) {
			parent.setLayout( new GridLayout() );
			_text = new Text( parent, SWT.WRAP );
			_text.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			if ( editable ) {
				_text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						listener.fieldModified();
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
		
		
	}










	
}
