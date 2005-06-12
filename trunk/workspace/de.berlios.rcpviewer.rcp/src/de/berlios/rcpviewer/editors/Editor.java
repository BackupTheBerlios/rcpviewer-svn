package de.berlios.rcpviewer.editors;

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

import de.berlios.rcpviewer.gui.IEditorContentBuilder;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;

public final class Editor extends EditorPart {
	
	private IDomainObject _domainObject;
	private RcpViewerEditorInput _editorInput;
	private IEditorContentBuilder  _contentBuilder;
	private IManagedForm _form;
	private FormToolkit _toolkit;

	@Override
	public void doSave(IProgressMonitor monitor) {
		_form.commit(true);
		_domainObject.persist();
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		if (_toolkit != null)
			_toolkit.dispose();
		
		super.dispose();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		_editorInput= (RcpViewerEditorInput)input;
		_domainObject= _editorInput.getDomainObject();
		_contentBuilder= _editorInput.getBuilder();
		_toolkit= new FormToolkit( site.getShell().getDisplay() );

		
		setSite( site );
		setInput( input );
		
		Object pojo= _domainObject.getPojo();
		setPartName( pojo.getClass().getName()+":"+pojo.hashCode() );
	}

	@Override
	public boolean isDirty() {
		if (_domainObject.isPersistent() == false)
			return true;
		if (_form == null)
			return false;
		return _form.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {		
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		_form= new ManagedForm(_toolkit, _toolkit.createScrolledForm(parent)) {
			public void dirtyStateChanged() {
				super.dirtyStateChanged();
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		};
		_contentBuilder.createGui( _form, _domainObject.getPojo());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	
}
