package org.essentialplatform.progmodel.standard;
import java.lang.annotation.*;

/**
 * The upper bound for this attribute (corresponds to UpperBound, that is, 
 * maximum cardinality, in meta model).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface UpperBoundOf {
	int value();
}
