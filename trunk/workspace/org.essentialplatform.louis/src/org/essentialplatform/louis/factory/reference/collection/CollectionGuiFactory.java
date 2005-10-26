/**
 * 
 */
package org.essentialplatform.louis.factory.reference.collection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.PageBook;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.ConfigureWidgetFactory;
import org.essentialplatform.louis.factory.DomainClassGuiFactory;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener;
import org.essentialplatform.louis.jobs.SearchJob;
import org.essentialplatform.louis.util.EmfUtil;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.util.StringUtil;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Generic form part for <code>isMany() EReference's</code> (i.e. collections)
 *  of <code>IDomainObject</code>'s.
 *  <br>This class basically handles the gui creation, dynamic behaviour is 
 *  delegated to <code>CollectionPartController</code>
 * @author Mike
 */
public class CollectionGuiFactory implements IGuiFactory<EReference> {
	
	/**
	 * Returns <code>true</code> if a multiple reference and parent is
	 * a <code>DomainClassGuiFactory</code>
	 * @param model
	 * @param context
	 * @return
	 */
	public boolean isApplicable(Object model, IGuiFactory<?> parent) {
		if( model == null ) throw new IllegalArgumentException();
		if( parent == null ) return false;
		if ( !( parent instanceof DomainClassGuiFactory ) ) return false;
		if ( model instanceof EReference ) {
			return ((EReference)model).isMany();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.factory.IGuiFactory#getDescription()
	 */
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}
		
	/**
	 * @param model
	 * @param mForm
	 * @param parent
	 * @param hints
	 * @return
	 */
	public CollectionPart createGui(
			final EReference model, 
			FormToolkit toolkit, 
			Composite parent, 
			GuiHints hints) {
		if( model == null ) throw new IllegalArgumentException();
		if( toolkit == null ) throw new IllegalArgumentException();
		if( parent == null ) throw new IllegalArgumentException();
		if( hints == null ) throw new IllegalArgumentException();
		
		// create controller for dynamic behaviour & return 
		CollectionPart part = new CollectionPart( model );
		
		// section		
		parent.setLayout( new FillLayout() );
		Section section = toolkit.createSection( parent, SECTION_STYLE );
		section.setText(
			StringUtil.padLeft( 
					model.getName(), hints.getMaxLabelLength() ) + ":"  ); //$NON-NLS-1$
		
		// section header
		Composite sectionHeader = toolkit.createComposite( section );
		sectionHeader.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		toolkit.paintBordersFor( sectionHeader );
		section.setTextClient( sectionHeader );
		Label label = buildSectionHeaderContents( model, toolkit, sectionHeader );
		part.setControl( label );

		// section area
		Composite sectionArea = toolkit.createComposite( section ) ;
		sectionArea.setBackground( section.getBackground() );
		section.setClient( sectionArea );
		
		// page book
		sectionArea.setLayout( new FillLayout() ) ; 
		PageBook pageBook = new PageBook( sectionArea, SWT.NONE );
		pageBook.setBackground( sectionArea.getBackground() );
		
		// find all sub-factories and create page for each
		List<IGuiFactory<?>> factories = LouisPlugin.getDefault().getGuiFactories(
				model, this );
		assert !factories.isEmpty();
		List<CollectionGuiPage> pages = new ArrayList<CollectionGuiPage>();
		for( IGuiFactory factory : factories ) {			
			// create page composite 
			Composite composite = toolkit.createComposite( pageBook );
			toolkit.paintBordersFor( composite );
			
			// create child gui & part
			IFormPart childPart = factory.createGui(
					model,
					toolkit,
					composite,
					new GuiHints( GuiHints.INCLUDE_ATTRIBUTES ) ) ;
			assert childPart instanceof ICollectionChildPart;
			
			// wrapper object
			CollectionGuiPage page = new CollectionGuiPage( 
					composite, 
					factory.getDescription(), 
					(ICollectionChildPart)childPart );
			
			// record	
			part.addChildPart( (ICollectionChildPart)childPart );
			pages.add( page );
		}
		
		// ensure first page visible
		pageBook.showPage( pages.get(0).getComposite() );
		
		// create 'toolbar' for section
		Composite toolbar = toolkit.createComposite( section );	
		section.setDescriptionControl( toolbar );		
		createToolbar( model, 
				       toolkit,
					   part,
					   toolbar,
					   pageBook,
					   pages );
		
		return part;
	}

	/* private gui creation methods */
	
	// just to tidy code
	private Label buildSectionHeaderContents( 
			EReference model,
			FormToolkit toolkit,
			Composite parent ){
		assert model != null;
		assert toolkit != null;
		assert parent != null;
		
		// layout
		GridLayout layout = new GridLayout( 2, false );
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		parent.setLayout(  layout );
		
		// type icon
		Label icon = toolkit.createLabel( parent, "" ); //$NON-NLS-1$
		icon.setImage( 
				ImageUtil.resize(
					ImageUtil.getImage( EmfUtil.getCollectionDomainType( model ) ),
				PART_ICON_SIZE ) );	
		icon.setLayoutData( new GridData() );

		// main field
		Label label = toolkit.createLabel( parent, "" ); //$NON-NLS-1$
		GridData labelData = new GridData( GridData.FILL_HORIZONTAL );
		labelData.heightHint = PART_ICON_SIZE.y;
		label.setLayoutData( labelData );
		
		return label;
	}
	
	
	// just to tidy code - the 'toolbar' here is a conceipt -
	// actually we are setting the description Control of the section
	private void createToolbar(
			final EReference model,
			FormToolkit toolkit, 
			final CollectionPart part,
			Composite toolbar,
			final PageBook pageBook,
			List<CollectionGuiPage> pages) {
		assert model != null;
		assert toolkit != null;
		assert part != null;
		assert toolbar != null;
		assert pageBook != null;
		assert pages != null;
		
		// count columns for layout
		int numColumns = 0;
		
		// model data
		final IDomainClass dClass = EmfUtil.getCollectionDomainType( model );
		
		// add button
		if ( EmfUtil.canAddTo( model ) ) {
			numColumns++;
			Button add = toolkit.createButton( toolbar, "", SWT.PUSH ); //$NON-NLS-1$
			add.setImage( 
				ImageUtil.resize( 
					ImageUtil.getImage(
							LouisPlugin.getDefault(), 
							"/icons/add.png" ), //$NON-NLS-1$
					PART_ICON_SIZE ) );		
			GridData addData = new GridData();
			addData.widthHint = PART_ICON_SIZE.x;
			addData.heightHint = PART_ICON_SIZE.y;
			add.setLayoutData( addData );
			add.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					Job job = new SearchJob( dClass );
					job.schedule();
					// whatever the result, pass to part.addToCollection()...
				}
			});
		}
		
		// remove button
		if ( EmfUtil.canRemoveFrom( model ) ) {
			numColumns++;
			final Button remove = toolkit.createButton( toolbar, "", SWT.PUSH ); //$NON-NLS-1$
			remove.setImage( 
				ImageUtil.resize( 
					ImageUtil.getImage(
							LouisPlugin.getDefault(), 
							"/icons/remove.png" ), //$NON-NLS-1$
					PART_ICON_SIZE ) );	
			GridData removeData = new GridData();
			removeData.widthHint = PART_ICON_SIZE.x;
			removeData.heightHint = PART_ICON_SIZE.y;
			remove.setLayoutData( removeData );
			remove.setEnabled( false );
			remove.setVisible( false );
			new RemoveListener( part, remove );			
		}
		
		// spacer
		numColumns++;
		toolkit.createLabel( toolbar, "" ).setLayoutData( //$NON-NLS-1$
				new GridData( GridData.FILL_HORIZONTAL ) );
		
		// radio options for each possible page
		boolean first = true;
		for ( final CollectionGuiPage page : pages ) {
			numColumns++;
			final Button radio = toolkit.createButton( 
					toolbar, 
					page.getDescription(), 
					SWT.RADIO ); 
			radio.addSelectionListener( new DefaultSelectionAdapter(){
				public final void widgetSelected(SelectionEvent event) {
					pageBook.showPage( page.getComposite() );
					part.setActivePart( page.getPart() );
				}
			} );
			if ( first ) {
				radio.setSelection( true );
				first = false;
			}
		}
		
		// configure gui button
		numColumns++;
		ConfigureWidgetFactory.createButton( toolbar, toolkit, part );
				
		
		// finally set layout
		toolbar.setLayout( new GridLayout( numColumns, false ) );
	}
	
	
	/* private classes */
	
	// container for one alternate presentation of a collection reference's data
	private class CollectionGuiPage {
		
		private final Composite _parent;
		private final String _description;
		private final ICollectionChildPart _part;
		
		CollectionGuiPage( 
				Composite parent, 
				String description, 
				ICollectionChildPart part ) {
			assert parent != null;
			assert description != null;
			assert part != null;
			_parent = parent;
			_description = description;
			_part = part;
		}

		/**
		 * @return Returns the part.
		 */
		ICollectionChildPart getPart() {
			return _part;
		}
		
		Composite getComposite() {
			return _parent;
		}
		
		String getDescription(){
			return _description;
		}
	}
	
	
	// controls remove button
	private class RemoveListener extends DefaultSelectionAdapter 
	                             implements IReferencePartDisplayListener {
		
		private final CollectionPart _part;
		private final Button _button;
		
		private IDomainObject<?> _currentSelection = null;
		
		RemoveListener( CollectionPart part, Button button ) {
			assert part != null;
			assert button != null;
			_part = part;
			_part.addDisplayListener( this );
			_button = button;
			_button.addSelectionListener( this );
		}
		
		/* (non-Javadoc)
		 * @see org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener#displayValueChanged(de.berlios.rcpviewer.session.IDomainObject)
		 */
		public void displayValueChanged(IDomainObject<?> value) {
			_currentSelection = value;
			_button.setEnabled( value != null );
			_button.setVisible( value != null );
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			if ( _currentSelection != null ) {
				_part.removeFromCollection( _currentSelection );
			}
		}
	}
	
}
