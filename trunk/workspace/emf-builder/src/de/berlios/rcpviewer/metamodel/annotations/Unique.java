package de.berlios.rcpviewer.metamodel.annotations;
import java.lang.annotation.*;

/**
 * Whether this (upper bound > 1) attribute requires uniqueness.
 * 
 * <p>
 * The default value of this annotation reflects the EMF default.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface Unique {
	boolean value() default true;
}
