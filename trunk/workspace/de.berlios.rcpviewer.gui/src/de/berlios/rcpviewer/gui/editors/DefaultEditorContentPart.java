package de.berlios.rcpviewer.gui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
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

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * A nested form that contains an IFormPart for every attribute in a given 
 * <code>IDomainObject</code>
 */
class DefaultEditorContentPart implements IManagedForm, IFormPart {
	
	private final IManagedForm _parentForm;
	private final IDomainObject _object;
	private final ArrayList<IFormPart> _parts;
	
	// REVIEW_CHANGE for Ted - actually no change but why can we not delegate to parentForm? -- mike
	private Object _container;
	private Object _input;

	

	/**
	 * @param parentForm
	 * @param object
	 */
	DefaultEditorContentPart( IManagedForm parentForm, IDomainObject object) {
		if ( parentForm == null ) throw new IllegalArgumentException();
		if ( object == null ) throw new IllegalArgumentException();
		
		_parentForm= parentForm;
		_object= object;
		_parts = new ArrayList<IFormPart>();
		
		// form title
		StringBuffer sb = new StringBuffer();
		sb.append( _object.getPojo().getClass().getSimpleName() );
		sb.append( ":" );
		sb.append( _object.getPojo().hashCode() );
		getForm().setText( sb.toString() );
		
		createGui();
		setFormInput( object );
	}
	
	// as it says
	private void createGui() {
		
		Composite body = getForm().getBody();
		body.setLayout( new GridLayout( 2, false ) );
		
		// loop through all attributes - add a field for each
		IDomainClass clazz = _object.getDomainClass();
		for ( Object a : clazz.attributes() ) {
			EAttribute attribute = (EAttribute)a;
			
			// label
			Label label = new Label( body, SWT.NONE );
			label.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
			label.setText( attribute.getName() + ":" );
			label.setBackground( body.getBackground() );
			
			// create parent composite for field
			Composite fieldComposite = new Composite( body, SWT.NONE );
			fieldComposite.setLayoutData( 
					new GridData( GridData.FILL_HORIZONTAL ) );
			fieldComposite.setBackground( body.getBackground() );
			getToolkit().paintBordersFor( fieldComposite );
			
			IFieldBuilder fieldBuilder
				= GuiPlugin.getDefault()
							.getFieldBuilderFactory()
							.getInstance( attribute );
			IFormPart formPart= fieldBuilder.createFormPart( 
					fieldComposite, _object, attribute);
			addPart(formPart);
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
		if ( !(pInput instanceof IDomainObject ) ) {
			throw new IllegalArgumentException();
		}
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
		if ( !(pInput instanceof IDomainObject ) ) {
			throw new IllegalArgumentException();
		}
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

	// REVIEW_CHANGE for Mike added to get clean compile on 3.1 -- dan
	public void initialize() {
		
	}

	
	
	
}
