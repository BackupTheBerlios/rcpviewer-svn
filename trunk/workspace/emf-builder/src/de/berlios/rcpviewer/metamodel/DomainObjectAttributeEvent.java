package de.berlios.rcpviewer.metamodel;

import java.util.EventObject;

import org.eclipse.emf.ecore.EAttribute;

/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * <p>
 * Parameterized by the type of the EAttribute that has changed.
 * 
 * @author Dan Haywood
 */
public final class DomainObjectAttributeEvent<T> extends DomainObjectEvent {

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
			final IDomainObject source, 
			final EAttribute eAttribute, 
			final T newValue) {
		super(source);
		this.eAttribute = eAttribute;
		this.newValue = newValue;
	}
	
	private final EAttribute eAttribute;
	/**
	 * The attribute that has changed.
	 * @return
	 */
	public EAttribute getEAttribute() {
		return eAttribute;
	}

	private final T newValue;
	public T getNewValue() {
		return newValue;
	}
	
}
