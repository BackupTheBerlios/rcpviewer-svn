package de.berlios.rcpviewer.session;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public final class DomainObjectOperationEvent extends DomainObjectEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public DomainObjectOperationEvent(
			final IDomainObject source, 
			final EOperation eOperation,
			final Object[] args) {
		super(source);
		this.eOperation = eOperation;
		this.args = args;
	}
	
	private final EOperation eOperation;
	/**
	 * The operation that has been invoked.
	 * @return
	 */
	public EOperation getEOperation() {
		return eOperation;
	}

	private final Object[] args;
	public Object[] getArgs() {
		return args;
	}
}
