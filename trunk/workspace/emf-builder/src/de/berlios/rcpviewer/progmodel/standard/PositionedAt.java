package de.berlios.rcpviewer.progmodel.standard;
import java.lang.annotation.*;

/**
 * Provide metadata to specify the positioning of an attribute or 
 * operation.
 * 
 * <p>
 * The value of the annotation determines the order relative to other
 * attributes or operations.  
 * 
 * <p>
 * For an attribute, the annotation should be specified on the accessor
 * (getter), or the mutator (setter) if there is no getter.  If specified
 * on both, then only the accessor's is used.
 * 
 * <p>
 * Specifying this annotation on things other than an attribute or 
 * operation has no effect.
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface PositionedAt {
	int value();
}
