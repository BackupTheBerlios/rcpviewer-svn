package de.berlios.rcpviewer.progmodel.standard;
import java.lang.annotation.*;

/**
 * Indicates an immutable (read-only) reference or type. 
 *
 * <p>
 * If applied to the (accessor of) a reference, indicates that the reference
 * cannot be changed.  If the reference is a collection (1:m), then indicates 
 * that elements cannot be added or removed from the collection.
 * 
 * <p> 
 * If applied to an entire type, then indicates that the state of the type
 * cannot be changed (except programmatically).
 * 
 * <p>
 * Ignore if applied elsewhere (eg if applied to an accessor or mutator 
 * representing an attribute, or if applied to a method representing a
 * (user) operation).
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
