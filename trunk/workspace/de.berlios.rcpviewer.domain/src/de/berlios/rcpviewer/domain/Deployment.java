package de.berlios.rcpviewer.domain;

/**
 * Parameterizes domain model with respect to how it has been deployed,
 * acting as an <i>Abstract Factory</i> (kit) for each.
 * 
 * <p>
 * There are two bindings: RUNTIME and COMPILETIME.  This class also
 * acts as a singleton registry of either (similar to {@link java.util.Calendar}
 * with {@link java.util.GregorianCalendar}
 * 
 * 
 * 
 * @author Dan Haywood
 */
public abstract class Deployment {
	
	/**
	 * Sets the binding, as returned by {@link #getDeployment()}.
	 * 
	 * @throws RuntimeException if a binding has already been instantiated.
	 */
	protected Deployment() {
		synchronized(Deployment.class) {
			if (deployment != null) {
				throw new RuntimeException("Binding already defined.");
			}
			deployment = this;
		}
	}

	/**
	 * The current binding (if any).
	 */
	private static Deployment deployment;
	
	/**
	 * Returns the current binding.
	 * 
	 * 
	 * @return
	 * @throws RuntimeException if no binding has been instantiated.
	 */
	public static Deployment getDeployment() {
		if (deployment == null) {
			throw new RuntimeException("No binding set.");
		}
		return deployment;
	}

	/**
	 * Binding for an attribute for a specific deployment.
	 * 
	 * @author Dan Haywood
	 */
	public interface IAttributeBinding<D extends Deployment> {}
	public abstract <D extends Deployment> IAttributeBinding<D> getBinding(IDomainClass.IAttribute attribute);
	
}
