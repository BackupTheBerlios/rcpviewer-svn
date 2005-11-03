package org.essentialplatform.runtime.session;


/**
 * Event object for events that relate to an {@link IDomainObject} changing its 
 * state with respect to an {@link ISession}.
 * 
 * @author Dan Haywood
 */
public final class SessionObjectEvent<T> extends SessionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 * @param source
	 */
	public SessionObjectEvent(final ISession source, final IDomainObject<T> domainObject) {
		super(source);
	}
	
	public IDomainObject<T> getDomainObject() {
		return (IDomainObject<T>)this.getSource();
	}

}
