package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.metamodel.IDomainObject;

/**
 * Event object for events that relate to an {@link IDomainObject} changing its 
 * state with respect to an {@link ISession}.
 * 
 * @author Dan Haywood
 */
public final class SessionObjectEvent extends SessionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 * @param source
	 */
	public SessionObjectEvent(final ISession source, final IDomainObject domainObject) {
		super(source);
		this.domainObject = domainObject;
	}
	

	private IDomainObject domainObject;
	public IDomainObject getDomainObject() {
		return (IDomainObject)this.getSource();
	}

}
