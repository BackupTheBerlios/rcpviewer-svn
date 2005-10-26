package org.essentialplatform.louis.factory.reference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Section;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.IConfigurable;
import org.essentialplatform.louis.util.SWTUtil;

import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.IPojo;

class ReferencePart extends AbstractFormPart implements IConfigurable {

	private final EReference _model;
	private final IDomainObjectReferenceListener _listener;
	private final IFormPart _detailsPart;
	
	private Text _control;
	private IDomainObject<?> _container = null;
	private IDomainObject<?> _refValue = null;
	private List<IReferencePartDisplayListener> _listeners = null;
	
	/**
	 * Requires the reference
	 * @param ref
	 * @param pages
	 */
	ReferencePart( EReference ref, IFormPart detailsPart ) {
		super();
		assert ref != null;
		assert detailsPart != null;
		_model = ref;
		_detailsPart = detailsPart;
		// add listener that updates display whenever domain object updated
		// outside of this field
		_listener = new IDomainObjectReferenceListener(){
			public void collectionAddedTo(DomainObjectReferenceEvent event) {
				// does nowt
			}
			public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
				// does nowt
			}
			public void referenceChanged(DomainObjectReferenceEvent event) {
				refresh();
			}
		};
	}
	
	/* IFormPart contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		super.initialize( form );
		_detailsPart.initialize( form );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		// remove listening from old object if any
		if ( _container != null ) {
			_container.getOneToOneReference( _model ).removeListener( _listener );	
		}
		assert input instanceof IDomainObject<?>;
		_container = (IDomainObject)input;
		// add listening to new object
		if ( _container != null ) {
			_container.getOneToOneReference( _model ).addListener( _listener );
		}
		refresh();
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		IDomainObject<?> value = null;
		if ( _container != null ) {
			IPojo referencedPojo = _container.getOneToOneReference( _model ).get();
			if (referencedPojo != null) {
				value = referencedPojo.getDomainObject();
			}
		}
		setValue( value );
		super.refresh();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		if ( onSave ) {
			_container.getOneToOneReference( _model ).set( getValue() );
		}
		super.commit(onSave);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		if ( _control != null ) _control.setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		_detailsPart.dispose();
	}
	
	/* IConfigurable contract - delegate to details */
	
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.factory.IConfigurable#run()
	 */
	public void run() {
		if ( _detailsPart instanceof IConfigurable ) {
			((IConfigurable)_detailsPart).run();
		}
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.factory.IConfigurable#addListener(de.berlios.rcpviewer.gui.factory.IConfigurable.IConfigurableListener)
	 */
	public boolean addConfigurableListener(IConfigurableListener listener) {
		if ( _detailsPart instanceof IConfigurable ) {
			return ((IConfigurable)_detailsPart).addConfigurableListener( listener );
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.factory.IConfigurable#removeListener(de.berlios.rcpviewer.gui.factory.IConfigurable.IConfigurableListener)
	 */
	public boolean removeConfigurableListener(IConfigurableListener listener) {
		if ( _detailsPart instanceof IConfigurable ) {
			return ((IConfigurable)_detailsPart).removeConfigurableListener( listener );
		}
		return false;
	}
	
	
	/* package private methods */
	
	/**
	 * Sets the primary control for this part.
	 * @param control
	 */
	void setControl( Text control ) {
		if ( control == null ) throw new IllegalArgumentException();
		_control = control;
	}
	
	/**
	 * Accessor to value.
	 */
	IDomainObject<?> getValue() {
		return _refValue;
	}
	
	/**
	 * Sets the value.
	 * @param value
	 */
	void setValue( IDomainObject<?> value  ) {
		_refValue = value;
		if ( _control != null ) {
			if ( _refValue == null ) {
				_control.setText( "" ); //$NON-NLS-1$
			}
			else {
				boolean reflow = _control.getText().length() == 0;
				_control.setText( LouisPlugin.getText( _refValue ) );
				if ( reflow ) {
					Section s = SWTUtil.getParent( _control, Section.class );
					if ( s != null ) s.layout();
				}
			}
		}
		_detailsPart.setFormInput( _refValue );
		markDirty();
		if ( _listeners != null ) {
			for ( IReferencePartDisplayListener listener : _listeners ) {
				listener.displayValueChanged( value );
			}
		}
	}
	
	/**
	 * Adds a display value listener
	 * @param listener
	 */
	void addDisplayListener( IReferencePartDisplayListener listener ) {
		assert listener != null;
		if ( _listeners == null ) {
			_listeners = new ArrayList<IReferencePartDisplayListener>();
		}
		_listeners.add( listener );
	}

}
