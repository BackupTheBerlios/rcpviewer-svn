package de.berlios.rcpviewer.gui.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Core editor for GUI layer.
 * @author Mike
 *
 */
public final class DefaultEditor extends EditorPart {
	
	public static final String ID = "de.berlios.rcpviewer.rcp.objectEditor";

	private IManagedForm _form = null;
	private FormToolkit _toolkit = null;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if ( site == null ) throw new IllegalArgumentException();
		if ( input == null ) throw new IllegalArgumentException();
		if ( !(input instanceof DefaultEditorInput) ) throw new IllegalArgumentException();
		
		_toolkit= new FormToolkit( site.getShell().getDisplay() );

		setSite( site );
		setInput( input );
		
		Object pojo = ((DefaultEditorInput)input).getDomainObject();
		StringBuffer sb = new StringBuffer();
		sb.append( pojo.getClass().getName() );
		sb.append( ":" );
		sb.append( pojo.hashCode() );
		setPartName( sb.toString() );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		assert getEditorInput() != null;
		assert getEditorInput() instanceof DefaultEditorInput;
		_form = new ManagedForm(_toolkit, _toolkit.createScrolledForm(parent)) {
			public void dirtyStateChanged() {
				super.dirtyStateChanged();
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		};
		DefaultEditorInput input = (DefaultEditorInput)getEditorInput();
		input.getBuilder().createGui( _form, input.getDomainObject().getPojo());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if ( _form != null ) {
			_form.getForm().setFocus();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		if (_toolkit != null)
			_toolkit.dispose();
		
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		if ( getDomainObject().isPersistent() == false)
			return true;
		if (_form == null)
			return false;
		return _form.isDirty();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		_form.commit(true);
		getDomainObject().persist();
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
		
	}
	
	// extract from input
	private IDomainObject<?> getDomainObject() {
		assert getEditorInput() != null;
		assert getEditorInput() instanceof DefaultEditorInput;
		return ((DefaultEditorInput)getEditorInput()).getDomainObject();
	}
}
