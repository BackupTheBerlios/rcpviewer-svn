package org.essentialplatform.louis.editors;


import static org.essentialplatform.louis.util.FontUtil.CharWidthType.AVERAGE;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.part.EditorPart;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.ConfigureWidgetFactory;
import org.essentialplatform.louis.configure.IConfigurable;
import org.essentialplatform.louis.editors.opsview.OpsViewPage;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.jobs.JobAction;
import org.essentialplatform.louis.jobs.RefreshDomainObjectJob;
import org.essentialplatform.louis.jobs.SaveJob;
import org.essentialplatform.louis.util.FontUtil;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.views.ops.IOpsViewPage;

import org.essentialplatform.runtime.session.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.session.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.session.IDomainObjectAttributeListener;
import org.essentialplatform.runtime.transaction.ITransactable;
import org.essentialplatform.runtime.transaction.ITransaction;
import org.essentialplatform.runtime.transaction.internal.TransactionManager;

/**
 * Core editor for GUI layer.
 * @author Mike
 * @see org.essentialplatform.louis.fieldbuilders.IFieldBuilder
 * @see org.essentialplatform.louis.fieldbuilders.IFieldBuilder.IField
 */
public final class DefaultEditor extends EditorPart {
	
	public static final String ID = DefaultEditor.class.getName();
	
	private static final String FORM_TITLE_PREFIX = "  "; //$NON-NLS-1$
	
	private ManagedForm _form = null;
	private OpsViewPage _opsView = null;
	private IDomainObjectAttributeListener _nameListener = null;
	
	
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
		setTitleImage( ((DefaultEditorInput)input).getImage() );
		
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
		_form = new ManagedForm( parent ) {
			public void dirtyStateChanged() {
				super.dirtyStateChanged();
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		};
		
		// set title on both window and form, and add icon to form
		resetTitle();
		int width = FORM_TITLE_PREFIX.length() * FontUtil.getCharWidth( 
				_form.getForm(), AVERAGE );
		Image image = ImageUtil.resize(
				((DefaultEditorInput)getEditorInput() ).getImage(),
				new Point( width, width ) );
		_form.getForm().setBackgroundImage( image  );
		
		// main gui creation
		IGuiFactory factory = LouisPlugin.getDefault().getGuiFactory(
				getDomainObject().getDomainClass(), null );
		IFormPart part = factory.createGui(
				getDomainObject().getDomainClass(),
				_form.getToolkit(),
				_form.getForm().getBody(),
				GuiHints.DUMMY );
		_form.addPart( part );
		
		// add configuration option...
		if ( part instanceof IConfigurable ) {
			IToolBarManager mgr = _form.getForm().getToolBarManager();
			mgr.add( ConfigureWidgetFactory.createAction( (IConfigurable)part ) );
			mgr.update( true );
			
		}
		

		// listen in on attribute changes in case they affect the name
		_nameListener = new IDomainObjectAttributeListener(){
			public void attributeChanged(DomainObjectAttributeEvent event){
				// check if name generated by input is different to current name
				String newName = getEditorInput().getName();
				if( !getPartName().equals( newName  ) ) {
					resetTitle();
				}
			}
			public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent event) {
			}
		};
		for ( EAttribute a : getDomainObject().getDomainClass().eAttributes() ) {
			getDomainObject().getAttribute( a ).addListener( _nameListener );
		}
		
		// finally set form input
		_form.setInput( getDomainObject() );
		_form.commit( false );
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
		if ( _form != null) {
			_form.dispose();
		}
		if ( _opsView != null ) {
			_opsView.dispose();
		}
		if ( _nameListener != null ) {
			IDomainObject<?> object = getDomainObject();
			for ( EAttribute a : object.getDomainClass().eAttributes() ) {
				object.getAttribute( a ).removeListener( _nameListener );
			}
		}
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return _form != null && _form.isDirty();
	}
	
	/**
	 * The transaction, if any, into which the pojo that this editor relates 
	 * has been enlisted.
	 * 
	 * @return
	 */
	private ITransaction getCurrentTransaction() {
		ITransactable transactable = (ITransactable)getDomainObject().getPojo();
		return  
			TransactionManager.instance().getCurrentTransactionFor(transactable, false);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		SaveJob save = new SaveJob( _form );
		
		// for now run sync rather than scheduling
		if ( Status.OK_STATUS == save.runInUIThread( monitor ) ) {
			// TODO reallly should pick this up from a listening machanism...
			firePropertyChange( IEditorPart.PROP_DIRTY );
		}
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

	/* private methods */
	
	// sets the title on both window and form
	private void resetTitle() {
		String title = ((DefaultEditorInput)getEditorInput()).getName();
		setPartName( title );
		_form.getForm().setText( FORM_TITLE_PREFIX + title );
	}

	
	// extract from input
	private IDomainObject<?> getDomainObject() {
		assert getEditorInput() != null;
		assert getEditorInput() instanceof DefaultEditorInput;
		return ((DefaultEditorInput)getEditorInput()).getDomainObject();
	}
}
