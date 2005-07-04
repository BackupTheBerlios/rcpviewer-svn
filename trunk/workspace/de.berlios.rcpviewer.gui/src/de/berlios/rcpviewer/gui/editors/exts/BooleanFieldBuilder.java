package de.berlios.rcpviewer.gui.editors.exts;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.berlios.rcpviewer.gui.editors.IFieldBuilder;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;

public class BooleanFieldBuilder implements IFieldBuilder {

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#isApplicable(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isApplicable(EAttribute attribute) {
		return Boolean.class == attribute.getEType().getInstanceClass();
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.editors.IFieldBuilder#createField(org.eclipse.swt.widgets.Composite, boolean, de.berlios.rcpviewer.gui.editors.IFieldBuilder.IFieldListener)
	 */
	public IField createField(Composite parent, boolean editable, IFieldListener listener) {
		return new BooleanField( parent, editable, listener );
	}
	
	private class BooleanField implements IField {
		
		private final Button _button;
		
		BooleanField( Composite parent, 
				      boolean editable,
				      final IFieldListener listener ) {
			parent.setLayout( new GridLayout() );

			parent.setLayout( new GridLayout() );
			_button= new Button( parent, SWT.CHECK );
			_button.setLayoutData( new GridData() );
			_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent pE) {
					listener.fieldModified();
				}
			});
			
			if ( editable ) {
				_button.addSelectionListener( new DefaultSelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent pE) {
						listener.fieldModified();
					}
				});
			}
			else {
				_button.setEnabled( false );
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
	}
}
