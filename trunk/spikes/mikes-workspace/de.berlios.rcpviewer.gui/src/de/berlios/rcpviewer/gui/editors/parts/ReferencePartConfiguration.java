/**
 * 
 */
package de.berlios.rcpviewer.gui.editors.parts;

import static de.berlios.rcpviewer.gui.util.EmfUtil.SortType.ANNOTATION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.util.EmfUtil;
import de.berlios.rcpviewer.gui.widgets.AbstractFormDisplay;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;

/**
 * Gui configuration of collection part
 * @author Mike
 */
class ReferencePartConfiguration {
	
	/**
	 * Constant for gui widget to sue to store data relevent to configuration.
	 */
	static final String GUI_CONFIG_DATA
		= ReferencePartConfiguration.class.getSimpleName();

	private final List<EAttribute> _attributes;
	private final boolean[] _visible;
	
	/**
	 * Constructor requires collection type.
	 * @param referenceDomainType
	 */
	ReferencePartConfiguration( IRuntimeDomainClass<?> referenceDomainType ) {
		assert referenceDomainType != null;
		_attributes = EmfUtil.sort( referenceDomainType.attributes(), ANNOTATION );
		_visible = new boolean[ _attributes.size() ];
		// eventually pick up initial settings from persistence mechanism...
		Arrays.fill( _visible, true );
	}
	
	/**
	 * All attributes.
	 * @return
	 */
	List<EAttribute> getAttributes() {
		return Collections.unmodifiableList( _attributes );
	}
	
	/**
	 * Whether the passed attribute should be visible on the gui.
	 * <br>The attribute must be one from <code>getAttributes()</code>.
	 * @param attribute
	 * @return
	 */
	boolean isVisible( EAttribute attribute ) {
		assert attribute != null;
		assert _attributes.contains( attribute );
		return _visible[ _attributes.indexOf( attribute ) ];
	}
	
	/**
	 * Allow the user to modify the configuration.
	 * <br>Must run in UI thread as opens dialog.
	 * <br>Returns whether anything changed.
	 * @return boolean
	 */
	boolean modify() {
		assert Display.getCurrent() != null;
		AttributeVisibilityDialog dialog = new AttributeVisibilityDialog();
		return ( SWT.OK == dialog.open() && dialog.isChanged() );
	}
	
	
	private class AttributeVisibilityDialog extends AbstractFormDisplay {
		
		private boolean _changed = false;

		AttributeVisibilityDialog() {
			super( new Shell( SWT.ON_TOP | SWT.APPLICATION_MODAL )  );
			setMinSize( new Point( 100, 100 ) );
		}

		/* (non-Javadoc)
		 * @see de.berlios.rcpviewer.gui.widgets.AbstractFormDisplay#open()
		 */
		@Override
		public int open() {
			// title
			getForm().setText( GuiPlugin.getResourceString( 
				"ReferencePartConfiguration.AttributeVisibilityDialog.Title" ) ); //$NON-NLS-1$
			
			// main gui
			Composite body = getForm().getBody();
			body.setLayout( new GridLayout() );
			
			// add visibility check boxes
			for ( int i=0 ; i < _visible.length ; i++ ) {
				final Button button = getFormToolkit().createButton(
						body,
						_attributes.get( i ).getName(),
						SWT.CHECK );
				button.setLayoutData( new GridData() );
				button.setSelection( _visible[i] );
				final int index = i;
				button.addSelectionListener( new DefaultSelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						_visible[index] = button.getSelection();
						_changed = true;
					}
				});
			}
			// spacer
			Label spacer = getFormToolkit().createLabel( body, "" ); //$NON-NLS-1$
			spacer.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
			// check box for recording
			Button remember = getFormToolkit().createButton(
					body,
					GuiPlugin.getResourceString(
						"ReferencePartConfiguration.AttributeVisibilityDialog.Remember" ),  //$NON-NLS-1$	
					SWT.CHECK );
			remember.setEnabled( false );
			
			// buttons
			addOKButton( ADD_DEFAULT_BEHAVIOUR );
			addCancelButton( ADD_DEFAULT_BEHAVIOUR );
			
			// super's functionality to open
			return super.open();
		}
		
		/**
		 * User changed something
		 * @return
		 */
		boolean isChanged(){
			return _changed;
		}
		
		
	}


}
