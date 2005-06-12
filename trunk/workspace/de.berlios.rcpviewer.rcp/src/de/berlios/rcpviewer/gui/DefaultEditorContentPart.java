package de.berlios.rcpviewer.gui;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IPartSelectionListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import de.berlios.rcpviewer.rcp.RcpViewerPlugin;

/**
 * A nested form that contains an IFormPart for every attribute in a given POJO.
 */
public class DefaultEditorContentPart 
implements IManagedForm, IFormPart
{
	private ArrayList<IFormPart> _parts= new ArrayList<IFormPart>();
	private Object _container;
	private FormToolkit _toolkit;
	private ScrolledForm _form;
	private boolean initialized= false;
	private Object _input;
	private Object _pojo;
	private IManagedForm _parentForm;
	

	public DefaultEditorContentPart(IManagedForm parentForm, Object pPojo) {
		if ( parentForm == null ) throw new IllegalArgumentException();
		if ( pPojo == null ) throw new IllegalArgumentException();
		
		_parentForm= parentForm;
		_toolkit= parentForm.getToolkit();
		
		_form= parentForm.getForm();
		_form.setText( pPojo.getClass().getName()+":"+pPojo.hashCode() );

		_pojo= pPojo;
		createGui();
		setFormInput(pPojo);
	}
	
	/**
	 * Currently does model stuff do.
	 */
	protected void createGui() {
		
		Composite body = _form.getBody();
		body.setLayout( new GridLayout( 2, false ) );

		// discover methods - stuff the model should do
		Method[] methods = _pojo.getClass().getMethods();
		
		// add a field for each getter method:
		for ( int i=0, num = methods.length; i < num ; i++ ) {
			String name = methods[i].getName();
			if ( name.startsWith( "get" ) ) {
				
				Method getMethod= methods[i];
				Method setMethod= null;
				try {
					String setMethodName= "set"+name.substring(3);
					setMethod= _pojo.getClass().getMethod(setMethodName, new Class[] { getMethod.getReturnType() });
				}
				catch (NoSuchMethodException x) {
					// fall through
				}
				if (setMethod != null) {
					
					// label
					Label label = new Label( body, SWT.NONE );
					label.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
					label.setText( name.substring( 3 ) + ":" );
					label.setBackground( body.getBackground() );
					
					// create parent composite for field
					Composite fieldComposite = new Composite( body, SWT.NONE );
					fieldComposite.setLayoutData( 
							new GridData( GridData.FILL_HORIZONTAL ) );
					fieldComposite.setBackground( body.getBackground() );
					_toolkit.paintBordersFor( fieldComposite );
					
					// get value and create approriate gui
					Object value = null;
					try {
						assert methods[i].getParameterTypes().length == 0;
						value = methods[i].invoke( _pojo, (Object[])null );
						// create correct field builder
						IFieldBuilder fieldBuilder
							= RcpViewerPlugin.getDefault().getFieldBuilderFactory().getInstance( 
									methods[i].getReturnType(), value  );
						IFormPart formPart= fieldBuilder.createFormPart( fieldComposite,getMethod, setMethod, null);
						addPart(formPart);
					}
					catch ( Exception ex ) {
						// exceptions must have specialist displays...
						IFieldBuilder fieldBuilder
							= RcpViewerPlugin.getDefault().getFieldBuilderFactory().getInstance( 
									ex.getClass(), ex  );
						IFormPart formPart= fieldBuilder.createFormPart( fieldComposite, null, null, ex );
						addPart(formPart);
					}	
				}
			}
		}
	}

	public void addPart(IFormPart part) {
		if (_parts.contains(part) == false) {
			_parts.add(part);
			part.initialize(this);
		}		
	}

	public void dirtyStateChanged() {
		_parentForm.dirtyStateChanged();
	}

	public void fireSelectionChanged(IFormPart part, ISelection pSelection) {
		for (IFormPart cpart: _parts) {
			if (part.equals(cpart))
				continue;
			if (cpart instanceof IPartSelectionListener) {
				((IPartSelectionListener) cpart).selectionChanged(part, pSelection);
			}
		}
	}

	public Object getContainer() {
		return _container;
	}

	public ScrolledForm getForm() {
		return _form;
	}

	public Object getInput() {
		return _input;
	}

	public IFormPart[] getParts() {
		return (IFormPart[])_parts.toArray();
	}

	public FormToolkit getToolkit() {
		return _toolkit;
	}

	public void initialize() {
		if (initialized) return;
		for (IFormPart part: _parts) {
			part.initialize(this);
		}
		initialized=true;
	}

	public void reflow(boolean pChanged) {
		_form.reflow(pChanged);
	}

	public void removePart(IFormPart part) {
		_parts.remove(part);
	}

	public void setContainer(Object pContainer) {
		_container= pContainer;
		
	}

	public boolean setInput(Object pInput) {
		boolean pageResult=false;		
		_input = pInput;
		for (IFormPart part: _parts) {
			boolean result = part.setFormInput(pInput);
			if (result)
				pageResult = true;
		}
		return pageResult;
	}

	public void staleStateChanged() {
		// do nothing
		
	}

	public void dispose() {
		for (IFormPart formPart: _parts) {
			formPart.dispose();
		}
	}

	public void initialize(IManagedForm pForm) {
		// TODO Auto-generated method stub
		
	}

	public void setFocus() {
		if (_parts.isEmpty() == false) {
			_parts.get(0).setFocus();
		}
	}

	public boolean setFormInput(Object pInput) {
		boolean result= true;
		for (IFormPart formPart: _parts) {
			if (formPart.setFormInput(pInput) == false)
				result= false;
		}
		return result;
	}

	public void commit(boolean pOnSave) {
		for (IFormPart formPart: _parts) {
			formPart.commit(pOnSave);
		}
	}

	public boolean isDirty() {
		for (IFormPart formPart: _parts) {
			if (formPart.isDirty())
				return true;
		}
		return false;
	}

	public boolean isStale() {
		for (IFormPart formPart: _parts) {
			if (formPart.isStale())
				return true;
		}
		return false;
	}

	public void refresh() {
		for (IFormPart formPart: _parts) {
			formPart.refresh();
		}
	}

	
	
	
}
