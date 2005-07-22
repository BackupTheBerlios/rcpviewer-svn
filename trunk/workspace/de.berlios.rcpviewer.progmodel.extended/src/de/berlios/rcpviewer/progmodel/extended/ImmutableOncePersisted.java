package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * Indicates an attribute, reference or collection can be modified only until
 * the domain object has been initially saved. 
 *
 * <p>
 * Very often there are few constraints on a domain object's state while it is 
 * being created, however its state must be rigidly controlled once it has
 * been created.  This annotation can be applied to all members representing
 * an object's state (attribute, reference or collection) indicating that the
 * member may not be modified once the object has been persisted.
 * 
 * <p>
 * Alternatively the annotation may be applied to an entire type in which case
 * it is inherited by all members.  (Members can opt out of this by specifying
 * the <code>optout</code> attribute of true)
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ImmutableOncePersisted {
	boolean optout() default false;
}
