package de.berlios.rcpviewer.gui.editors;

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

import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * A nested form that contains an IFormPart for every attribute in a given POJO.
 */
class DefaultEditorContentPart implements IManagedForm, IFormPart {
	
	private final IManagedForm _parentForm;
	private final Object _pojo;
	private final ArrayList<IFormPart> _parts;
	
	// REVIEW_CHANGE for Ted - actually no change but why can we not delegate to
	// parentForm?
	private Object _container;
	private Object _input;

	

	/**
	 * @param parentForm
	 * @param pPojo
	 */
	DefaultEditorContentPart( IManagedForm parentForm, Object pPojo) {
		if ( parentForm == null ) throw new IllegalArgumentException();
		if ( pPojo == null ) throw new IllegalArgumentException();
		
		_parentForm= parentForm;
		_pojo= pPojo;
		_parts = new ArrayList<IFormPart>();
		
		// form title
		StringBuffer sb = new StringBuffer();
		sb.append( pPojo.getClass().getSimpleName() );
		sb.append( ":" );
		sb.append( pPojo.hashCode() );
		getForm().setText( sb.toString() );
		
		createGui();
		setFormInput( pPojo );
	}
	
	/**
	 * Currently does own reflction to wokr out attributes.
	 * REVIEW_CHANGE for Ted - any reason not using IDomainObject?
	 */
	protected void createGui() {
		
		Composite body = getForm().getBody();
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
					getToolkit().paintBordersFor( fieldComposite );
					
					// get value and create approriate gui
					Object value = null;
					try {
						assert methods[i].getParameterTypes().length == 0;
						value = methods[i].invoke( _pojo, (Object[])null );
						// create correct field builder
						IFieldBuilder fieldBuilder
							= GuiPlugin.getDefault().getFieldBuilderFactory().getInstance( 
									methods[i].getReturnType(), value  );
						IFormPart formPart= fieldBuilder.createFormPart( fieldComposite,getMethod, setMethod, null);
						addPart(formPart);
					}
					catch ( Exception ex ) {
						// exceptions must have specialist displays...
						IFieldBuilder fieldBuilder
							= GuiPlugin.getDefault().getFieldBuilderFactory().getInstance( 
									ex.getClass(), ex  );
						IFormPart formPart= fieldBuilder.createFormPart( fieldComposite, null, null, ex );
						addPart(formPart);
					}	
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#addPart(org.eclipse.ui.forms.IFormPart)
	 */
	public void addPart(IFormPart part) {
		if (_parts.contains(part) == false) {
			_parts.add(part);
			part.initialize(this);
		}		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#dirtyStateChanged()
	 */
	public void dirtyStateChanged() {
		_parentForm.dirtyStateChanged();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#fireSelectionChanged(org.eclipse.ui.forms.IFormPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void fireSelectionChanged(IFormPart part, ISelection pSelection) {
		for (IFormPart cpart: _parts) {
			if (part.equals(cpart))
				continue;
			if (cpart instanceof IPartSelectionListener) {
				((IPartSelectionListener) cpart).selectionChanged(part, pSelection);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#getContainer()
	 */
	public Object getContainer() {
		return _container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#getForm()
	 */
	public ScrolledForm getForm() {
		return _parentForm.getForm();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#getInput()
	 */
	public Object getInput() {
		return _input;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#getParts()
	 */
	public IFormPart[] getParts() {
		return (IFormPart[])_parts.toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#getToolkit()
	 */
	public FormToolkit getToolkit() {
		return _parentForm.getToolkit();
	}

	// REVIEW_CHANGE for Ted - this methoid removed as not used
	/*
	public void initialize() {
		if (initialized) return;
		for (IFormPart part: _parts) {
			part.initialize(this);
		}
		initialized=true;
	}
	*/

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#reflow(boolean)
	 */
	public void reflow(boolean pChanged) {
		getForm().reflow(pChanged);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#removePart(org.eclipse.ui.forms.IFormPart)
	 */
	public void removePart(IFormPart part) {
		_parts.remove(part);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#setContainer(java.lang.Object)
	 */
	public void setContainer(Object pContainer) {
		_container= pContainer;
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#setInput(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IManagedForm#staleStateChanged()
	 */
	public void staleStateChanged() {
		// do nothing
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		for (IFormPart formPart: _parts) {
			formPart.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm pForm) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		if (_parts.isEmpty() == false) {
			_parts.get(0).setFocus();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object pInput) {
		boolean result= true;
		for (IFormPart formPart: _parts) {
			if (formPart.setFormInput(pInput) == false)
				result= false;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean pOnSave) {
		for (IFormPart formPart: _parts) {
			formPart.commit(pOnSave);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		for (IFormPart formPart: _parts) {
			if (formPart.isDirty())
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		for (IFormPart formPart: _parts) {
			if (formPart.isStale())
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		for (IFormPart formPart: _parts) {
			formPart.refresh();
		}
	}

	
	
	
}
