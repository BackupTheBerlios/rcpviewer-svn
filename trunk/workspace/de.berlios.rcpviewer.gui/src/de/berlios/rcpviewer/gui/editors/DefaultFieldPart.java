package de.berlios.rcpviewer.gui.editors;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Creates an editable Text box.
 * @author Mike
 */
class DefaultFieldPart implements IFormPart {
	
	private final Composite _parent;
	private final Method _getMethod;
	private final Method _setMethod;
	private final Text _text;
	
	private Object _input;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	
	/**
	 * @param parent
	 * @param getMethod
	 * @param setMethod
	 */
	DefaultFieldPart(Composite parent, Method getMethod,Method setMethod) {
		if ( parent == null ) throw new IllegalArgumentException();
		// value could be null

		_parent= parent;
		_getMethod= getMethod;
		_setMethod= setMethod;
		
		parent.setLayout( new GridLayout() );
		_text = new Text( parent, SWT.WRAP );
		_text.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		_text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			};
		});
	}
	
	/* IFormPart contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		try {
			if (_setMethod != null)
				_setMethod.invoke(_input, new Object[] { _text.getText() });
			setDirty(false);
		} catch (Exception e) {
			Status status= new Status(
					IStatus.WARNING, 
					GuiPlugin.getDefault().getBundle().getSymbolicName(), 
					0, 
					e.getMessage(), 
					e);
			GuiPlugin.getDefault().getLog().log(status);
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
		String display = null;
		if ( _input != null && _getMethod != null) {
			try {
				display = (String)_getMethod.invoke(_input, null);
			}
			catch (Exception x) {
				// do nothin
			}
		}
		if (display == null)
			display = "null";
		_text.setText( display );
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_text.setFocus();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object pInput) {
		_input= pInput;
		refresh();
		return true;		
	}
	
	/* private methods */
	
	// as it says
	private void setDirty(boolean value) {
		assert _managedForm != null;
		if (_isDirty != value) {
			_isDirty= value;
			_managedForm.dirtyStateChanged();
		}
	}

}
