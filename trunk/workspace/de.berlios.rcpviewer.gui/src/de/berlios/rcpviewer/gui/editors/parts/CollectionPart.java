/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;


import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.PageBook;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.gui.dnd.DomainObjectTransfer;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.jobs.RemoveReferenceJob;
import de.berlios.rcpviewer.gui.jobs.SearchJob;
import de.berlios.rcpviewer.gui.util.FontUtil;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.gui.util.StringUtil;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Generic form part for <code>isMany() EReference's</code> (i.e. collections)
 *  of <code>IDomainObject</code>'s.
 * @author Mike
 */
public class CollectionPart implements IReferencePart {
	
	private static final Point TOOLBAR_ICON_SIZE = new Point( 12, 12);
	
	private final CollectionGuiPage[] _pages;
	
	private IManagedForm _form = null;
	private CollectionGuiPage _currentPage;
	private boolean _isDirty = false;


	/**
	 * Gui creation done here rather than in initialize() method
	 * as this results in less state to hold onto
	 * @param ref - cannot be <code>null</code> and must represent a multiple reference
	 * @param parent - cannot be <code>null</code>
	 * @param toolkit - cannot be <code>null</code>
	 * @param columnwidths - can be <code>null</code>
	 */
	public CollectionPart(
				   final EReference ref,
				   Composite sectionParent, 
				   FormToolkit toolkit,
				   int[] columnWidths,
				   int maxLabelLength ) {
		assert ref != null;
		assert ref.isMany();
		assert sectionParent != null;
		assert toolkit != null;
		assert columnWidths != null;
		assert maxLabelLength != 0;
		
		// section		
		sectionParent.setLayout( new FillLayout() );
		Section section = toolkit.createSection( sectionParent, EXPANDABLE_STYLE );
		section.setText( StringUtil.padLeft( ref.getName(), maxLabelLength ) + ":"  ); //$NON-NLS-1$

		// section area
		Composite parent = toolkit.createComposite( section ) ;
		parent.setBackground( section.getBackground() );
		section.setClient( parent );
		
		// fetch all required types - yuk!
		Class<?> containerPojoType = ((EClassifier)ref.eContainer()).getInstanceClass();
		IRuntimeDomainClass<?> containerDomainType 
			= (IRuntimeDomainClass<?>)RuntimeDomain.instance().lookupNoRegister( 
					containerPojoType );
		Class<?> collectionPojoType = ref.getEType().getInstanceClass();
		IRuntimeDomainClass<?> collectionDomainType
			= (IRuntimeDomainClass<?>)RuntimeDomain.instance().lookupNoRegister( 
					collectionPojoType );		
		boolean allowAdd = ( containerDomainType.getAssociatorFor( ref ) != null );
		boolean allowRemove = ( containerDomainType.getDissociatorFor( ref ) != null );

		assert collectionPojoType != null;
		assert collectionDomainType != null;
		
		// pages
		parent.setLayout( new FillLayout() ) ; 
		PageBook pages = new PageBook( parent, SWT.NONE );
		pages.setBackground( parent.getBackground() );
		_pages = new CollectionGuiPage[2];
		
		// list viewer - offloaded to private method for tidiness
		_pages[0] = createTablePage( 
				allowAdd, 
			    ref,
				collectionDomainType, 
			    toolkit, 
			    pages );
		pages.showPage( _pages[0].getComposite() );
		_currentPage = _pages[0];
		
		//  master child gui - offloaded to private method for tidiness
		_pages[1] = createMasterChildPage( 
				allowAdd,
				ref,
				collectionDomainType,
				toolkit, 
				pages );
		
		// section control - 'toolbar' for section
		Composite toolbar = toolkit.createComposite( section );
		section.setDescriptionControl( toolbar );		
		configureToolbar( allowAdd, 
					   allowRemove, 
					   ref, 
					   collectionDomainType, 
					   toolkit, 
					   toolbar,
					   pages,
					   _pages );
	}

	/* IFormPart contract */
	
	/**
	 * Records form reference
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		if( form == null ) throw new IllegalArgumentException();
		_form = form;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		// does nowt
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return _isDirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		if ( onSave ) _isDirty = false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		assert _pages != null;
		for ( CollectionGuiPage page : _pages ) {
			page.getViewer().setInput( input );
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		getVisibleViewer().getControl().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		assert _pages != null;
		assert _form != null;
		for ( CollectionGuiPage page : _pages ) {
			page.getViewer().refresh();
		}
		// refresh normally occurs after reference added / removed so may
		// need to resize viewers
		_form.reflow( true );
	}
	
	/* private methods */
	
	// as it says
	private Viewer getVisibleViewer() {
		assert _currentPage != null;
		assert _currentPage.getViewer() != null;
		return _currentPage.getViewer();
	}
	
	
	/* private gui creation methods */
	
	// just to tidy constructor code
	private CollectionGuiPage createTablePage(
			boolean allowAdd,
			final EReference ref,
			final IRuntimeDomainClass<?> collectionDomainType,
			FormToolkit toolkit,
			PageBook pageBook ) {
		
		// create wrapper class
		Composite parent = toolkit.createComposite( pageBook );
		toolkit.adapt( parent );
		toolkit.paintBordersFor( parent );
        parent.setLayout( new GridLayout() );
		CollectionGuiPage page = new CollectionGuiPage( 
				parent, 
				GuiPlugin.getResourceString( 
						"CollectionPart.TableDescription") ); //$NON-NLS-1$

		// create viewer
		TableViewer viewer = createTableViewer(
				page.getComposite(),
				allowAdd,
				ref,
				collectionDomainType );
		toolkit.adapt( viewer.getControl(), true, true );
		page.setViewer( viewer );
		
		return page;
	}
	
	// just to tidy constructor code
	private TableViewer createTableViewer( 
			Composite parent,
			boolean allowAdd,
			final EReference ref,
			final IRuntimeDomainClass<?> collectionDomainType ){

		// actual instantiation and placement in parent
		TableViewer viewer = new TableViewer( 
				parent, 
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL );
		Table control = viewer.getTable();
		control.setLinesVisible( true );
		control.setHeaderVisible( true );
		control.setData( FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER );
		control.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		// columns for table..
		TableColumn column;
		column = new TableColumn( control, SWT.LEFT );
		column.setText( GuiPlugin.getResourceString( 
				"CollectionPart.FirstColumnHeader") ); //$NON-NLS-1$
		for ( EAttribute attribute  : collectionDomainType.attributes() ) {
			column = new TableColumn( control, SWT.LEFT );
			column.setText( attribute.getName() );
		}

		// label provider 
		viewer.setLabelProvider( 
			new CollectionTableLabelProvider( collectionDomainType ) );

		// content provider an inner class as quite complicated
		viewer.setContentProvider( new CollectionContentProvider( this, ref ) );

		// open listener - dbl-click opens editor for selected item
		viewer.addOpenListener( new CollectionOpenListener() );
		
		// always a drag source
		final DragSource dragSource = new DragSource( 
				control, 
				DND.DROP_MOVE | DND.DROP_COPY );
		final DomainObjectTransfer transfer = (DomainObjectTransfer)
			DndTransferFactory.getTransfer(  ref.getEType().getInstanceClass() );
		dragSource.setTransfer( new Transfer[]{ transfer } );
		dragSource.addDragListener ( 
				new CollectionDragSourceListener( viewer ) );

		// drag target if can add:
		if ( allowAdd ) {
			final DropTarget target = new DropTarget( 
					control, 
					DND.DROP_MOVE | DND.DROP_COPY );
			target.setTransfer( new Transfer[]{ transfer } );
			target.addDropListener ( 
					new CollectionDropTargetAdapter( viewer, ref, transfer ) );
		}
		
		return viewer;
	}	
	
	
	// just to tidy constructor code
	private CollectionGuiPage createMasterChildPage( 
			boolean allowAdd,
			EReference ref,
			final IRuntimeDomainClass<?> collectionDomainType,
			FormToolkit toolkit, 
			PageBook pageBook ) {
		
		// create wrapper class
		Composite parent = toolkit.createComposite( pageBook );
		parent.setBackground( pageBook.getBackground() );
        parent.setLayout( new FillLayout() );
		CollectionGuiPage page = new CollectionGuiPage( 
				parent, 
				GuiPlugin.getResourceString( 
						"CollectionPart.MasterChildDescription") ); //$NON-NLS-1$
		
		// create sash & children
		SashForm form = new SashForm( parent ,SWT.HORIZONTAL);
		form.setBackground( parent.getBackground() );
		form.setLayout( new FillLayout() );
		Composite masterComposite  = new Composite( form, SWT.NONE );
		masterComposite.setBackground( form.getBackground() );
		toolkit.paintBordersFor( masterComposite );
		Composite childComposite  = new Composite( form, SWT.NONE );
		childComposite.setBackground( form.getBackground() );
		toolkit.paintBordersFor( childComposite );
		form.setWeights( new int[] { 50, 50 } );

		// create 'master' ListViewer
		ListViewer viewer = createListViewer(
				masterComposite,
				allowAdd,
				ref,
				collectionDomainType );
		toolkit.adapt( viewer.getControl(),  true, true );
		page.setViewer( viewer );
		
		// create 'child' attribute list gui
		final IManagedForm childForm = createChildForm(
				childComposite, collectionDomainType );
		childForm.getForm().setVisible( false );
		
		// for sync'ing child 
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if ( event.getSelection().isEmpty() ) {
					childForm.getForm().setVisible( false );
				}
				else {
					childForm.setInput( 
							((StructuredSelection)event.getSelection())
								.getFirstElement() );
					if ( !childForm.getForm().isVisible() ) {
						_form.reflow( true );
						childForm.getForm().setVisible( true );
					}
				}
			}
		});

		return page;
	}
	
	// just to tidy constructor code
	private ListViewer createListViewer( 
			Composite parent,
			boolean allowAdd,
			final EReference ref,
			final IRuntimeDomainClass<?> collectionDomainType ){
		assert parent != null;
		assert ref != null;
		assert collectionDomainType != null;
		
		parent.setLayout( new GridLayout() );
		
		ListViewer viewer = new ListViewer( 
				parent, 
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL );
		List list = viewer.getList();
		list.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );
		list.setData( FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER );
		list.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		// label provider - assumes a domain class for now
		viewer.setLabelProvider( GuiPlugin.getDefault().getLabelProvider() );
		
		// content provider an inner class as quite complicated
		viewer.setContentProvider( new CollectionContentProvider( this, ref ) );

		// open listener - dbl-click opens editor for selected item
		viewer.addOpenListener( new CollectionOpenListener() );
		
		// always a drag source
		final DragSource dragSource = new DragSource( 
				list, 
				DND.DROP_MOVE | DND.DROP_COPY );
		final DomainObjectTransfer transfer = (DomainObjectTransfer)
			DndTransferFactory.getTransfer(  ref.getEType().getInstanceClass() );
		dragSource.setTransfer( new Transfer[]{ transfer } );
		dragSource.addDragListener ( 
				new CollectionDragSourceListener( viewer ) );

		// drag target if can add:
		if ( allowAdd ) {
			final DropTarget target = new DropTarget( 
					list, 
					DND.DROP_MOVE | DND.DROP_COPY );
			target.setTransfer( new Transfer[]{ transfer } );
			target.addDropListener ( 
					new CollectionDropTargetAdapter( viewer, ref, transfer ) );
		}
		
		return viewer;
	}
	
	private IManagedForm createChildForm(
			Composite parent, 
			IRuntimeDomainClass<?> childDomainType ) {
		assert parent != null;
		assert childDomainType != null;
		
		// create form
		parent.setLayout( new FillLayout() );
		ManagedForm form = new ManagedForm( parent );
		Composite body = form.getForm().getBody();
		body.setLayout( new FillLayout( SWT.VERTICAL ) );
		
		// want column width hints for IField's
		// for this calculate longest required label length for attributes
		int[] columnWidths = new int[]{ 0, 0 };
		int maxLabelLength = 0;
		for ( EAttribute a : childDomainType.attributes() ) {       
			int length = a.getName().length();
			if ( length > maxLabelLength ) maxLabelLength = length;
		}
		parent.setFont( FontUtil.getLabelFont() );
		columnWidths[0] = maxLabelLength * FontUtil.getCharWidth( 
				parent, FontUtil.CharWidthType.SAFE );
		
		// loop through all attributes - add an IFormPart for each
		for ( EAttribute attribute  : childDomainType.attributes() ) {       

			// create parent composite for IField
			Composite partComposite = form.getToolkit().createComposite( body );
//			partComposite.setLayoutData( 
//					new GridData( GridData.FILL_HORIZONTAL ) );
			form.getToolkit().paintBordersFor( partComposite );
			
			// create IField
			IFieldBuilder fieldBuilder
				= GuiPlugin.getDefault().getFieldBuilder( attribute );
			AttributePart attPart = new AttributePart(
					partComposite,
					fieldBuilder,
					attribute,
					columnWidths );
			form.addPart( attPart );
		}
		
		return form;
	}
	
	
	// just to tidy constructor code - the 'toolbar' here is a conceipt -
	// setting the description Control of the section
	private void configureToolbar(
			boolean allowAdd,
			boolean allowRemove,
			final EReference ref,
			final IRuntimeDomainClass collectionDomainType,
			FormToolkit toolkit,
			Composite toolbar,
			final PageBook pageBook,
			CollectionGuiPage[] pages) {
		assert ref != null;
		assert collectionDomainType != null;
		assert toolkit != null;
		assert toolbar != null;
		assert pageBook != null;
		assert pages != null;
		
		// layout
		int numColumns = pages.length + 1;
		if( allowAdd ) numColumns++;
		if( allowRemove ) numColumns++;
		toolbar.setLayout( new GridLayout( numColumns, false ) );
		
		// add button
		if ( allowAdd ) {
			Button add = toolkit.createButton( toolbar, "", SWT.PUSH ); //$NON-NLS-1$
			add.setImage( 
				ImageUtil.resize( 
					ImageUtil.getImage(
							GuiPlugin.getDefault(), 
							"/icons/add.png" ), //$NON-NLS-1$
					TOOLBAR_ICON_SIZE ) );		
			GridData addData = new GridData();
			addData.widthHint = TOOLBAR_ICON_SIZE.x;
			addData.heightHint = TOOLBAR_ICON_SIZE.y;
			add.setLayoutData( addData );
			add.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					Job job = new SearchJob( collectionDomainType );
					job.schedule();
				}
			});
		}
		
		// remove button
		if ( allowRemove ) {
			final Button remove = toolkit.createButton( toolbar, "", SWT.PUSH ); //$NON-NLS-1$
			remove.setImage( 
				ImageUtil.resize( 
					ImageUtil.getImage(
							GuiPlugin.getDefault(), 
							"/icons/remove.png" ), //$NON-NLS-1$
					TOOLBAR_ICON_SIZE ) );	
			GridData removeData = new GridData();
			removeData.widthHint = TOOLBAR_ICON_SIZE.x;
			removeData.heightHint = TOOLBAR_ICON_SIZE.y;
			remove.setLayoutData( removeData );
			remove.setEnabled( false );
			remove.setVisible( false );
			for ( CollectionGuiPage page : _pages ) {
				page.getViewer().addSelectionChangedListener(
					new ISelectionChangedListener(){
						public void selectionChanged(SelectionChangedEvent event){
							boolean enabled = !event.getSelection().isEmpty();
							remove.setEnabled( enabled );
							remove.setVisible( enabled );
						}
					});
			}

			remove.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					ISelection selection = getVisibleViewer().getSelection();
					assert !selection.isEmpty(); 
					// single selection viewer so
					Object selected
						= ((StructuredSelection)selection).getFirstElement();
					if ( selected instanceof IDomainObject ) {
						Job job = new RemoveReferenceJob(
								((IDomainObject<?>)getVisibleViewer().getInput()),
								ref,
								(IDomainObject<?>)selected );
						job.schedule();
					}
				}	
			});
		}
		
		// spacer
		toolkit.createLabel( toolbar, "" ).setLayoutData( //$NON-NLS-1$
				new GridData( GridData.FILL_HORIZONTAL ) );
		
		// radio options for ecah possible page
		boolean first = true;
		for ( final CollectionGuiPage page : pages ) {
			final Button radio = toolkit.createButton( 
					toolbar, 
					page.getDescription(), 
					SWT.RADIO ); 
			radio.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					pageBook.showPage( page.getComposite() );
					_currentPage = page;
				}
			} );
			if ( first ) {
				radio.setSelection( true );
				first = false;
			}
		}
	}
}
