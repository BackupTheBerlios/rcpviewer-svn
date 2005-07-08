package de.berlios.rcpviewer.gui.editors;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.fields.FieldBuilderFactory;
import de.berlios.rcpviewer.gui.fields.IFieldBuilder;
import de.berlios.rcpviewer.gui.views.actions.IActionsViewPage;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Core editor for GUI layer.
 * <br>Gui built up of <code>IField</code>s provided by 
 * <code>IFieldBuilder</code>'s.
 * @author Mike
 * @see de.berlios.rcpviewer.gui.fields.IFieldBuilder
 * @see de.berlios.rcpviewer.gui.fields.IFieldBuilder.IField
 */
public final class DefaultEditor extends EditorPart {
	
	public static final String ID = "de.berlios.rcpviewer.rcp.objectEditor";
	
	private FieldBuilderFactory _fieldBuilderFactory = null;
	private IManagedForm _form = null;
	private FormToolkit _toolkit = null;
	private ActionsViewPage _actions = null;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if ( site == null ) throw new IllegalArgumentException();
		if ( input == null ) throw new IllegalArgumentException();
		if ( !(input instanceof DefaultEditorInput) ) throw new IllegalArgumentException();
		
		// instantiate field build factory if necessary
		try {
			_fieldBuilderFactory = FieldBuilderFactory.instance();
		}
		catch ( CoreException ce ) {
			throw new PartInitException( ce.getStatus() );
		}

		setSite( site );
		setInput( input );
		setPartName( input.getName() );
		_toolkit = new FormToolkit( site.getShell().getDisplay() );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		assert _fieldBuilderFactory != null;
		
		// main form
		_form = new ManagedForm( _toolkit, _toolkit.createScrolledForm(parent)) {
			public void dirtyStateChanged() {
				super.dirtyStateChanged();
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		};
		_form.getForm().setText( getEditorInput().getName() );
		
		// create form gui
		Composite body = _form.getForm().getBody();
		body.setLayout( new GridLayout( 2, false ) );
		
		// loop through all attributes - add a label and an IField for each
		IDomainObject object = getDomainObject();
		IDomainClass clazz = object.getDomainClass(); // JAVA_5_FIXME
		for ( Object a : clazz.attributes() ) {       // JAVA_5_FIXME
			EAttribute attribute = (EAttribute)a;
			
			// label
			Label label = new Label( body, SWT.NONE );
			label.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
			label.setText( attribute.getName() + ":" );
			label.setBackground( body.getBackground() );
			
			// create parent composite for IField
			Composite fieldComposite = new Composite( body, SWT.NONE );
			fieldComposite.setLayoutData( 
					new GridData( GridData.FILL_HORIZONTAL ) );
			fieldComposite.setBackground( body.getBackground() );
			_toolkit.paintBordersFor( fieldComposite );
			
			// create IField
			IFieldBuilder fieldBuilder
				= _fieldBuilderFactory.getInstance( attribute );
			FieldPart fieldPart = new FieldPart(
					fieldComposite,
					fieldBuilder,
					object,
					attribute );
			_form.addPart( fieldPart );
			fieldPart.initialize( _form );
		}
		
		_form.setInput( object );
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
		if (_toolkit != null) {
			_toolkit.dispose();
		}
		if ( _actions != null ) {
			_actions.dispose();
		}
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
	
	/**
	 * Always returns <code>false</code>
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {		
		return false;
	}

	/**
	 * Always throws an <code>UnsupportedOperationException</code>
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
		
	}
	
	/**
	 * Returns:
	 * <ol>
	 * <li>if Class is IPropertySheetPage, the individual instance of 
	 * actions page for this editor / object
	 * <li>super()
	 * </ol>
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 * @see org.eclipse.ui.views.properties.IPropertySheetPage
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if ( adapter.equals( IActionsViewPage.class ) ) {
			if ( _actions == null ) {
				_actions = new ActionsViewPage( getDomainObject() );
			}
			return _actions;
		}
		return super.getAdapter(adapter);
	}

	// extract from input
	private IDomainObject<?> getDomainObject() {
		assert getEditorInput() != null;
		assert getEditorInput() instanceof DefaultEditorInput;
		return ((DefaultEditorInput)getEditorInput()).getDomainObject();
	}
}
