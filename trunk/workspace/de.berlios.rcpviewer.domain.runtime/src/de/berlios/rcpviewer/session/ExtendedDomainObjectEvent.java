package de.berlios.rcpviewer.session;

import java.util.EventObject;

import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;


/**
 * Event object for events that impact an {@link IExtendedDomainObject}.
 * 
 * @author Dan Haywood
 */
public abstract class ExtendedDomainObjectEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public ExtendedDomainObjectEvent(final IExtendedDomainObject source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the {@link IExtendedDomainObject} that raised the event.
	 */
	public IExtendedDomainObject getExtendedDomainObject() {
		return (IExtendedDomainObject)this.getSource();
	}

}
