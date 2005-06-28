package de.berlios.rcpviewer.progmodel.standard;
import java.lang.annotation.*;

/**
 * Indicates that the reference has an opposite reference (that is, is 
 * bidirectional). 
 * 
 * <p>
 * Apply to the getter of a reference or a collection.  (In the case of a 
 * reference, may also apply to a setter if no getter).  If applied to any other
 * method then will be ignored.  It is only necessary to apply to one side of
 * a relationship (but is not an error if applied to both). 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface OppositeOf {
	/**
	 * Name of the reference that represents this relationship in the other
	 * direction.
	 * 
	 * @return
	 */
	String value();
}
