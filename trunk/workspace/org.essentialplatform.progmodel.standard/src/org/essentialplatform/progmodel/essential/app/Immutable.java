package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * Indicates an immutable (read-only) type or a collection of a type. 
 *
 * <p>
 * If applied to the a reference representing a collection (1:m), then 
 * indicates that elements cannot be added or removed from the collection.
 * 
 * <p> 
 * If applied to an entire type, then indicates that the state of the type
 * cannot be changed (except programmatically).
 * 
 * <p>
 * The annotation is ignored if applied elsewhere (eg if applied to an accessor 
 * or mutator representing an attribute, to a reference representing a
 * single object (1:1), or if applied to a method representing a operation).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Immutable { }
