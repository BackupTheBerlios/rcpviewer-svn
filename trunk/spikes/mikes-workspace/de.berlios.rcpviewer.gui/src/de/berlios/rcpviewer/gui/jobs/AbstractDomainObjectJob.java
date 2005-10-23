/**
 * 
 */
package de.berlios.rcpviewer.gui.jobs;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Super class for all GUI jobs associated with a specific 
 * <code>IDomainObject</code>.
 * @author Mike
 */
public abstract class AbstractDomainObjectJob extends AbstractUserJob {

	private final IDomainObject<?> _object;
	
	/**
	 * @param name
	 * @param object
	 */
	public AbstractDomainObjectJob( String name, IDomainObject<?> object ) {
		super(name);
		if ( object == null ) throw new IllegalArgumentException();
		_object = object;
	}

	/**
	 * Delegated to super, then job family linked by domain object.
	 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
	 */
	@Override
	public boolean belongsTo(Object family) {
		if ( super.belongsTo( family ) ) return true;
		return _object.equals( family );
	}
	
	/**
	 * Accessor for subclasses.
	 * @return
	 */
	protected IDomainObject<?> getDomainObject() {
		return _object;
	}
	
	


}
