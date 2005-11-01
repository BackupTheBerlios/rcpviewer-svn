package org.essentialplatform.progmodel.standard;
import java.lang.annotation.*;

/**
 * Whether this (upper bound > 1) attribute requires ordering.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * <p>
 * The default value of this annotation reflects the EMF default.
 * 
 * <p>
 * TODO: would be rather nice if it supported a comparator.  However, this
 * comparator would need to be stored in an EAnnotation.  Also, since we
 * can't have null as a default, would need to provide a "do-everything"
 * comparator class.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface Ordered {
	boolean value() default true;
}
