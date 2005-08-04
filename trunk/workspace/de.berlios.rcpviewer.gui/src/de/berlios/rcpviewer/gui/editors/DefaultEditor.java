package de.berlios.rcpviewer.gui.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.jobs.JobAction;
import de.berlios.rcpviewer.gui.jobs.RefreshDomainObjectJob;
import de.berlios.rcpviewer.gui.util.GCUtil;
import de.berlios.rcpviewer.gui.views.ops.IOpsViewPage;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Core editor for GUI layer.
 * <br>Gui built up of <code>IField</code>s provided by 
 * <code>IFieldBuilder</code>'s.
 * @author Mike
 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder
 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder.IField
 */
public final class DefaultEditor extends EditorPart {
	
	public static final String ID = "de.berlios.rcpviewer.rcp.objectEditor"; //$NON-NLS-1$
	
	private IManagedForm _form = null;
	private FormToolkit _toolkit = null;
	private OpsViewPage _opsView = null;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if ( site == null ) throw new IllegalArgumentException();
		if ( input == null ) throw new IllegalArgumentException();
		if ( !(input instanceof DefaultEditorInput) ) throw new IllegalArgumentException();
		
		setSite( site );
		setInput( input );
		setPartName( input.getName() );
		_toolkit = new FormToolkit( site.getShell().getDisplay() );
		
		// refresh action
		site.getActionBars().setGlobalActionHandler(
			ActionFactory.REFRESH.getId(),
			new JobAction( new RefreshDomainObjectJob( getDomainObject() ) ) ) ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		
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
		body.setLayout( new GridLayout() );
		
		// want column width hints for IField's
		// for this calculate longest required label length for attributes
		int[] columnWidths = new int[]{ 0, 0 };
		IDomainObject<?> object = getDomainObject();
		IDomainClass<?> clazz = object.getDomainClass(); 
		int maxLength = 0;
		for ( Object a : clazz.attributes() ) {       
			int length = ((EAttribute)a).getName().length();
			if ( length > maxLength ) maxLength = length;
		}
		columnWidths[0] = maxLength * GCUtil.getSafeCharWidth( parent ) ;
		
		// loop through all attributes - add an IField for each
		for ( Object a : clazz.attributes() ) {       
			EAttribute attribute = (EAttribute)a;
			
			// create parent composite for IField
			Composite fieldComposite = _toolkit.createComposite( body );
			fieldComposite.setLayoutData( 
					new GridData( GridData.FILL_HORIZONTAL ) );
			_toolkit.paintBordersFor( fieldComposite );
			
			// create IField
			IFieldBuilder fieldBuilder
				= GuiPlugin.getDefault().getFieldBuilder( attribute );
			FieldPart fieldPart = new FieldPart(
					fieldComposite,
					fieldBuilder,
					object,
					attribute,
					columnWidths );
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
		if ( _opsView != null ) {
			_opsView.dispose();
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
	 * <li>if Class is IOpsViewPage, the individual instance of 
	 * ops page for this editor / object
	 * <li>super()
	 * </ol>
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 * @see org.eclipse.ui.views.properties.IPropertySheetPage
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if ( adapter.equals( IOpsViewPage.class ) ) {
			if ( _opsView == null ) {
				_opsView = new OpsViewPage( getDomainObject() );
			}
			return _opsView;
		}
		return super.getAdapter(adapter);
	}
	
	/* Non-platform public methods */
	
	/**
	 * Refreshes the display if any.
	 */
	public void refresh() {
		if ( _form != null ) _form.refresh();
	}

	// extract from input
	private IDomainObject<?> getDomainObject() {
		assert getEditorInput() != null;
		assert getEditorInput() instanceof DefaultEditorInput;
		return ((DefaultEditorInput)getEditorInput()).getDomainObject();
	}
}
