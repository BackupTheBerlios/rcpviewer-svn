package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.domain.AbstractDomainObjectAdapter;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.runtime.IRuntimeDomain;
import de.berlios.rcpviewer.session.IDomainObject;

public class ExtendedDomainObject<T> extends AbstractDomainObjectAdapter<T> {

	public ExtendedDomainObject(IDomainObject<T> domainObject) {
		super(domainObject);
	}

	public ExtendedRuntimeDomainClass<T> getExtendedRuntimeDomainClass() {
		IRuntimeDomainClass<T> domainClass = adapts().getDomainClass(); 
		return (ExtendedRuntimeDomainClass<T>)domainClass.getAdapter(ExtendedRuntimeDomainClass.class); 
	}

	/**
	 * Constraints applicable on this attribute.
	 * 
	 * <p>
	 * The constraints in force are the combination of:
	 * <ul>
	 * <li> whether the business rules encapsulated within the domain object 
	 *      indicates that the attribute can be used/viewed, and
	 * <li> whether the configured {@link de.berlios.rcpviewer.authorization.IAuthorizationManager}
	 *      constrains the current user to use this feature of this class of
	 *      object.
	 * </ul>
	 * 
	 * <p>
	 * In the standard programming model, the domain object constraints 
	 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
	 * returned by the <code>xxxPre()</code> method.
	 * 
	 * @param attribute
	 */
	public IPrerequisites prerequisiteFor(EAttribute eAttribute)  {
		IRuntimeDomainClass<T> rdc = adapts().getDomainClass();
		IPrerequisites authorizationPrerequisites = 
			rdc.authorizationConstraintFor(eAttribute);
		
		IPrerequisites domainObjectPrerequisites = domainObjectConstraintFor(eAttribute);
		return authorizationPrerequisites.andRequire(domainObjectPrerequisites);
	}
	
	/**
	 * Whether the attribute can be set at all, according to the domain object
	 * model.
	 * 
	 * @param attribute
	 */
	private IPrerequisites domainObjectConstraintFor(EAttribute eAttribute) {
		ExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
		
		Method attributePre = erdc.getAttributePre(eAttribute);
		if (attributePre == null) {
			return Prerequisites.noop();
		}
		
		try {
			return (IPrerequisites)attributePre.invoke(adapts().getPojo(), new Object[]{});
		} catch (IllegalArgumentException ex) {
			// TODO log?
		} catch (IllegalAccessException ex) {
			// TODO Auto-generated catch block
		} catch (InvocationTargetException ex) {
			// TODO Auto-generated catch block
		}
		return Prerequisites.noop();

	}



}
