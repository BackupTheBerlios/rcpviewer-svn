package org.essentialplatform.runtime.client.domain.event;

import java.util.EventObject;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.IDomainObject;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public final class ExtendedDomainObjectReferenceEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public ExtendedDomainObjectReferenceEvent(
			final IDomainObject.IObjectReference source, //  
			final IPrerequisites newPrerequisites) {
		super(source);
		this.newPrerequisites = newPrerequisites;
	}
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the reference that raised this event.
	 */
	public IDomainObject.IObjectReference getReference() {
		return (IDomainObject.IObjectReference)getSource();
	}



	private final IPrerequisites newPrerequisites;
	public IPrerequisites getNewPrerequisites() {
		return newPrerequisites;
	}
	
}
