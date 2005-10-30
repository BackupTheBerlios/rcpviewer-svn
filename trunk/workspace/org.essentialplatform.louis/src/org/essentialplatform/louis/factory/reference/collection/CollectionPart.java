package org.essentialplatform.louis.factory.reference.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.configure.IConfigurable;
import org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener;
import org.essentialplatform.louis.util.StringUtil;

import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.ExtendedDomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IDomainObject.IObjectCollectionReference;

class CollectionPart extends AbstractFormPart implements IConfigurable {

	private final EReference _model;
	private final IDomainObjectReferenceListener _domainListener;
	private final ListenerForwarder _partListener;
	private final List<ICollectionChildPart> _children;
	
	private Label _control;
	private IDomainObject<?> _container = null;
	private List<IDomainObject<?>> _displayed = null;
	private ICollectionChildPart _activePart;
	
	/**
	 * Requires the reference
	 * @param ref
	 * @param pages
	 */
	CollectionPart( EReference ref ) {
		super();
		assert ref != null;
		_model = ref;
		_domainListener = new IDomainObjectReferenceListener(){
			public void collectionAddedTo(DomainObjectReferenceEvent event) {
				refresh();
			}
			public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
				refresh();
			}
			public void referenceChanged(DomainObjectReferenceEvent event) {
				// does nowt
			}
			public void referencePrerequisitesChanged(ExtendedDomainObjectReferenceEvent event) {
				// does nowt
			}
		};
		_children = new ArrayList<ICollectionChildPart>();
		_partListener = new ListenerForwarder();
	}
	
	/* IFormPart contract */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	public void initialize(IManagedForm form) {
		super.initialize(form);
		for ( ICollectionChildPart child : _children ) {
			child.initialize( form );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	@Override
	public boolean setFormInput(Object input) {
		if ( _container != null ) {
			_container.getCollectionReference( _model ).removeListener( _domainListener );
			_container = null;
		}
		if ( input instanceof IDomainObject<?> ) {
			_container = (IDomainObject<?>)input;
			_container.getCollectionReference( _model ).addListener( _domainListener );
		}
		refresh();
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		if ( _activePart != null ) _activePart.setFocus();
	}
	
	/**
	 * Does not delegate commit - does itself.
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	@Override
	public void commit(boolean onSave) {
		if ( onSave ) {
			assert _displayed != null;
			
			// compare displayed list with model
			IObjectCollectionReference ref = _container.getCollectionReference( _model );
			Collection<IDomainObject<?>> model = getCollectionDomainObjects();
			
			// in model, not displayed - remove
			for ( IDomainObject<?> element : model ) {
				if ( !_displayed.contains( element ) ) {
					ref.removeFromCollection( element );
				}
			}
			
			// in display, not in model - add
			for ( IDomainObject<?> element : _displayed ) {
				if ( !model.contains( element ) ) {
					ref.addToCollection( element );
				}
			}
		}
		super.commit( onSave );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	@Override
	public void refresh() {
		_displayed = getCollectionDomainObjects();
		setDisplay( _displayed );
		super.refresh();
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	@Override
	public void dispose() {
		if ( _container != null ) {
			_container.getCollectionReference( _model ).removeListener( _domainListener );
		}
		super.dispose();
	}
	
	
	/* IConfigurale contract */
	
	/**
	 * Dummy method - does nowt
	 * @see org.essentialplatform.louis.configure.IConfigurable#addConfigurableListener(org.essentialplatform.louis.configure.IConfigurable.IConfigurableListener)
	 */
	public boolean addConfigurableListener(IConfigurableListener listener) {
		return false;
	}
	
	/**
	 * Dummy method - does nowt
	 * @see org.essentialplatform.louis.configure.IConfigurable#removeConfigurableListener(org.essentialplatform.louis.configure.IConfigurable.IConfigurableListener)
	 */
	public boolean removeConfigurableListener(IConfigurableListener listener) {
		return false;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if ( _activePart != null ) {
			_activePart.run();
			getManagedForm().reflow( true );
		}
	}
	
	/* package private methods */
	
	/**
	 * Sets the summary control for this part.
	 * @param control
	 */
	void setControl( Label control ) {
		if ( control == null ) throw new IllegalArgumentException();
		_control = control;
	}
	
	/**
	 * @param part
	 * @return
	 */
	boolean addChildPart(ICollectionChildPart part) {
		boolean ok = _children.add( part );
		if ( ok ) {
			part.setParent( this );
			part.addDisplayListener( _partListener );
		}
		return ok;
	}
	
	/**
	 * Adds a display value listener
	 * @param listener
	 */
	void addDisplayListener( IReferencePartDisplayListener listener ) {
		_partListener.add( listener );
	}
	
	/**
	 * Adds a domain object to the collection.
	 */
	void addToCollection( IDomainObject<?> dObj ) {
		assert dObj != null;
		assert _displayed != null;
		_displayed.add( dObj );
		setDisplay( _displayed );
	}
	
	/**
	 * Removes a domain object from the collection.
	 */
	void removeFromCollection( IDomainObject<?> dObj ) {
		assert dObj != null;
		assert _displayed != null;
		assert _displayed.contains( dObj );
		_displayed.remove( dObj );
		setDisplay( _displayed );
	}
	
	/**
	 * Sets the active part.
	 * @param part
	 */
	void setActivePart( ICollectionChildPart part ) {
		assert part != null;
		assert ( _children.contains( part ) );
		_activePart = part;
		if ( getManagedForm() != null ) getManagedForm().reflow( true );
	}
	
	/* private methods */
	
	// returns the domain objects for all the collection's pojo's
	private List<IDomainObject<?>> getCollectionDomainObjects() {
		List<IDomainObject<?>> elements = new ArrayList<IDomainObject<?>>();
		if ( _container == null ) return elements;
		elements.addAll(_container.getCollectionReference( _model ).getCollection());
		return elements;
	}
	
	// sets display on all children
	private void setDisplay(  List<IDomainObject<?>> display ) {
		for ( ICollectionChildPart child :_children ) {
			child.display( display );
		}
		if ( _control != null ) {
			int numDisplayed = display.size();
			String s;
			if ( numDisplayed ==1 ) {
				s = LouisPlugin.getResourceString( "CollectionPart.SingularMsg" ); //$NON-NLS-1$
			}
			else {
				s = StringUtil.printf(
						LouisPlugin.getResourceString( "CollectionPart.PluralMsg" ), //$NON-NLS-1$	
						String.valueOf( numDisplayed ) );
			}
			_control.setText( s );
		}
		getManagedForm().reflow( true );
		markDirty();
	}
	
	/* private classes */
	
	private class ListenerForwarder implements IReferencePartDisplayListener {
		private List<IReferencePartDisplayListener> _listeners = null;
		
		// adds a listener
		void add( IReferencePartDisplayListener listener ) {
			assert listener != null;
			if ( _listeners == null ) {
				_listeners = new ArrayList<IReferencePartDisplayListener>();
			}
			_listeners.add( listener );
		}
		
		/* (non-Javadoc)
		 * @see org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener#displayValueChanged(de.berlios.rcpviewer.session.IDomainObject)
		 */
		public void displayValueChanged(IDomainObject<?> value) {
			if ( _listeners != null ) {
				for ( IReferencePartDisplayListener listener : _listeners ) {
					listener.displayValueChanged( value );
				}
			}
		}
	}
}
