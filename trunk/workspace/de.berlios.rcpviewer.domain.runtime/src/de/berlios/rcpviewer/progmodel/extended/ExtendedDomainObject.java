package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.domain.AbstractDomainObjectAdapter;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
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
	 * Whether the attribute can be set at all.
	 * 
	 * <p>
	 * In the standard programming model, corresponds to the 
	 * {@link de.berlios.rcpviewer.progmodel.extended.IConstraintSet} returned by
	 * the <code>xxxPre()</code> method. 
	 * 
	 * @param attribute
	 */
	public IConstraintSet constraintFor(EAttribute eAttribute)  {
		Method attributePre = getExtendedRuntimeDomainClass().getAttributePre(eAttribute);
		if (attributePre == null) {
			return ConstraintSet.create();
		}
		try {
			return (IConstraintSet)attributePre.invoke(adapts().getPojo(), new Object[]{});
		} catch (IllegalArgumentException ex) {
			// TODO log?
		} catch (IllegalAccessException ex) {
			// TODO Auto-generated catch block
		} catch (InvocationTargetException ex) {
			// TODO Auto-generated catch block
		}
		return ConstraintSet.create();
	}



}
