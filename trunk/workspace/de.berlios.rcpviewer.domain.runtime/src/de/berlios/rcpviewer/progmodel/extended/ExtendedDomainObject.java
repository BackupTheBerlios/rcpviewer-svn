package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.domain.AbstractDomainObjectAdapter;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.authorization.IAuthorizationManager;

public class ExtendedDomainObject<T> extends AbstractDomainObjectAdapter<T> {

	public ExtendedDomainObject(IDomainObject<T> domainObject) {
		super(domainObject);
	}

	public ExtendedRuntimeDomainClass<T> getExtendedRuntimeDomainClass() {
		IRuntimeDomainClass<T> domainClass = adapts().getDomainClass(); 
		return (ExtendedRuntimeDomainClass<T>)domainClass.getAdapter(ExtendedRuntimeDomainClass.class); 
	}

	/**
	 * Convenience method that combines all prerequisites to access/modify
	 * an attribute.
	 * 
	 * <p>
	 * There are three sets of prerequisites that can apply:
	 * <ul>
	 * <li> the prerequisites to access the attribute, as defined in the 
	 *      programming model by the <code>getXxxPre()</code>; 
	 *      see {@link #accessorPrerequisitesFor(EAttribute)}
	 * <li> the prerequisites to modify the attribute, as defined in the
	 *      programming model by the <code>setXxxPre(...)</code>;
	 *      see {@link #mutatorPrerequisitesFor(EAttribute, Object)}.
	 * <li> the prerequisites of the configured {@link IAuthorizationManager},
	 *      see {@link #authorizationPrerequisitesFor(EAttribute)}.
	 * </ul>
	 *  
	 * <p>
	 * If there is no mutator then the method can still be called with
	 * <code>null</code> as the candidate value.
	 * 
	 * @param eAttribute
	 * @param candidateValue
	 * @return
	 */
	public IPrerequisites prerequisitesFor(
			final EAttribute eAttribute, final Object candidateValue)  {
		return          accessorPrerequisitesFor(eAttribute)
		    .andRequire(authorizationPrerequisitesFor(eAttribute))
			.andRequire(mutatorPrerequisitesFor(eAttribute, candidateValue));
	}
	

	/**
	 * Prerequisites applicable to access this attribute.
	 * 
	 * <p>
	 * In the programming model, the prerequisites 
	 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
	 * returned by the <code>getXxxPre()</code> method.  Note there may also be
	 * prerequisites corresponding to whether the attribute can be modified
	 * (in other words validation), see {@link #mutatorPrerequisitesFor(EAttribute)}.
	 * In the programming model these correspond to the 
	 * <code>setXxxPre(...)</code> method.
	 * 
	 * <p>
	 * In addition, there may be authorization prerequisites, see
	 * {@link #authorizationPrerequisitesFor(EAttribute)}.
	 * 
	 * @param attribute
	 */
	public IPrerequisites accessorPrerequisitesFor(EAttribute eAttribute)  {
		ExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
		
		Method accessorPre = erdc.getAccessorPre(eAttribute);
		if (accessorPre == null) {
			return Prerequisites.none();
		}
		
		try {
			return (IPrerequisites)accessorPre.invoke(adapts().getPojo(), new Object[]{});
		} catch (IllegalArgumentException ex) {
			// TODO log?
		} catch (IllegalAccessException ex) {
			// TODO Auto-generated catch block
		} catch (InvocationTargetException ex) {
			// TODO Auto-generated catch block
		}
		return Prerequisites.none();
	}

	/**
	 * Prerequisites applicable to modify this attribute with a specific value
	 * (in other words, validation).
	 * 
	 * <p>
	 * The prerequisites are dependent upon the candidate value for the
	 * attribute.  For example, an attribute might not accept negative values;
	 * if so then these prerequisites would effectively veto that candidate
	 * value.
	 * 
	 * <p>
	 * In the programming model, the domain object prerequisites 
	 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
	 * returned by the <code>setXxxPre(..)</code> method.  Note there will also 
	 * be prerequisites corresponding to be able to access the attribute, 
	 * see {@link #accessorPrerequisitesFor(EAttribute)}.  In the 
	 * programming model these correspond to the <code>getXxxPre()</code> 
	 * method.
	 * 
	 * <p>
	 * In addition, there may be authorization prerequisites, see
	 * {@link #authorizationPrerequisitesFor(EAttribute)}.
	 * 
	 * @param attribute
	 * @param candidateValue - the new value for this attribute will take, if meets prerequisites.
	 */
	public IPrerequisites mutatorPrerequisitesFor(
			final EAttribute eAttribute, final Object candidateValue)  {
		ExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
		
		Method mutatorPre = erdc.getMutatorPre(eAttribute);
		if (mutatorPre == null) {
			return Prerequisites.none();
		}
		
		try {
			return (IPrerequisites)mutatorPre.invoke(adapts().getPojo(), new Object[]{candidateValue});
		} catch (IllegalArgumentException ex) {
			// TODO log?
		} catch (IllegalAccessException ex) {
			// TODO Auto-generated catch block
		} catch (InvocationTargetException ex) {
			// TODO Auto-generated catch block
		}
		return Prerequisites.none();
	}

	/**
	 * Prerequisites applicable to view/edit this attribute according to the
	 * configured {@link IAuthorizationManager}.
	 * 
	 * @param eAttribute
	 * @return
	 */
	public IPrerequisites authorizationPrerequisitesFor(EAttribute eAttribute)  {
		IRuntimeDomainClass<T> rdc = adapts().getDomainClass();
		return rdc.authorizationConstraintFor(eAttribute);
	}



}
