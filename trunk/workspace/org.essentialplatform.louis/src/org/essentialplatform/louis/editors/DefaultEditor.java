package org.essentialplatform.louis.editors;


import static org.essentialplatform.louis.util.FontUtil.CharWidthType.AVERAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabFolderListener;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;

import org.essentialplatform.runtime.client.domain.bindings.IObjectAttributeClientBinding;
import org.essentialplatform.runtime.client.domain.event.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.client.domain.event.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectAttributeListener;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

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

/**
 * Core editor for GUI layer.
 * @author Mike
 * @see org.essentialplatform.louis.fieldbuilders.IFieldBuilder
 * @see org.essentialplatform.louis.fieldbuilders.IFieldBuilder.IField
 */
public final class DefaultEditor extends EditorPart {
	
	public static final String ID = DefaultEditor.class.getName();
	
	/**
	 * Space for the icon.
	 */
	private static final String FORM_TITLE_PREFIX = "      "; //$NON-NLS-1$
	
	private ManagedForm _form = null;
	private OpsViewPage _opsView = null;
	private IDomainObjectAttributeListener _titleListener = null;

	/**
	 * Composite residing on the editor tab holding default editor
	 */
	private Composite _editorComposite;
	/**
	 * Composite residing on the shell tab
	 */
	private Composite _shellComposite;
	/**
	 * Composite residing on the help tab
	 */
	private Composite _helpComposite;

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
		ScrolledForm scrolledForm = _form.getForm();
		scrolledForm.setBackground(parent.getBackground());

		// set title on both window and form, and add icon to form
		DefaultEditorInput<?> editorInput = getDefaultEditorInput();
		String title = editorInput.getName();
		setPartName( title );
		scrolledForm.setText( FORM_TITLE_PREFIX + title );
		
		// REVIEW_CHANGE: Dan hacking: wanted a bigger icon on the editor.  Please do it right, though.
		//int width = FORM_TITLE_PREFIX.length() * FontUtil.getCharWidth(scrolledForm, AVERAGE );
		int width = 32;
		Image image = ImageUtil.resize(
				editorInput.getImage(),
				new Point( width, width ) );
		scrolledForm.setBackgroundImage( image  );

		FormToolkit formToolkit = _form.getToolkit();
		Composite formBody = scrolledForm.getBody();
		formBody.setBackground( parent.getBackground() );
		formBody.setLayout(new GridLayout());
		
		CTabFolder tabFolder = new CTabFolder(formBody, SWT.FLAT|SWT.BOTTOM);
		formToolkit.adapt(tabFolder, true, true);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		_editorComposite = createTab(formToolkit, tabFolder, "Object"); 
		_shellComposite = createTab(formToolkit, tabFolder, "Shell");
		_helpComposite = createTab(formToolkit, tabFolder, "Help");
		
		tabFolder.setSelection(0);
		
		// main gui creation (on the editor tab)
		IGuiFactory factory = LouisPlugin.getDefault().getGuiFactory(
				getDomainObject().getDomainClass(), null );
		IFormPart part = factory.createGui(
				getDomainObject().getDomainClass(),
				formToolkit,
				_editorComposite,
				GuiHints.DUMMY );
		_form.addPart( part );
		
		// add configuration option...
		if ( part instanceof IConfigurable ) {
			IToolBarManager mgr = scrolledForm.getToolBarManager();
			mgr.add( ConfigureWidgetFactory.createAction( (IConfigurable)part ) );
			mgr.update( true );
			
		}

		// listen in on attribute changes in case they affect the name
		_titleListener = new IDomainObjectAttributeListener(){
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
		for ( IAttribute a : getDomainObject().getDomainClass().iAttributes() ) {
			final IObjectAttribute attrib = getDomainObject().getAttribute( a );
			IObjectAttributeClientBinding atBinding = (IObjectAttributeClientBinding)attrib.getBinding(); 
			atBinding.addListener( _titleListener );
		}
		
		// finally set form input
		_form.setInput( getDomainObject() );
		if (!isEnlistedInTransaction()) {
			// safe enough to flush
			_form.commit( false );	
		} else {
			// don't commit the form; there is a transaction for this domain object.
		}
		
	}


	private Composite createTab(FormToolkit formToolkit, CTabFolder tabFolder, String tabName) {
		Composite composite = new Composite(tabFolder, SWT.NULL);
		formToolkit.adapt(composite, true, true);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NULL);
		tabItem.setControl(composite);
		tabItem.setText(tabName);
		return composite;
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
		if ( _titleListener != null ) {
			IDomainObject<?> object = getDomainObject();
			for ( IAttribute a : object.getDomainClass().iAttributes() ) {
				final IObjectAttribute attrib = object.getAttribute( a );
				IObjectAttributeClientBinding atBinding = (IObjectAttributeClientBinding)attrib.getBinding();
				atBinding.removeListener( _titleListener );
			}
		}
		super.dispose();
	}

	/* 
	 * Dirty if the managed form wrapped by this editor is dirty (that is, if
	 * any of the parts that it in turn contains is dirty) OR if this editor
	 * itself is dirty because the pojo is enlisted in a transaction.
	 * 
	 * <p>
	 * The latter can happen if we have just instantiated a pojo, for example.
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return isEnlistedInTransaction() || 
		       _form != null && _form.isDirty();
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

	private boolean isEnlistedInTransaction() {
		return getCurrentTransaction() != null;
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
	 * Provide access eg to CurrentTransactionView when determining what 
	 * domain object's editor has been selected.
	 */
	public DefaultEditorInput getDefaultEditorInput() {
		return (DefaultEditorInput)getEditorInput();
	}

	/**
	 * Refreshes the display if any.
	 */
	public void refresh() {
		if ( _form != null ) _form.refresh();
	}

	/* private methods */
	
	// sets the title on both window and form
	private void resetTitle() {
		String title = getDefaultEditorInput().getName();
		setPartName( title );
		_form.getForm().setText( FORM_TITLE_PREFIX + title );
	}

	
	// extract from input
	private IDomainObject<?> getDomainObject() {
		assert getEditorInput() != null;
		assert getEditorInput() instanceof DefaultEditorInput;
		return getDefaultEditorInput().getDomainObject();
	}

}
