package de.berlios.rcpviewer.gui.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.part.EditorPart;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.jobs.JobAction;
import de.berlios.rcpviewer.gui.jobs.RefreshDomainObjectJob;
import de.berlios.rcpviewer.gui.jobs.ReportJob;
import de.berlios.rcpviewer.gui.util.FontUtil;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.gui.views.ops.IOpsViewPage;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectAttributeListener;

/**
 * Core editor for GUI layer.
 * @author Mike
 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder
 * @see de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder.IField
 */
public final class DefaultEditor extends EditorPart {
	
	public static final String ID = "de.berlios.rcpviewer.rcp.objectEditor"; //$NON-NLS-1$
	
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
				_form.getForm(), FontUtil.CharWidthType.AVERAGE );
		Image image = ImageUtil.resize(
				((DefaultEditorInput)getEditorInput() ).getImage(),
				new Point( width, width ) );
		_form.getForm().setBackgroundImage( image  );
		
		// create form gui
		Composite body = _form.getForm().getBody();
		body.setLayout( new GridLayout() );
		
		// want column width hints for IField's
		// for this calculate longest required label length for attributes
		int[] columnWidths = new int[]{ 0, 0 };
		IDomainObject<?> object = getDomainObject();
		IDomainClass<?> clazz = object.getDomainClass(); 
		int maxLabelLength = 0;
		for ( EAttribute a : clazz.attributes() ) {       
			int length = a.getName().length();
			if ( length > maxLabelLength ) maxLabelLength = length;
		}
		for ( EReference r : clazz.references() ) {
			int length = r.getName().length();
			if ( length > maxLabelLength ) maxLabelLength = length;
		}
		parent.setFont( FontUtil.getLabelFont() );
		columnWidths[0] = maxLabelLength * FontUtil.getCharWidth( 
				parent, FontUtil.CharWidthType.SAFE );
		// more faff - want to align labels even if references have toggle 
		// icons - could do much faff calculating size - make do with hardcode 
		// value for now
		if ( !clazz.references().isEmpty() ) {
			columnWidths[0] = columnWidths[0] + 4;
		}
		
		// loop through all attributes - add an IFormPart for each
		for ( EAttribute attribute  : clazz.attributes() ) {       

			// create parent composite for IField
			Composite partComposite = _form.getToolkit().createComposite( body );
			partComposite.setLayoutData( 
					new GridData( GridData.FILL_HORIZONTAL ) );
			_form.getToolkit().paintBordersFor( partComposite );
			
			// create IField
			IFieldBuilder fieldBuilder
				= GuiPlugin.getDefault().getFieldBuilder( attribute );
			AttributePart attPart = new AttributePart(
					partComposite,
					fieldBuilder,
					object,
					attribute,
					columnWidths );
			_form.addPart( attPart );
		}
		
		// loop through all references - add an IFormPart for each
		for ( EReference ref : clazz.references() ) {       
	
			// create parent composite
			Composite partComposite = _form.getToolkit().createComposite( body );
			partComposite.setLayoutData( 
					new GridData( GridData.FILL_HORIZONTAL ) );
			_form.getToolkit().paintBordersFor( partComposite );
			
			// create form part - currently one of three types...
			IFormPart refPart;
			
			// check we can currently deal with this - i.e. only if a
			// reference to one or more instances of a domain class
			final Class<?> refPojoType = ref.getEType().getInstanceClass();
			if( RuntimeDomain.instance().lookupNoRegister( refPojoType ) == null ) {
				refPart = new InvalidReferencePart( 
						ref,
						GuiPlugin.getResourceString( "DefaultEditor.InvalidRefMsg" ), //$NON-NLS-1$
						partComposite, 
						columnWidths );
			}
			else {
				if ( ref.isMany() ) {
					refPart = new CollectionPart( 
							object,
							ref,
							partComposite,
							_form.getToolkit(),
							columnWidths,
							maxLabelLength );
				}
				else {
					refPart = new ReferencePart( 
							object,
							ref,
							partComposite,
							_form.getToolkit(),
							columnWidths,
							maxLabelLength );
				}
			}
			_form.addPart( refPart );
		}
			
		_form.setInput( object );
		
		// listen in on attribute changes in case they affect the name
		_nameListener = new IDomainObjectAttributeListener(){
			public void attributeChanged(DomainObjectAttributeEvent event){
				// check if name generated by input is different to current name
				String newName = getEditorInput().getName();
				if( !getPartName().equals( newName  ) ) {
					resetTitle();
				}
			}
		};
		for ( EAttribute a : clazz.attributes() ) {
			object.getAttribute( a )
			      .addDomainObjectAttributeListener( _nameListener );
		}
		
		
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
			for ( EAttribute a : object.getDomainClass().attributes() ) {
				object.getAttribute( a )
				      .removeDomainObjectAttributeListener( _nameListener );
			}
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
		ReportJob report = new ReportJob(
				GuiPlugin.getResourceString( "DefaultEditor.Save"), //$NON-NLS-1$
				ReportJob.INFO,
				getPartName() );
		report.schedule();
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
