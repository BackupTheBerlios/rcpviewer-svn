package org.essentialplatform.runtime.shared.session.event;

import java.util.EventObject;

import org.essentialplatform.runtime.shared.session.ISession;

/**
 * Event object for events that impact an {@link ISession}.
 * 
 * @author Dan Haywood
 */
public class SessionEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Package local visibility so that the only subclasses must be in this
	 * package.
	 * 
	 * @param source
	 */
	public SessionEvent(final ISession source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the session that raised the event.
	 */
	public ISession getSession() {
		return (ISession)this.getSource();
	}

}
