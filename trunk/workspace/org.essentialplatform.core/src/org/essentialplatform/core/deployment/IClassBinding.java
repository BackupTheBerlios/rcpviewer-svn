package org.essentialplatform.core.deployment;

import java.lang.annotation.Annotation;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * Placeholder for deployment-specific functionality for an 
 * {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IClassBinding<T> {
	
	/**
	 * Returns the specified annotation (if any) on the class.
	 * 
	 * @param <T>
	 * @param annotationClass
	 * @return
	 */
	public <Q extends Annotation> Q getAnnotation(Class<Q> annotationClass);

}