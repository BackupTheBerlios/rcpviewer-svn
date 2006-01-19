package org.essentialplatform.core.deployment;

import org.eclipse.emf.ecore.EClass;
import org.essentialplatform.core.domain.IDomain;

/**
 * Placeholder for deployment-specific functionality for an {@link IDomain}.
 * 
 * @author Dan Haywood
 */
public interface IDomainBinding {
	public String getPackageNameFor(final Object classRepresentation);
	public String getClassSimpleNameFor(final Object classRepresentation);
	/**
	 * Perform any post-creation processing on the EClass specific to this
	 * deployment binding.
	 * 
	 * <p>
	 * For example, runtime binding will set the Java {@link java.lang.Class}
	 * on the {@link EClass}' <tt>instanceClass</tt> property.
	 * @param eClass
	 * @param classRepresentation
	 */
	public void processEClass(final EClass eClass, final Object classRepresentation);
	
	/**
	 * Lookup of this deployment's representation of the class for the
	 * specified {@link EClass}.
	 * 
	 * @param eClass
	 * @return
	 */
	public Object classRepresentationFor(EClass eClass);
}