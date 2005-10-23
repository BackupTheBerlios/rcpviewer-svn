package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

public interface IExtendedRuntimeDomainClass<T> {

	public <V> boolean isCompatible(Class<V> adapterClassName);

	/**
	 * Returns the <code>getXxxPre()</code> for an attribute, or none if there 
	 * is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, accept no arguments and must 
	 * return an {@link IPrerequisites}. 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getAccessorPre(EAttribute eAttribute);
	/**
	 * Returns the <code>setXxxPre()</code> for an attribute, or none if there 
	 * is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, accept a single argument of the 
	 * same type as the attribute and return an {@link IPrerequisites}.
	 * 
	 * @param reference
	 * @return
	 */
	public Method getMutatorPre(EAttribute eAttribute);

	/**
	 * Returns the <code>getXxxPre()</code> for a (single- or (multi-
	 * valued) reference, or none if there is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, accept no arguments and must 
	 * return an {@link IPrerequisites}. 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getAccessorPre(EReference reference);
	
	/**
	 * Returns the <code>setXxxPre()</code> for a single-valued (simple) 
	 * reference, or none if there is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, accept a single argument of the 
	 * same type as the reference and must return an {@link IPrerequisites}. 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getMutatorPre(EReference reference);
	/**
	 * Returns the <code>addToXxxPre()</code> for a multi-valued (collection) 
	 * reference, or none if there is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, accept a single argument of the 
	 * same type as the reference and must return an {@link IPrerequisites}. 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getAddToPre(EReference reference);
	/**
	 * Returns the <code>removeFromXxxPre()</code> for a multi-valued (collection) 
	 * reference, or none if there is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, accept a single argument of the 
	 * same type as the reference and must return an {@link IPrerequisites}. 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getRemoveFromPre(EReference reference);

	/**
	 * Returns the <code>XxxPre()</code> for an operation 
	 * <code>xxx(...)</code>, or none if there is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, have a set of arguments with
	 * identical types to that of the operation, and must return an 
	 * {@link IPrerequisites}. 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getInvokePre(EOperation operation);

	/**
	 * Returns the <code>XxxPre()</code> for an operation 
	 * <code>xxx(...)</code>, or none if there is none.
	 * 
	 * <p>
	 * The method must be <code>public</code>, have a set of arguments that
	 * are an array of the correspond types in the operation, and must return  
	 * void. 
	 * 
	 * @param operation
	 * @return
	 */
	public Method getInvokeDefaults(EOperation operation);


}