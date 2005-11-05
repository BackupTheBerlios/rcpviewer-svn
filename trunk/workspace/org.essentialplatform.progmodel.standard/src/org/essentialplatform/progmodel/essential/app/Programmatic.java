package org.essentialplatform.progmodel.essential.app;
import java.lang.annotation.*;

/**
 * Whether this method is for programmatic purposes only, that is, not to be
 * exposed in the user-interface.
 * 
 * <p>
 * Only applies to methods that could represent operations; ignored if applied
 * to methods relating to attributes (eg accessors and mutators).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface Programmatic {
}
