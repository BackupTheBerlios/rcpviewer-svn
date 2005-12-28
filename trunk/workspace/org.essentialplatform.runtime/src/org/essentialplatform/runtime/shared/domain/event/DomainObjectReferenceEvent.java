package org.essentialplatform.runtime.shared.domain.event;

import java.util.EventObject;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectReference;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * <p>
 * Parameterized by the type of the EReference that has changed.
 * 
 * @author Dan Haywood
 */
public final class DomainObjectReferenceEvent<T> extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public DomainObjectReferenceEvent(
			final IDomainObject.IObjectReference source, 
			final T referencedObject) {
		super(source);
		this.referencedObject = referencedObject;
	}
	
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the extended attribute that raised this event.
	 */
	public IDomainObject.IObjectReference getReference() {
		return (IDomainObject.IObjectReference)getSource();
	}

	private final T referencedObject;
	public T getReferencedObject() {
		return referencedObject;
	}
	
}
