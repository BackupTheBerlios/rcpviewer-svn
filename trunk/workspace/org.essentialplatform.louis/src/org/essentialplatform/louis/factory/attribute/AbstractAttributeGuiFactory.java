package org.essentialplatform.louis.factory.attribute;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.factory.GuiHints;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.FontUtil;
import org.essentialplatform.louis.util.ImageUtil;


/**
 * Base attribute gui factory.
 * @author Mike
 *
 */
public abstract class AbstractAttributeGuiFactory<T1,T2 extends Control> 
		implements IGuiFactory<IDomainClass.IAttribute>{
	
	
	/**
	 * Default description - blank String
	 * @see org.essentialplatform.gui.factory.IGuiFactory#getDescription()
	 */
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * @param model
	 * @param toolkit
	 * @param parent
	 * @param hints
	 * @return
	 */
	public IFormPart createGui(
			IDomainClass.IAttribute model, 
			FormToolkit toolkit, 
			Composite parent, 
			GuiHints hints) {
		if( model == null ) throw new IllegalArgumentException();
		if( parent == null ) throw new IllegalArgumentException();
		if( toolkit == null ) throw new IllegalArgumentException();
		if( hints == null ) throw new IllegalArgumentException();
		
		// create part
		final AbstractAttributeFormPart<T1,T2> part = createFormPart( model );

		GridLayout layout = new GridLayout( 3, false );
		parent.setLayout( layout );
		
		// label
		GridData labelData = new GridData();
		if ( hints.getColumnWidths().length == 3 
					&& hints.getColumnWidths()[0] != 0 ) {
			labelData.widthHint = hints.getColumnWidths()[0];
		}
		Label label = new Label( parent, SWT.RIGHT );
		label.setLayoutData( labelData );
		label.setBackground( parent.getBackground() );
		label.setText( model.getName() + ":" ); //$NON-NLS-1$
		label.setFont( FontUtil.getLabelFont() );
		
		// icon
		Class pojoClass = model.getEAttribute().getEType().getInstanceClass() ;
		GridData iconData = new GridData();
		if ( hints.getColumnWidths().length == 3 
					&& hints.getColumnWidths()[1] != 0 ) {
			iconData.widthHint = hints.getColumnWidths()[1];
		}
		else {
			iconData.widthHint = PART_ICON_SIZE.x;
		}
		Label icon = new Label( parent, SWT.NONE );
		icon.setImage( 
			ImageUtil.resize( LouisPlugin.getImage( pojoClass ), PART_ICON_SIZE ) ) ;
		icon.setLayoutData( iconData );
		icon.setToolTipText( LouisPlugin.getText( pojoClass ) );
		
		// main control
		final T2 control = createMainControl( parent, part, model, hints );
		if ( control.getLayoutData() == null ) {
			GridData textData = new GridData( GridData.FILL_BOTH ) ;
			if ( hints.getColumnWidths().length == 3 
						&& hints.getColumnWidths()[2] != 0 ) {
				textData.widthHint = hints.getColumnWidths()[2];
			}
			control.setLayoutData( textData );
		}
		else {
			// check on subclass's Text creation
			assert control.getLayoutData() instanceof GridData;
			assert ((GridData)control.getLayoutData()).horizontalSpan == 1;
		}
		part.setControl( control );
		
		// additional controls if editable?
		boolean editable = isEditable( model, hints );
		if ( editable ) {
			// additional controls
			Control[] additional = createAdditionalEditControls( parent, part, model, hints );
			if ( additional != null && additional.length > 0 ) {
				layout.numColumns += additional.length; 
			}
		}

		// DnD
		Transfer[] types = new Transfer[] { LouisPlugin.getTransfer( pojoClass ) };
		int operations = DND.DROP_MOVE | DND.DROP_COPY ;
		
		final DragSource source = new DragSource (control, operations);
		source.setTransfer(types);
		source.addDragListener (new DragSourceListener () {
			public void dragStart(DragSourceEvent event) {
				event.doit = ( part.getValue() != null );
			}
			public void dragSetData (DragSourceEvent event) {
				event.data = part.getValue();
			}
			public void dragFinished(DragSourceEvent event) {
				// does nowt
			}
		});

		if ( editable ) {
			DropTarget target = new DropTarget(control, operations);
			target.setTransfer(types);
			target.addDropListener (new DropTargetAdapter() {
				public void drop(DropTargetEvent event) {
					if (event.data == null) {
						event.detail = DND.DROP_NONE;
						return;
					}
					// note that all DnD ops converted to copy's 
					event.detail = DND.DROP_COPY;
					part.setValue( (T1)event.data, true );
				}
			});
		}
		
		return part;
	}

	/**
	 * Subclasses must define the <code>IFormPart</code>. 
	 * @param model
	 * @return
	 */
	protected abstract AbstractAttributeFormPart<T1,T2> createFormPart( 
			IDomainClass.IAttribute model );
	
	/**
	 * Subclasses must define:
	 * <ul>
	 * <li>style
	 * <li>layout data - must be a <code>GridData</code>  and span one column only
	 * <li>modify listeners - set form part if changes are made
	 * <li>verify listeners - add any required
	 * </ul>
	 * @param parent
	 * @param part 
	 * @param model
	 * @param hints 
	 */
	protected abstract T2 createMainControl ( 
			Composite parent, 
			AbstractAttributeFormPart<T1, T2> part, 
			IDomainClass.IAttribute model, 
			GuiHints hints );
	
	/**
	 * Allows subclasses to add additional controls if the field is editable.
	 * <br>This default implementation adds nothing.
	 * @param parent
	 * @param part
	 * @param model 
	 * @param hints 
	 * @return
	 */
	protected Control[] createAdditionalEditControls(
			Composite parent, 
			AbstractAttributeFormPart<T1,T2> part, 
			IDomainClass.IAttribute model, 
			GuiHints hints ) {
		return null;
	}
	
	/**
	 * Utiltiy method that uses model and hints to decide if editabel or not.
	 * @param model
	 * @param hints
	 * @return
	 */
	protected boolean isEditable( IDomainClass.IAttribute model, GuiHints hints ) {
		assert model != null;
		assert hints != null;
		return !hints.styleMatches( GuiHints.READ_ONLY ) 
					&& model.isChangeable();
	}
}
