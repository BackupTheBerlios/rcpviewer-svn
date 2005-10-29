package de.berlios.rcpviewer.session;

import java.util.EventObject;

import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;


/**
 * Event object for events that impact an {@link IExtendedDomainObject}.
 * 
 * @author Dan Haywood
 */
public final class ExtendedDomainObjectOperationEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public ExtendedDomainObjectOperationEvent(
			final IDomainObject.IObjectOperation source, // workaround 
			final IPrerequisites newPrerequisites) {
		super(source);
		this.newPrerequisites = newPrerequisites;
	}
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the attribute that raised this event.
	 */
	public IDomainObject.IObjectOperation getOperation() {
		return (IDomainObject.IObjectOperation)getSource();
	}


	private final IPrerequisites newPrerequisites;
	public IPrerequisites getNewPrerequisites() {
		return newPrerequisites;
	}
}
