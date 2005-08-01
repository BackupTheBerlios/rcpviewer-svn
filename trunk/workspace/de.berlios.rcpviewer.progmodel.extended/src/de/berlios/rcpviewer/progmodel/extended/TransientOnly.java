package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * Indicates that a type cannot be persisted.
 *
 * <p>
 * Most objects are persisted to some sort of persistent object store.  
 * Occasionally though there is the requirement for an object which is not
 * persisted.
 * 
 * <p>
 * For example, an application might provide some sort of "what-if" capability,
 * for a Forecast for a project planning application, say.  The Forecast object
 * might allow the end user to predict some outcome, and might even have some
 * report feature, but wouldn't be persistable in its own right.
 * 
 * <p>
 * Another example: an application might use an object that mimics a paper 
 * form.  The end user would create this form object and fill in the details, 
 * but would then invoke an operation that would instruct the form to post the
 * relevant information to the appropriate domain objects and then to discard
 * itself.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface TransientOnly {
}
