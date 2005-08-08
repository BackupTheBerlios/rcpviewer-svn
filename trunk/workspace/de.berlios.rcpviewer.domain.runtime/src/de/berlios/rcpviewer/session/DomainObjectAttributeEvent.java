package de.berlios.rcpviewer.session;

import java.util.EventObject;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * <p>
 * Parameterized by the type of the EAttribute that has changed.
 * 
 * @author Dan Haywood
 */
public final class DomainObjectAttributeEvent<T> extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public DomainObjectAttributeEvent(
			final IDomainObject.IAttribute source, 
			final T newValue) {
		super(source);
		this.newValue = newValue;
	}
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the extended attribute that raised this event.
	 */
	public IDomainObject.IAttribute getAttribute() {
		return (IDomainObject.IAttribute)getSource();
	}


	private final T newValue;
	public T getNewValue() {
		return newValue;
	}
	
}
