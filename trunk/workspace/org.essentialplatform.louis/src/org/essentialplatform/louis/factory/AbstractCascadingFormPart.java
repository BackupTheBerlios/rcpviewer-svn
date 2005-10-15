/**
 * 
 */
package org.essentialplatform.louis.factory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.essentialplatform.louis.configure.ConfigurableAdapter;

/**
 * <br>Extends <code>IFormPart</code> by adding a heirarchy to the contract's
 * methods so that a call to any method is cascaded to all child parts.
 * <br>As such parts are inevitable <code>IConfigurable</code>, 
 * offers this functionality as
 * well.
 * @author Mike
 */
public abstract class AbstractCascadingFormPart<T extends IFormPart> 
		extends ConfigurableAdapter implements IFormPart {
	
	private static final IConfigurableListener[] EMPTY_ARRAY 
		= new IConfigurableListener[0];

	private final List<T> _parts;
	
	private IManagedForm _mForm = null;
	
	/* constructor */
	
	protected AbstractCascadingFormPart() {
		_parts = new ArrayList<T>();
	}
	
	
	/* IFormPart contract */

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		for( IFormPart part : _parts ) {
			if ( part.isDirty() ) part.commit( onSave );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		for( IFormPart part : _parts ) {
			part.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		_mForm = form;
		for( IFormPart part : _parts ) {
			part.initialize( form );
		}
	}

	/**
	 * Returns <code>true</code> if any child is dirty
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		for( IFormPart part : _parts ) {
			if ( part.isDirty() ) return true;
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if any child is stale
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		for( IFormPart part : _parts ) {
			if ( part.isStale() ) return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		for( IFormPart part : _parts ) {
			part.refresh();
		}
	}

	/**
	 * Sets focus to first child.
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		IFormPart part = _parts.get(0);
		if ( part != null ) part.setFocus();
	}

	/**
	 * Returns <code>true</code> if any child has returned true;
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		boolean overall = false;
		for( IFormPart part : _parts ) {
			if ( part.setFormInput( input ) && !overall ) overall = true;
		}
		return overall;
	}
	
	/* additional methods */
	
	/**
	 * Adds a child part.
	 * @param part
	 * @return
	 */
	public boolean addChildPart( T part ) {
		return _parts.add( part );
	}
	
	/**
	 * Removes a child part.
	 * @param part
	 * @return
	 */
	public boolean removeChildPart( T part ) {
		return _parts.remove( part );
	}
	
	
	/* protected accessors */
	
	/**
	 * @return Returns the parts.
	 */
	protected List<T> getChildParts() {
		return _parts;
	}
	
	/**
	 * Could be <code>null</code> if <code>initialize</code> has not yet been 
	 * called.
	 * @return
	 */
	protected IManagedForm getForm(){
		return _mForm;
	}
}
