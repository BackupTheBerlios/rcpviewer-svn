package de.berlios.rcpviewer.gui.editors.exts;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import de.berlios.rcpviewer.gui.GuiPlugin;


/**
 * Generates a label and a checkbox.
 * @author Mike
 */
class BooleanFieldPart implements IFormPart {
	
	private final Composite _parent;
	private final Method _getMethod;
	private final Method _setMethod;
	private final Button _button;
	
	private Object _input;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	
	/**
	 * @param parent
	 * @param getMethod
	 * @param setMethod
	 */
	BooleanFieldPart(Composite parent, Method getMethod,Method setMethod) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null

		_parent= parent;
		_getMethod= getMethod;
		_setMethod= setMethod;
		
		parent.setLayout( new GridLayout() );
		_button= new Button( parent, SWT.CHECK );
		_button.setLayoutData( new GridData() );
		_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pE) {
				setDirty(true);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		try {
			if (_setMethod != null)
				_setMethod.invoke(_input, new Object[] { _button.getSelection() ? Boolean.TRUE : Boolean.FALSE });
			setDirty(false);
		} catch (Exception e) {
			String msg= e.getMessage();
			if (msg == null)
				msg= "";
			Status status= new Status(
					IStatus.WARNING, 
					GuiPlugin.getDefault().getBundle().getSymbolicName(), 
					0, 
					msg, 
					e);
			GuiPlugin.getDefault().getLog().log(status);
		} 
	}

	/**
	 * @param value
	 */
	private void setDirty(boolean value) {
		if (_isDirty != value) {
			_isDirty= value;
			_managedForm.dirtyStateChanged();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		// do nothing		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm pForm) {
		_managedForm= pForm;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return _isDirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		Boolean value= Boolean.FALSE;
		if (_input != null && _getMethod != null) {
			try {
				value= (Boolean)_getMethod.invoke(_input, null);
			}
			catch (Exception x) {
				throw new RuntimeException(x);
			}
		}
		_button.setSelection(value);
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_button.setFocus();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object pInput) {
		_input= pInput;
		refresh();
		return true;		
	}

}
