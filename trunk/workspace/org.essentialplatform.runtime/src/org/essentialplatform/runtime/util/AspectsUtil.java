package org.essentialplatform.runtime.util;


import org.aspectj.lang.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.ConstructorSignature;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;

import org.essentialplatform.core.domain.IDomainClass;

import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.DomainObject;
import org.essentialplatform.runtime.domain.IPojo;

import org.essentialplatform.runtime.session.ISession;

public final class AspectsUtil {

	/**
	 * Cannot be instantiated.
	 *
	 */
	private AspectsUtil() {	}

	
	/**
	 * Looks up the {@link Field} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	public static Field getFieldFor(JoinPoint.StaticPart joinPointStaticPart) {
		FieldSignature signature = (FieldSignature)joinPointStaticPart.getSignature();
		// workaround to force signature to be able to pick up fields (soft references)
		Field[] fields = signature.getDeclaringType().getFields();
		Field field = signature.getField();
		return field;
	}

	/**
	 * Looks up the {@link Method} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	public static Method getMethodFor(JoinPoint.StaticPart joinPointStaticPart) {
		MethodSignature signature = (MethodSignature)joinPointStaticPart.getSignature();
		// workaround to force signature to be able to pick up methods (soft references)
		Method[] methods = signature.getDeclaringType().getMethods();
		return signature.getMethod();
	}

	
	/**
	 * Looks up the {@link Constructor} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	public static Constructor getConstructorFor(JoinPoint.StaticPart joinPointStaticPart) {
		ConstructorSignature signature = (ConstructorSignature)joinPointStaticPart.getSignature();
		// workaround to force signature to be able to pick up constructors (soft references)
		Constructor[] constructors = signature.getDeclaringType().getConstructors();
		return signature.getConstructor();
	}

	
	/**
	 * Looks up the {@link EAttribute} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	public static IDomainClass.IAttribute getIAttributeFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		Signature signature = joinPointStaticPart.getSignature();
		String name = signature.getName();
		return domainObject.getDomainClass().getIAttributeNamed(name);
	}


	/**
	 * Looks up the {@link EReference} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @param domainObject
	 * @param joinPoint representing either an addTo or a removeFrom operation.
	 */
	public static IDomainClass.IReference getIReferenceFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		Signature signature = joinPointStaticPart.getSignature();
		String name = signature.getName();
		if (name.startsWith("addTo")) {
			name = StringUtil.camelCase(name.substring("addTo".length()));
		} else if (name.startsWith("removeFrom")) {
			name = StringUtil.camelCase(name.substring("removeFrom".length()));
		}
		return domainObject.getDomainClass().getIReferenceNamed(name);
	}
	


	/**
	 * Looks up the {@link EOperation} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	public static IDomainClass.IOperation getIOperationFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		Signature signature = joinPointStaticPart.getSignature();
		String name = signature.getName();
		return domainObject.getDomainClass().getIOperationNamed(name);
	}


	/**
	 * Looks up the {@link IDomainObject.IAttribute} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @return attribute or null
	 */
	public static IDomainObject.IObjectAttribute getAttributeFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		IDomainClass.IAttribute iAttribute = getIAttributeFor(domainObject, joinPointStaticPart);
		if (iAttribute == null) {
			return null;
		}
		return domainObject.getAttribute(iAttribute);
	}


	/**
	 * Looks up the {@link IDomainObject.IOneToOneReference} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @return reference or null
	 */
	public static IDomainObject.IObjectOneToOneReference getOneToOneReferenceFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		IDomainClass.IReference iReference = getIReferenceFor(domainObject, joinPointStaticPart);
		if (iReference == null) {
			return null;
		}
		return domainObject.getOneToOneReference(iReference);
	}


	/**
	 * Looks up the {@link IDomainObject.ICollectionReference} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 * 
	 * @param domainObject
	 * @param joinPoint representing either an addTo or a removeFrom operation.
	 * @return reference or null
	 */
	public static IDomainObject.IObjectCollectionReference getCollectionReferenceFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		IDomainClass.IReference iReference = getIReferenceFor(domainObject, joinPointStaticPart);
		if (iReference == null) {
			return null;
		}
		return domainObject.getCollectionReference(iReference);
	}

	
	/**
	 * Looks up the {@link IDomainObject.IAttribute} that corresponds to the
	 * signature represented by the supplied {@link JoinPoint.StaticPart}.
	 * 
	 * <p>
	 * This is a helper method provided for the convenience of subaspects.
	 */
	public static IDomainObject.IObjectOperation getOperationFor(final IDomainObject domainObject, JoinPoint.StaticPart joinPointStaticPart) {
		IDomainClass.IOperation iOperation = getIOperationFor(domainObject, joinPointStaticPart);
		if (iOperation == null) {
			return null;
		}
		return domainObject.getOperation(iOperation);
	}


}
