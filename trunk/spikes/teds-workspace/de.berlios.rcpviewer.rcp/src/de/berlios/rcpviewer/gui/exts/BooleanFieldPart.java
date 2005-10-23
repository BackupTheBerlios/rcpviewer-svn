package de.berlios.rcpviewer.gui.exts;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import de.berlios.rcpviewer.RCPViewer;
import de.berlios.rcpviewer.rcp.RcpViewerPlugin;

public class BooleanFieldPart implements IFormPart {
	private Composite _parent;
	private Method _getMethod;
	private Method _setMethod;
	private Object _input;
	private Button _button;
	private IManagedForm _managedForm;
	private boolean _isDirty= false;

	public BooleanFieldPart(Composite parent, Method getMethod,Method setMethod) {
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
	
	public void commit(boolean pOnSave) {
		try {
			if (_setMethod != null)
				_setMethod.invoke(_input, new Object[] { _button.getSelection() ? Boolean.TRUE : Boolean.FALSE });
			setDirty(false);
		} catch (Exception e) {
			String msg= e.getMessage();
			if (msg == null)
				msg= "";
			Status status= new Status(Status.WARNING, RCPViewer.PLUGIN_ID, 0, msg, e);
			RcpViewerPlugin.getDefault().getLog().log(status);
		} 
	}

	private void setDirty(boolean value) {
		if (_isDirty != value) {
			_isDirty= value;
			_managedForm.dirtyStateChanged();
		}
	}

	public void dispose() {
		// do nothing		
	}

	public void initialize(IManagedForm pForm) {
		_managedForm= pForm;		
	}

	public boolean isDirty() {
		return _isDirty;
	}

	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

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

	public void setFocus() {
		_button.setFocus();		
	}

	public boolean setFormInput(Object pInput) {
		_input= pInput;
		refresh();
		return true;		
	}

	
	public boolean isApplicable(Class clazz, Object value) {
		return Boolean.class ==  clazz;
	}


}
