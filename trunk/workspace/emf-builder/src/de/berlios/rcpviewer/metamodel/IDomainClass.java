package de.berlios.rcpviewer.metamodel;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;

public interface IDomainClass {

	public Class getJavaClass();
	
	public EClass getEClass();

	public int getNumberOfAttributes();

	/**
	 * Since EClass (as of EMF 2.0) doesn't expose, we provide as a convenience.
	 * @param attributeName
	 * @return
	 */
	public EAttribute getEAttributeNamed(String attributeName);
	
}
