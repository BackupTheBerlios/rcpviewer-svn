package de.berlios.rcpviewer.metamodel.annotations;
import java.lang.annotation.*;

/**
 * Derived (implies also transient and volatile) attribute, reference or 
 * collection.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface Derived { }
