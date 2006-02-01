package org.essentialplatform.aop.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mandatory parameter.
 * 
 * <p>
 * If set to null, then throws a {@link java.lang.IllegalArgumentException}.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Mandatory {
}
