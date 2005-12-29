/**
 * 
 */
package org.essentialplatform.core.deployment;

import java.lang.annotation.Annotation;

public interface IClassClientBinding<T> {
	
	/**
	 * Returns the specified annotation (if any) on the class.
	 * 
	 * @param <T>
	 * @param annotationClass
	 * @return
	 */
	public <Q extends Annotation> Q getAnnotation(Class<Q> annotationClass);

}