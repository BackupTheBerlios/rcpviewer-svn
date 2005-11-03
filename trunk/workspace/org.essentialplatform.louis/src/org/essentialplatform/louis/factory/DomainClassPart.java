package org.essentialplatform.louis.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
import org.eclipse.ui.forms.IFormPart;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.SWTUtil;
import org.essentialplatform.louis.widgets.AbstractFormDisplay;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * @author Mike
 *
 */
public class DomainClassPart extends AbstractCascadingFormPart<IFormPart> {

	private final IDomainClass _dClass;
	private final boolean _editable;
	private final Map<EAttribute,Composite> _attributes;
	private final Map<EReference,Composite> _references;
	
	/* Constructors */
	
	/**
	 * Constructor requires domain class.
	 * @param class
	 */
	public DomainClassPart( IDomainClass dClass, boolean editable ) {
		assert dClass != null;
		_dClass = dClass;
		_editable = editable;
		_attributes = new LinkedHashMap<EAttribute,Composite>();
		_references = new LinkedHashMap<EReference,Composite>();
	}
	
	/* overridden IFormPart contract */
	
	@Override
	public boolean setFormInput(Object input) {
		boolean enabled = _editable && ( input != null );
		for ( Composite composite : _attributes.values() ) {
			SWTUtil.setEnabled( composite, enabled );
		}
		for ( Composite composite : _references.values() ) {
			SWTUtil.setEnabled( composite, enabled );
		}
		return super.setFormInput(input);
	}

		
	/* IConfigurableFormPart contract */
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		assert Display.getCurrent() != null;
		VisibilityDialog dialog = new VisibilityDialog();
		dialog.open();
		if ( dialog.hasChanged() ) {
			for ( IConfigurableListener listener : getListeners() ) {
				listener.configurationChanged( this );
			}
			// automatically reflow form
			if ( getForm() != null ) getForm().reflow( true );
		}	
	}
	
	/* package private methods */
	
	/**
	 * Links an attribute to the gui it is displayed on and sets initial
	 * display state.
	 * @param attribute
	 * @param area
	 */
	void addAttribute( EAttribute attribute, Composite area ) {
		assert attribute != null;
		assert area != null;
		_attributes.put( attribute, area );
		setVisible( area, isInitiallyVisible( attribute ) );
	}
	
	/**
	 * Links a reference to the gui it is displayed on and sets initial
	 * display state.
	 * @param ref
	 * @param area
	 */
	void addReference( EReference reference, Composite area ) {
		assert reference != null;
		assert area != null;
		_references.put( reference, area );
		setVisible( area, isInitiallyVisible( reference ) );
	}
	
	
	/* private methods */
	
	// as its says
	private void setVisible( Composite composite, boolean visible ) {
		assert composite != null;
		assert composite.getLayoutData() != null;
		assert composite.getLayoutData() instanceof GridData;
		if ( visible && !composite.isVisible() ) {
			((GridData)composite.getLayoutData()).heightHint
				= SWT.DEFAULT;
			composite.setVisible( true );
		}
		else if ( !visible && composite.isVisible() ) {
			((GridData)composite.getLayoutData()).heightHint = 0;
			composite.setVisible( false );
		}
	}
	
	// as it says
	private boolean isInitiallyVisible( EStructuralFeature feature ) {
		assert feature != null;
		// eventually pick up initial settings from persistence mechanism...
		// TODO
		return true;
	}
	
	/* private classes */
	
	private class VisibilityDialog extends AbstractFormDisplay {
		
		private boolean _changed = false;
		
		VisibilityDialog() {
			super( new Shell( SWT.ON_TOP | SWT.APPLICATION_MODAL )  );
			setMinSize( new Point( 100, 100 ) );
		}

		/* (non-Javadoc)
		 * @see org.essentialplatform.gui.widgets.AbstractFormDisplay#open()
		 */
		@Override
		public int open() {
			// title
			getForm().setText( LouisPlugin.getResourceString( 
				"DomainClassGuiConfigurator.VisibilityDialog.Title" ) ); //$NON-NLS-1$
			
			// main gui
			Composite body = getForm().getBody();
			body.setLayout( new GridLayout() );
			
			// add visibility check boxes
			for( EAttribute attribute : _attributes.keySet() ) {
				addCheckBox( attribute.getName(),
						     body,
						     _attributes.get( attribute ) );
			}
			for( EReference reference : _references.keySet() ) {
				addCheckBox( reference.getName(),
						     body,
						     _references.get( reference ) );
			}
			// spacer
			Label spacer = getFormToolkit().createLabel( body, "" ); //$NON-NLS-1$
			spacer.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
			// check box for recording
			Button remember = getFormToolkit().createButton(
					body,
					LouisPlugin.getResourceString(
						"DomainClassGuiConfigurator.VisibilityDialog.Remember" ),  //$NON-NLS-1$	
					SWT.CHECK );
			remember.setEnabled( false );
			
			// buttons
			addOKButton( ADD_DEFAULT_BEHAVIOUR );
			
			// super's functionality to open
			return super.open();
		}	
		
		private void addCheckBox( 
				String label, 
				Composite parent, 
				final Composite underControl ) {
			assert label != null;
			assert parent != null;
			assert underControl != null;
			final Button button = getFormToolkit().createButton(
					parent,
					label,
					SWT.CHECK );
			button.setLayoutData( new GridData() );
			button.setSelection( underControl.isVisible() );
			button.addSelectionListener( new DefaultSelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					setVisible( underControl, button.getSelection() );
					_changed = true;
				}
			});
		}
		
		boolean hasChanged() {
			return _changed;
		}
	}	
}
