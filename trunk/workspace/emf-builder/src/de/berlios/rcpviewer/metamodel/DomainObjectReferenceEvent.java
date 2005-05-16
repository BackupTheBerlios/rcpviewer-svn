package de.berlios.rcpviewer.metamodel;

import java.util.EventObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * <p>
 * Parameterized by the type of the EReference that has changed.
 * 
 * @author Dan Haywood
 */
public final class DomainObjectReferenceEvent<T> extends DomainObjectEvent {

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
			final IDomainObject source, 
			final EReference eReference, 
			final T referencedObject) {
		super(source);
		this.eReference = eReference;
		this.referencedObject = referencedObject;
	}
	
	private final EReference eReference;
	/**
	 * The reference that has changed.
	 * @return
	 */
	public EReference getEReference() {
		return eReference;
	}

	private final T referencedObject;
	public T getReferencedObject() {
		return referencedObject;
	}
	
}
